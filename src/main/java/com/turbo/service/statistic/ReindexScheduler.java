package com.turbo.service.statistic;

import com.turbo.config.RabbitConfig;
import com.turbo.model.statistic.ActionType;
import com.turbo.model.statistic.ReindexAction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.*;

@Service
@Slf4j
public class ReindexScheduler {

    private final Map<String, ReindexAction> updateActionMap = new ConcurrentHashMap<>();
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private final ReindexService reindexService;

    public ReindexScheduler(
            @Value("${statistic.initial-reindex-delay}") final Integer initReindexDelay,
            @Value("${statistic.reindex-delay}") final Integer reindexDelay,
            ReindexService reindexService
    ) {
        this.reindexService = reindexService;
        Objects.requireNonNull(initReindexDelay);
        Objects.requireNonNull(reindexDelay);

        scheduler.scheduleWithFixedDelay(
                this::reindexStatistic,
                initReindexDelay,
                reindexDelay,
                TimeUnit.SECONDS
        );
    }

    @RabbitListener(queues = RabbitConfig.UPDATES_QUEUE_NAME)
    private void updatesListener(final ReindexAction message) {
        log.info("MESSAGE :" +  message.toString());
        putUpdateActionToMap(message);
    }

    private void putUpdateActionToMap(final ReindexAction message) {
        updateActionMap.merge(
                getUpdateId(message),
                message,
                this::mergeUpdateActions
        );
    }

    private String getUpdateId(final ReindexAction action) {
        return action.getType() + String.valueOf(action.getId());
    }

    private ReindexAction mergeUpdateActions(
            final ReindexAction oldValue,
            final ReindexAction newValue
    ) {
        return oldValue.merge(newValue);
    }

    private void reindexStatistic() {
        final Set<String> values = new HashSet<>(updateActionMap.keySet());

        for(String key : values) {
            final ReindexAction action = updateActionMap.remove(key);
            try {
                //main action for update
                reindexRoute(action);
            } catch (Exception e) {
                putUpdateActionToMap(action);
                log.error("Cannot save: ", e);
                throw new RuntimeException( e);
            }
        }
    }

    private void reindexRoute(final ReindexAction action) {
        switch (action.getType()) {
            case ActionType.DELETE_POST : {
                reindexService.deletePost(action.getId());
                break;
            }
            case ActionType.DELETE_IMAGE : {
                reindexService.deleteImage(action.getId());
                break;
            }
            case ActionType.UPDATE_POST: {
                reindexService.updatePostContent(action.getOriginalValue());
                break;
            }
            default: {
                throw new RuntimeException("Cannot find update type!");
            }
        }
    }
}
