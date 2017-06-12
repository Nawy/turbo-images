package com.turbo.service;

import com.turbo.model.Post;
import com.turbo.model.exception.InternalServerErrorHttpException;
import com.turbo.model.exception.NotFoundHttpException;
import com.turbo.model.page.Page;
import com.turbo.model.page.Paginator;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

/**
 * Created by rakhmetov on 09.05.17.
 */
@Service
public class PostService {

    private final PostSearchRepository postSearchRepository;
    private final PostStatRepository postStatRepository;
    private final PostRepository postRepository;

    @Autowired
    public PostService(
            PostSearchRepository postSearchRepository,
            PostRepository postRepository,
            PostStatRepository postStatRepository
    ) {
        this.postSearchRepository = postSearchRepository;
        this.postStatRepository = postStatRepository;
        this.postRepository = postRepository;
    }

    public Post save(Post post) {
        return post.getId() == null ?
                update(post) :
                addPost(post);
    }

    private Post addPost(Post post) {
        Post postWithId = postRepository.save(post);
        postSearchRepository.addPost(postWithId);
        return postWithId;
    }

    private Post update(Post post) {
        postSearchRepository.updatePost(post);
        return postRepository.save(post);
    }

    public Post getPostById(final long id) {
        return postRepository.get(id);
    }

    public List<Post> getMostViral(int page, SearchPeriod searchPeriod, SearchSort searchSort) {

        List<Long> resultIds = null;

        // NEWEST
        if(searchSort == SearchSort.NEWEST) {
            resultIds = postSearchRepository.getNewestPost(page);
        }

        // RATING or VIEWS for ALL_TIME from CONTENT
        if(searchPeriod == SearchPeriod.ALL_TIME && searchSort != SearchSort.NEWEST) {
            final PostField sortingField = getContentPostField(searchSort);

            resultIds = postSearchRepository.getSortedPost(page, sortingField, SearchOrder.DESC);
        }

        //RATING or VIEWS for specific time from STATISTICS
        if(searchSort != SearchSort.NEWEST && searchPeriod != SearchPeriod.ALL_TIME) {
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
            resultIds = postStatRepository.getPostStat(
                    null,
                    null,
                    new Page(page),
                    specificDate,
                    period,
                    sortingField,
                    SearchOrder.DESC
            );

        }

        // PROCESSING ALL OF REQUESTS
        Objects.requireNonNull(resultIds);
        if(resultIds.isEmpty()) {
            throw new NotFoundHttpException("Post not found!");
        }

        return postRepository.bulkGet(resultIds);
    }

    public List<Post> getUserPosts(int page, String username, SearchPattern searchPattern) {

        List<Long> resultIds;

        if(searchPattern.getSort() == SearchSort.NEWEST) {
            // ONLY NEWEST
            resultIds = postSearchRepository.getPostByAuthor(
                    username,
                    page,
                    searchPattern.getPeriod(),
                    PostField.CREATION_DATE,
                    SearchOrder.DESC
            );
        } else {
            // RATING/VIEWS in specific order
            final PostField sortingField = getContentPostField(searchPattern.getSort());

            resultIds = postSearchRepository.getPostByAuthor(
                    username,
                    page,
                    searchPattern.getPeriod(),
                    sortingField,
                    searchPattern.getOrder()
            );
        }

        return postRepository.bulkGet(resultIds);
    }

    public void delete(long id) {
        postRepository.delete(id);
    }

    private PostField getContentPostField(SearchSort searchSort) {
        switch (searchSort) {
            case RATING: return PostField.RATING;
            case VIEWS: return PostField.VIEWS;
            default:
                throw new InternalServerErrorHttpException(" unknown sorting field");
        }
    }

    private PostDiffStatField getStatPostField(SearchSort searchSort) {
        switch (searchSort) {
            case RATING: return PostDiffStatField.RATING;
            case VIEWS: return PostDiffStatField.VIEWS;
            default:
                throw new InternalServerErrorHttpException(" unknown sorting field in statistic");
        }
    }

}
