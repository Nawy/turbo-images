package com.turbo.service;

import com.turbo.model.*;
import com.turbo.model.aerospike.CommentRepoModel;
import com.turbo.model.aerospike.PostRepoModel;
import com.turbo.model.dto.PostContentDto;
import com.turbo.model.dto.PostRatingDto;
import com.turbo.model.exception.InternalServerErrorHttpException;
import com.turbo.model.exception.NotFoundHttpException;
import com.turbo.model.search.SearchOrder;
import com.turbo.model.search.SearchPattern;
import com.turbo.model.search.SearchPeriod;
import com.turbo.model.search.SearchSort;
import com.turbo.model.search.field.PostField;
import com.turbo.model.search.field.stat.PostDiffStatField;
import com.turbo.model.search.field.stat.PostStatPeriod;
import com.turbo.repository.aerospike.PostRepository;
import com.turbo.repository.elasticsearch.content.PostSearchRepository;
import com.turbo.repository.elasticsearch.statistic.PostStatisticRepository;
import com.turbo.service.statistic.StatisticService;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.ObjectUtils.firstNonNull;

/**
 * Created by rakhmetov on 09.05.17.
 */
@Log
@Service
@AllArgsConstructor
public class PostService {

    private static final int PAGE_SIZE = 30;

    private final PostSearchRepository postSearchRepository;
    private final PostStatisticRepository postStatisticRepository;
    private final PostRepository postRepository;
    private final UserService userService;
    private final UserImageService userImageService;
    private final StatisticService statisticService;

    public Post save(final PostRepoModel post) {
        return post.getId() != null ?
                upsert(post) :
                addPost(post);
    }

    private Post addPost(PostRepoModel postRepoModel) {
        PostRepoModel postWithId = postRepository.save(postRepoModel);
        Post post = getPostById(postWithId.getId());
        postSearchRepository.addPost(post);
        return post;
    }

    private Post upsert(PostRepoModel post) {
        PostRepoModel postAfterUpdate = postRepository.save(post);
        Post updatedPost = getPostById(postAfterUpdate.getId());
        //FIXME i can update too long
        postSearchRepository.upsertPost(updatedPost);
        return updatedPost;
    }

    void saveRawPost(PostRepoModel postRepoModel) {
        postRepository.save(postRepoModel);
    }

    PostRepoModel getRawPost(long id) {
        return postRepository.get(id);
    }

    private Post updateReindexContent(final PostRepoModel post) {
        PostRepoModel postWithId = postRepository.save(post);
        Post updatedPost = getPostById(postWithId.getId());
        statisticService.updatePostContent(
                updatedPost.getId(),
                updatedPost.getName(),
                updatedPost.getDescription(),
                updatedPost.getTags()
        );
        return updatedPost;
    }

    private void reindexContent(final PostRepoModel postWithId) {
        Post updatedPost = getPostById(postWithId.getId());
        statisticService.updatePostContent(
                updatedPost.getId(),
                updatedPost.getName(),
                updatedPost.getDescription(),
                updatedPost.getTags()
        );
    }

    private void reindexRating(final PostRatingDto ratingDto) {
        statisticService.updatePostRating(
                ratingDto.getPostId(),
                ratingDto.getRating(),
                ratingDto.getViews()
        );
    }

    public Post updatePostContent(final PostContentDto postContentDto) {
        PostRepoModel postRepoModel = postRepository.get(postContentDto.getPostId());
        PostRepoModel updatedRepoModel = new PostRepoModel(
                postRepoModel.getId(),
                firstNonNull(postContentDto.getName(), postRepoModel.getName()),
                postRepoModel.getRating(),
                postRepoModel.getViews(),
                firstNonNull(postContentDto.getImageIds(), postRepoModel.getImages()),
                postRepoModel.getDeviceType(),
                firstNonNull(postContentDto.getTags(), postRepoModel.getTags()),
                postRepoModel.getUserId(),
                postRepoModel.getCreationDateTime(),
                postRepoModel.isVisible(),
                firstNonNull(postContentDto.getDescription(), postRepoModel.getDescription()),
                postRepoModel.getComments()
        );

        PostRepoModel postWithId = postRepository.save(updatedRepoModel);
        reindexContent(postWithId);
        return makePost(updatedRepoModel);
    }

    public Post updatePostRating(PostRatingDto postRatingDto) {
        PostRepoModel postRepoModel = postRepository.get(postRatingDto.getPostId());
        final Rating current = postRepoModel.getRating();
        final Rating rating = new Rating(
                current.getUps() + (postRatingDto.getRating() > 0 ? 1 : 0),
                current.getUps() + (postRatingDto.getRating() < 0 ? 1 : 0),
                current.getRating() + postRatingDto.getRating()
        );
        PostRepoModel updatedRepoModel = new PostRepoModel(
                postRepoModel.getId(),
                postRepoModel.getName(),
                rating,
                postRepoModel.getViews() + postRatingDto.getViews(),
                postRepoModel.getImages(),
                postRepoModel.getDeviceType(),
                postRepoModel.getTags(),
                postRepoModel.getUserId(),
                postRepoModel.getCreationDateTime(),
                postRepoModel.isVisible(),
                postRepoModel.getDescription(),
                postRepoModel.getComments()
        );
        postRepository.save(updatedRepoModel);
        reindexRating(postRatingDto);
        return makePost(updatedRepoModel);
    }

    public Post getPostById(final long id) {
        PostRepoModel postRepoModel = postRepository.get(id);
        if (postRepoModel == null) throw new NotFoundHttpException("can't find post with such id");
        return makePost(postRepoModel);
    }

    private Post makePost(PostRepoModel postRepoModel) {
        //get Users
        List<Long> commentUserIds = postRepoModel.getComments().values().stream().map(CommentRepoModel::getUserId).collect(Collectors.toList());
        Set<Long> userIds = new HashSet<>(commentUserIds);
        userIds.add(postRepoModel.getUserId());
        List<User> users = userService.bulkGet(userIds);
        Map<Long, User> userMap = users.stream().collect(Collectors.toMap(User::getId, Function.identity()));
        //make Comments
        Map<Long, Comment> comments = postRepoModel.getComments().values().stream()
                .map(commentRepoModel -> makeComment(commentRepoModel, userMap.get(commentRepoModel.getUserId())))
                .collect(Collectors.toMap(Comment::getId, Function.identity()));
        //get user images
        List<UserImage> userImages = userImageService.getUserImages(postRepoModel.getImages());
        //make post
        return makePost(
                postRepoModel,
                userMap.get(postRepoModel.getUserId()),
                new HashSet<>(userImages),
                comments
        );
    }

    public List<Post> getMostViral(int page, SearchPeriod searchPeriod, SearchSort searchSort) {

        List<Long> resultIds = null;

        // NEWEST
        if (searchSort == SearchSort.DATE) {
            resultIds = postSearchRepository.getNewestPost(page);
        }

        // RATING or VIEWS for ALL_TIME from CONTENT
        if (searchPeriod == SearchPeriod.ALL_TIME && searchSort != SearchSort.DATE) {
            final PostField sortingField = getContentPostField(searchSort);

            resultIds = postSearchRepository.getSortedPost(page, sortingField, SearchOrder.DESC);
        }

        //RATING or VIEWS for specific time from STATISTICS
        if (searchSort != SearchSort.DATE && searchPeriod != SearchPeriod.ALL_TIME) {
            final PostDiffStatField sortingField = getStatPostField(searchSort);

            PostStatPeriod period;
            LocalDate specificDate;
            switch (searchPeriod) {
                case DAY:
                    specificDate = LocalDate.now();
                    period = PostStatPeriod.DAYS;
                    break;
                case WEEK:
                    final int currentDayOfWeek = LocalDate.now().getDayOfWeek().getValue();
                    specificDate = LocalDate.now().minusDays(currentDayOfWeek - 1);
                    period = PostStatPeriod.WEEKS;
                    break;
                case MONTH:
                    final int currentDayOfMonth = LocalDate.now().getDayOfMonth();
                    specificDate = LocalDate.now().minusDays(currentDayOfMonth - 1);
                    period = PostStatPeriod.MONTHS;
                    break;
                case YEAR:
                    final int currentDayOfYear = LocalDate.now().getDayOfYear();
                    specificDate = LocalDate.now().minusDays(currentDayOfYear - 1);
                    period = PostStatPeriod.YEAR;
                    break;
                default:
                    throw new InternalServerErrorHttpException(" unknown search period");
            }

            Objects.requireNonNull(specificDate);
            Objects.requireNonNull(period);
            resultIds = postStatisticRepository
                    .getPostStat()
                    .date(specificDate, period)
                    .sort(sortingField, SearchOrder.DESC)
                    .page(page)
                    .execute();
        }

        // PROCESSING ALL OF REQUESTS
        Objects.requireNonNull(resultIds);
        if (resultIds.isEmpty()) {
            throw new NotFoundHttpException("Post not found!");
        }

        return bulkGetPreviewPosts(resultIds);
    }

    public List<Post> getUserPosts(int page, long userId, SearchPattern searchPattern) {

        List<Long> resultIds;

        if (searchPattern.getSort() == SearchSort.DATE) {
            // ONLY NEWEST
            resultIds = postSearchRepository.getPostByAuthor(
                    userId,
                    page,
                    searchPattern.getPeriod(),
                    PostField.CREATION_DATE,
                    SearchOrder.DESC
            );
        } else {
            // RATING/VIEWS in specific order
            final PostField sortingField = getContentPostField(searchPattern.getSort());

            resultIds = postSearchRepository.getPostByAuthor(
                    userId,
                    page,
                    searchPattern.getPeriod(),
                    sortingField,
                    searchPattern.getOrder()
            );
        }

        return bulkGetPreviewPosts(resultIds);
    }

    public List<Post> getPostsByDate( final LocalDateTime dateTime) {
        final List<Long> postsIds = postSearchRepository.getPosts(dateTime, PAGE_SIZE);
        return bulkGetPreviewPosts(postsIds);
    }

    public List<Post> getUserPostsByDate(final long userId, final LocalDateTime dateTime) {
        final List<Long> postsIds = postSearchRepository.getUserPosts(userId, dateTime, PAGE_SIZE);
        return bulkGetPreviewPosts(postsIds);
    }

    public void deletePost(long id) {
        postRepository.delete(id);
        //TODO add Elastic remove entity
    }

    private PostField getContentPostField(SearchSort searchSort) {
        switch (searchSort) {
            case RATING:
                return PostField.RATING;
            case VIEWS:
                return PostField.VIEWS;
            default:
                throw new InternalServerErrorHttpException(" unknown sorting field");
        }
    }

    private PostDiffStatField getStatPostField(SearchSort searchSort) {
        switch (searchSort) {
            case RATING:
                return PostDiffStatField.RATING;
            case VIEWS:
                return PostDiffStatField.VIEWS;
            default:
                throw new InternalServerErrorHttpException(" unknown sorting field in statistic");
        }
    }

    private List<Post> bulkGetPreviewPosts(List<Long> postIds) {
        if (postIds.isEmpty()) return Collections.emptyList();
        List<PostRepoModel> postRepoModels = postRepository.bulkGet(postIds);
        Set<Long> userIds = postRepoModels.stream()
                .map(PostRepoModel::getUserId)
                .collect(Collectors.toSet());
        List<User> userList = userService.bulkGet(userIds);
        // userId, user
        Map<Long, User> userMap = userList.stream().collect(Collectors.toMap(User::getId, Function.identity()));
        List<Long> postsUserImageIds = postRepoModels.stream()
                .flatMap(postRepoModel -> postRepoModel.getImages().stream())
                .distinct()
                .collect(Collectors.toList());
        List<UserImage> userImages = userImageService.getUserImages(postsUserImageIds);
        // userImage_Id , userImage
        Map<Long, UserImage> userImageMap = userImages.stream().collect(Collectors.toMap(UserImage::getId, Function.identity()));
        return postRepoModels.stream()
                .map(postRepoModel -> {
                            User user = userMap.get(postRepoModel.getUserId());
                            Set<Long> repoImagesIds = postRepoModel.getImages();
                            Set<UserImage> postImageMap = repoImagesIds.stream().map(userImageMap::get).collect(Collectors.toSet());
                    return makePost(postRepoModel, user, postImageMap, null);
                        }
                ).collect(Collectors.toList());
    }

    private Post makePost(PostRepoModel postRepoModel, User user, Set<UserImage> postUserImages, Map<Long, Comment> comments) {
        return new Post(
                postRepoModel.getId(),
                postRepoModel.getName(),
                postRepoModel.getRating(),
                postRepoModel.getViews(),
                postUserImages,
                postRepoModel.getDeviceType(),
                postRepoModel.getTags(),
                user,
                postRepoModel.getCreationDateTime(),
                postRepoModel.isVisible(),
                postRepoModel.getDescription(),
                comments
        );
    }

    private Comment makeComment(CommentRepoModel commentRepoModel, User user) {
        return new Comment(
                commentRepoModel.getId(),
                user,
                commentRepoModel.getReplyId(),
                commentRepoModel.getDevice(),
                commentRepoModel.getContent(),
                commentRepoModel.getCreationDate(),
                commentRepoModel.getRating()
        );
    }

}
