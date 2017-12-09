package com.turbo.service;

import com.turbo.model.statistic.*;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@AllArgsConstructor
public class StatisticService {

    private RabbitTemplate rabbitTemplate;

    public void upvotePost(final long id) {
        rabbitTemplate.convertAndSend(new ReindexPostRating(id, Collections.singletonList(1L)));
    }

    public void downvotePost(final long id) {
        rabbitTemplate.convertAndSend(new ReindexPostRating(id, Collections.singletonList(-1L)));
    }

    public void increaseViews(final long id, final long count) {
        rabbitTemplate.convertAndSend(new ReindexPostViews(id, count));
    }

    public void updateCommentContent(final long id, final String content) {
        rabbitTemplate.convertAndSend(new ReindexCommentContent(id, content));
    }

    public void updatePostContent(final long id, final String name, final String description) {
        rabbitTemplate.convertAndSend(new ReindexPostContent(id, name, description));
    }

    public void deletePost(final long id) {
        rabbitTemplate.convertAndSend(new ReindexDeletePost(id));
    }

    public void deleteComment(final long id) {
        rabbitTemplate.convertAndSend(new ReindexDeleteComment(id));
    }

    public void deleteImage(final long id) {
        rabbitTemplate.convertAndSend(new ReindexDeleteImage(id));
    }

    public void updateImageName(final String name, final long id) {

    }
}
