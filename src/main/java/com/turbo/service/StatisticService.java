package com.turbo.service;

import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class StatisticService {

    private RabbitTemplate rabbitTemplate;

    public void upvote(final long id) {

    }

    public void downvote(final long id) {

    }

    public void increaseViews(final long id, final long count) {

    }


}
