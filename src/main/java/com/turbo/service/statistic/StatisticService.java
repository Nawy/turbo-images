package com.turbo.service.statistic;

import com.turbo.config.RabbitConfig;
import com.turbo.model.statistic.*;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class StatisticService {

    private RabbitTemplate rabbitTemplate;

    public void postUpvote(final long id) {
        rabbitTemplate.convertAndSend(
                ReindexPost.changeRating(id, 1L)
        );
    }

    public void postDownvote(final long id) {
        rabbitTemplate.convertAndSend(
                ReindexPost.changeRating(id, -1L)
        );
    }

    public void postView(final long id, final long count) {
        rabbitTemplate.convertAndSend(
                ReindexPost.changeViews(id, count)
        );
    }

    public void updateCommentContent(final long id, final String content) {
        rabbitTemplate.convertAndSend(new ReindexCommentContent(id, content));
    }

    public void updatePostContent(final long id, final String name, final String description) {
        rabbitTemplate.convertAndSend(
                RabbitConfig.UPDATES_EXCHANGE_NAME,
                null,
                ReindexPost.changeContent(id, name, description)
        );
    }

    public void deletePost(final long id) {
        rabbitTemplate.convertAndSend(new ReindexDeletePost(id));
    }

    public void deleteImage(final long id) {
        rabbitTemplate.convertAndSend(new ReindexDeleteImage(id));
    }

    public void updateImageName(final String name, final long id) {

    }
}
