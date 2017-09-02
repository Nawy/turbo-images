package com.turbo.service;

import com.turbo.model.Post;
import com.turbo.model.User;
import com.turbo.model.UserImage;
import com.turbo.model.aerospike.PostRepoModel;
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
import com.turbo.repository.elasticsearch.stat.PostStatRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by rakhmetov on 09.05.17.
 */
@Service
public class PostService {

    private final PostSearchRepository postSearchRepository;
    private final PostStatRepository postStatRepository;
    private final PostRepository postRepository;
    private final UserService userService;
    private final UserImageService userImageService;

    public PostService(PostSearchRepository postSearchRepository, PostStatRepository postStatRepository, PostRepository postRepository, UserService userService, UserImageService userImageService) {
        this.postSearchRepository = postSearchRepository;
        this.postStatRepository = postStatRepository;
        this.postRepository = postRepository;
        this.userService = userService;
        this.userImageService = userImageService;
    }

    public Post save(Post post) {
        return post.getId() == null ?
                update(post) :
                addPost(post);
    }

    private Post addPost(Post post) {
        PostRepoModel postWithId = postRepository.save(new PostRepoModel(post));
        postSearchRepository.addPost(postWithId);
        post.setId(postWithId.getId());
        return post;
    }

    private Post update(Post post) {
        update(new PostRepoModel(post));
        return post;
    }

    private void update(PostRepoModel post) {
        PostRepoModel updatedPost = postRepository.save(post);
        //FIXME add to search tags?
        //FIXME may be long update what to do with that?
        postSearchRepository.updatePost(updatedPost);
    }

    public Post updatePostName(long postId, String name) {
        PostRepoModel postRepoModel = postRepository.get(postId);
        PostRepoModel updatedRepoModel = new PostRepoModel(
                postRepoModel.getId(),
                name,
                postRepoModel.getUps(),
                postRepoModel.getDowns(),
                postRepoModel.getRating(),
                postRepoModel.getViews(),
                postRepoModel.getImages(),
                postRepoModel.getDeviceType(),
                postRepoModel.getTags(),
                postRepoModel.getUserId(),
                postRepoModel.getCreationDateTime(),
                postRepoModel.isVisible(),
                postRepoModel.getDescription()
        );
        update(updatedRepoModel);
        return makePost(updatedRepoModel);
    }

    public Post updatePostDescription(long postId, String description) {
        PostRepoModel postRepoModel = postRepository.get(postId);
        PostRepoModel updatedRepoModel = new PostRepoModel(
                postRepoModel.getId(),
                postRepoModel.getName(),
                postRepoModel.getUps(),
                postRepoModel.getDowns(),
                postRepoModel.getRating(),
                postRepoModel.getViews(),
                postRepoModel.getImages(),
                postRepoModel.getDeviceType(),
                postRepoModel.getTags(),
                postRepoModel.getUserId(),
                postRepoModel.getCreationDateTime(),
                postRepoModel.isVisible(),
                description
        );
        update(updatedRepoModel);
        return makePost(updatedRepoModel);
    }

    public Post addPostTag(long postId, String tag) {
        PostRepoModel postRepoModel = postRepository.get(postId);
        Set<String> updatedTags = new HashSet<>(postRepoModel.getTags());
        updatedTags.add(tag);
        PostRepoModel updatedRepoModel = new PostRepoModel(
                postRepoModel.getId(),
                postRepoModel.getName(),
                postRepoModel.getUps(),
                postRepoModel.getDowns(),
                postRepoModel.getRating(),
                postRepoModel.getViews(),
                postRepoModel.getImages(),
                postRepoModel.getDeviceType(),
                updatedTags,
                postRepoModel.getUserId(),
                postRepoModel.getCreationDateTime(),
                postRepoModel.isVisible(),
                postRepoModel.getDescription()
        );
        update(updatedRepoModel);
        return makePost(updatedRepoModel);
    }

    public Post removePostTag(long postId, String tag) {
        PostRepoModel postRepoModel = postRepository.get(postId);
        Set<String> updatedTags = new HashSet<>(postRepoModel.getTags());
        updatedTags.remove(tag);
        PostRepoModel updatedRepoModel = new PostRepoModel(
                postRepoModel.getId(),
                postRepoModel.getName(),
                postRepoModel.getUps(),
                postRepoModel.getDowns(),
                postRepoModel.getRating(),
                postRepoModel.getViews(),
                postRepoModel.getImages(),
                postRepoModel.getDeviceType(),
                updatedTags,
                postRepoModel.getUserId(),
                postRepoModel.getCreationDateTime(),
                postRepoModel.isVisible(),
                postRepoModel.getDescription()
        );
        update(updatedRepoModel);
        return makePost(updatedRepoModel);
    }

    public Post getPostById(final long id) {
        PostRepoModel postRepoModel = postRepository.get(id);
        return makePost(postRepoModel);
    }

    private Post makePost(PostRepoModel postRepoModel) {
        User user = userService.get(postRepoModel.getUserId());
        Map<Long, String> repoPostImageMap = postRepoModel.getImages();
        List<UserImage> userImages = userImageService.getUserImages(postRepoModel.getImages().keySet());
        Map<UserImage, String> postUserImages = userImages.stream().collect(Collectors.toMap(Function.identity(), userImage -> repoPostImageMap.get(userImage.getId())));
        return makePost(postRepoModel, user, postUserImages);
    }

    public List<Post> getMostViral(int page, SearchPeriod searchPeriod, SearchSort searchSort) {

        List<Long> resultIds = null;

        // NEWEST
        if (searchSort == SearchSort.NEWEST) {
            resultIds = postSearchRepository.getNewestPost(page);
        }

        // RATING or VIEWS for ALL_TIME from CONTENT
        if (searchPeriod == SearchPeriod.ALL_TIME && searchSort != SearchSort.NEWEST) {
            final PostField sortingField = getContentPostField(searchSort);

            resultIds = postSearchRepository.getSortedPost(page, sortingField, SearchOrder.DESC);
        }

        //RATING or VIEWS for specific time from STATISTICS
        if (searchSort != SearchSort.NEWEST && searchPeriod != SearchPeriod.ALL_TIME) {
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
            resultIds = postStatRepository
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

        return bulkGetPosts(resultIds);
    }

    public List<Post> getUserPosts(int page, long userId, SearchPattern searchPattern) {

        List<Long> resultIds;

        if (searchPattern.getSort() == SearchSort.NEWEST) {
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

        return bulkGetPosts(resultIds, userId);
    }

    public void delete(long id) {
        postRepository.delete(id);
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

    private List<Post> bulkGetPosts(List<Long> postIds, long userId) {
        User user = userService.get(userId);
        List<PostRepoModel> postRepoModels = postRepository.bulkGet(postIds);
        List<Long> postsUserImageIds = postRepoModels.stream()
                .flatMap(postRepoModel -> postRepoModel.getImages().keySet().stream())
                .collect(Collectors.toList());
        List<UserImage> userImages = userImageService.getUserImages(postsUserImageIds);
        Map<Long, UserImage> userImageMap = userImages.stream().collect(Collectors.toMap(UserImage::getId, Function.identity()));
        return postRepoModels.stream().map(postRepoModel -> {

            Map<Long, String> repoImages = postRepoModel.getImages();
            Map<UserImage, String> postImageMap = repoImages.entrySet().stream().collect(Collectors.toMap(entry -> userImageMap.get(entry.getKey()), Map.Entry::getValue));
            return makePost(postRepoModel, user, postImageMap);

        })
                .collect(Collectors.toList());
    }

    private List<Post> bulkGetPosts(List<Long> postIds) {
        List<PostRepoModel> postRepoModels = postRepository.bulkGet(postIds);
        List<Long> postsUserImageIds = postRepoModels.stream()
                .flatMap(postRepoModel -> postRepoModel.getImages().keySet().stream())
                .collect(Collectors.toList());
        List<UserImage> userImages = userImageService.getUserImages(postsUserImageIds);
        Map<Long, UserImage> userImageMap = userImages.stream().collect(Collectors.toMap(UserImage::getId, Function.identity()));
        return postRepoModels.stream().map(postRepoModel -> {
            User user = userService.get(postRepoModel.getUserId());
            Map<Long, String> repoImages = postRepoModel.getImages();
            Map<UserImage, String> postImageMap = repoImages.entrySet().stream().collect(Collectors.toMap(entry -> userImageMap.get(entry.getKey()), Map.Entry::getValue));
            return makePost(postRepoModel, user, postImageMap);

        })
                .collect(Collectors.toList());
    }

    private Post makePost(PostRepoModel postRepoModel, User user, Map<UserImage, String> postUserImages) {
        return new Post(
                postRepoModel.getId(),
                postRepoModel.getName(),
                postRepoModel.getUps(),
                postRepoModel.getDowns(),
                postRepoModel.getRating(),
                postRepoModel.getViews(),
                postUserImages,
                postRepoModel.getDeviceType(),
                postRepoModel.getTags(),
                user,
                postRepoModel.getCreationDateTime(),
                postRepoModel.isVisible(),
                postRepoModel.getDescription()
        );
    }

}
