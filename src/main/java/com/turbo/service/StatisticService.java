package com.turbo.service;

import com.turbo.model.statistic.ActionType;
import com.turbo.model.statistic.UpdateAction;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class StatisticService {

    private RabbitTemplate rabbitTemplate;

    public void upvote(final long id) {
        rabbitTemplate.convertAndSend(new UpdateAction(ActionType.RATING, id, 1));
    }

    public void downvote(final long id) {
        rabbitTemplate.convertAndSend(new UpdateAction(ActionType.RATING, id, -1));
    }

    public void increaseViews(final long id, final long count) {
        rabbitTemplate.convertAndSend(new UpdateAction(ActionType.VIEW, id, 1));
    }


}
