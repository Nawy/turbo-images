package com.turbo.service.statistic;

import com.turbo.config.RabbitConfig;
import com.turbo.model.statistic.*;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@AllArgsConstructor
public class StatisticService {

    private RabbitTemplate rabbitTemplate;

    public void updatePostRaiting(
            final long id,
            final long rating,
            final long views
    ) {
        rabbitTemplate.convertAndSend(
                ReindexPost.changeRating(id, views, rating)
        );
    }

    public void updatePostContent(
            final long id,
            final String name,
            final String description,
            final Set<String> tags
    ) {
        rabbitTemplate.convertAndSend(
                RabbitConfig.UPDATES_EXCHANGE_NAME,
                null,
                ReindexPost.changeContent(id, name, description, tags)
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
