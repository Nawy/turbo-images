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

    public void updatePostName(final long id, final String name) {
        rabbitTemplate.convertAndSend(new ReindexPostContent(id, name, null));
    }

    public void updatePostDescription(final long id, final String description) {
        rabbitTemplate.convertAndSend(new ReindexPostContent(id, null, description));
    }

    public void deletePost(final long id) {

    }

    public void updateImageName(final String name, final long id) {

    }

    public void deleteImage(final long id) {

    }
}
