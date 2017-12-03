package com.turbo.service;

import com.turbo.config.RabbitConfig;
import com.turbo.model.statistic.UpdateAction;
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

    private final Map<String, List<UpdateAction>> updateActionMap = new ConcurrentHashMap<>();
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public StatisticReindexService(
            @Value("${statistic.initial-reindex-delay}") final Integer initReindexDelay,
            @Value("${statistic.reindex-delay}") final Integer reindexDelay
    ) {
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
    private void updatesListener(final UpdateAction message) {
        putUpdateActionToMap(message);
    }

    private void putUpdateActionToMap(final UpdateAction message) {
        updateActionMap.merge(
                getUpdateId(message),
                Collections.singletonList(message),
                this::mergeUpdateActions
        );
    }

    private String getUpdateId(final UpdateAction action) {
        return action.getType().toString() + String.valueOf(action.getId());
    }

    private List<UpdateAction> mergeUpdateActions(
            final List<UpdateAction> newValue,
            final List<UpdateAction> currentValue
    ) {
        currentValue.addAll(newValue);
        return currentValue;
    }

    private void reindexStatistic() {
        updateActionMap.keySet()
                .forEach(key -> {
                            final List<UpdateAction> actions = updateActionMap.remove(key);
                            // counting result views or rating
                            final long resultValue = actions.stream().mapToLong(UpdateAction::getId).sum();
                            final UpdateAction firstElement = actions.get(0);
                            final UpdateAction resultAction =
                                    new UpdateAction(
                                            firstElement.getType(),
                                            firstElement.getValue(),
                                            resultValue
                                    );

                            try {
                                //main action for update
                                reindexRoute(resultAction);
                            } catch (Exception e) {
                                putUpdateActionToMap(resultAction);
                            }
                        }
                );
    }

    private void reindexRoute(final UpdateAction action) {
        switch (action.getType()) {
            case RATING: {
                break;
            }
            case VIEW: {
                break;
            }
            default: {
                throw new RuntimeException("Cannot find update type!");
            }
        }
    }
}
