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

import java.util.List;
import java.util.Objects;

@Service
@Slf4j
@AllArgsConstructor
public class ReindexService {

    private final PostService postService;
    private final PostSearchRepository postSearchRepository;
    private final ReindexPostService postStatisticService;


    public void deletePost(final Long id) {
        //TODO
    }

    public void deleteImage(final Long id) {
        // TODO
    }

    public void updatePostContent(final ReindexPost action) {
        log.debug("REINDEX MESSAGE: {}", action);
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

        final Rating newRating = new Rating();
        action.getRatings().forEach(newRating::change);

        postSearchRepository.upsertPost(post);
        postStatisticService.updateStatisticPost(
                UpdatePostStatistic.builder()
                        .id(post.getId())
                        .name(post.getName())
                        .description(post.getDescription())
                        .rating(newRating.getRating())
                        .views(action.getViews())
                        .ups(newRating.getUps())
                        .downs(newRating.getDowns())
                        .build()
        );
        log.debug("REINDEX POST with id={}", post.getId());
    }
}
