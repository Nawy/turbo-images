package com.turbo.service.statistic;

import com.turbo.model.Post;
import com.turbo.model.Rating;
import com.turbo.model.search.stat.UpdatePostStatistic;
import com.turbo.model.statistic.ReindexPost;
import com.turbo.repository.elasticsearch.content.PostSearchRepository;
import com.turbo.service.PostService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.stream.LongStream;

@Service
@Slf4j
@AllArgsConstructor
public class StatisticActionService {

    private final PostService postService;
    private final PostSearchRepository postSearchRepository;
    private final PostStatisticService postStatisticService;


    public void deletePost(final Long id) {
        //TODO
    }

    public void deleteImage(final Long id) {
        // TODO
    }

    public void updatePostContent(final ReindexPost action) {
        final Post post = postService.getPostById(action.getId());

        if (Objects.isNull(post)) {
            return;
        }

        if (Objects.nonNull(action.getName())) {
            post.setName(action.getName());
        }

        if (Objects.nonNull(action.getDescription())) {
            post.setDescription(action.getDescription());
        }

        long ratingChangeAmount = action.getRatings().stream().mapToLong(rating -> rating).sum();
        post.setViews(post.getViews() + action.getViews());
        Rating rating = post.getRating();
        rating.change(ratingChangeAmount);

        postSearchRepository.upsertPost(post);
        postStatisticService.updateStaticPost(
                UpdatePostStatistic.builder()
                        .id(post.getId())
                        .name(post.getName())
                        .description(post.getDescription())
                        .rating(rating.getRating())
                        .views(action.getViews())
                        .ups(rating.getUps())
                        .downs(rating.getDowns())
                        .build()
        );
        log.info("REINDEX POST with id={}", post.getId());
    }
}
