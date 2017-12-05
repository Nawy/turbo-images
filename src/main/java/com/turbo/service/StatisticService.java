package com.turbo.service;

import com.turbo.model.statistic.ActionType;
import com.turbo.model.statistic.ReindexAction;
import com.turbo.model.statistic.ReindexPostRating;
import com.turbo.model.statistic.ReindexPostViews;
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
        rabbitTemplate.convertAndSend(new ReindexPostViews(id, Collections.singletonList(count)));
    }

    public void updatePostName(final String name, final long id) {

    }

    public void deletePost(final long id) {

    }

    public void updateImageName(final String name, final long id) {

    }

    public void deleteImage(final long id) {

    }
}
