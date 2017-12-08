package com.turbo.service;

import com.turbo.config.RabbitConfig;
import com.turbo.model.statistic.ActionType;
import com.turbo.model.statistic.ReindexAction;
import lombok.extern.java.Log;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.*;

@Service
@Log
public class StatisticReindexService {

    private final Map<String, ReindexAction> updateActionMap = new ConcurrentHashMap<>();
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private final StatisticActionService statisticActionService;

    public StatisticReindexService(
            @Value("${statistic.initial-reindex-delay}") final Integer initReindexDelay,
            @Value("${statistic.reindex-delay}") final Integer reindexDelay,
            StatisticActionService statisticActionService
    ) {
        this.statisticActionService = statisticActionService;
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
        updateActionMap.keySet()
                .forEach(key -> {
                            final ReindexAction action = updateActionMap.remove(key);
                            try {
                                //main action for update
                                reindexRoute(action);
                            } catch (Exception e) {
                                putUpdateActionToMap(action);
                            }
                        }
                );
    }

    private void reindexRoute(final ReindexAction action) {
        switch (action.getType()) {
            case ActionType.COMMENT_CONTENT : {
                statisticActionService.updateCommentContent(action);
            }
            case ActionType.DELETE_COMMENT : {
                statisticActionService.deleteComment(action.getId());
            }
            case ActionType.DELETE_POST : {
                statisticActionService.deleteComment(action.getId());
            }
            case ActionType.DELETE_IMAGE : {
                statisticActionService.deleteComment(action.getId());
            }
            default: {
                throw new RuntimeException("Cannot find update type!");
            }
        }
    }
}
