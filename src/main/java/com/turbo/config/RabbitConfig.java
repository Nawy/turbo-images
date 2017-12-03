package com.turbo.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.core.Queue;

@Configuration
public class RabbitConfig {

    public static final String UPDATES_QUEUE_NAME = "images_updates";
    public static final String UPDATES_EXCHANGE_NAME = UPDATES_QUEUE_NAME;

    @Bean
    Queue imagesUpdatesQueue() {
        return new Queue(UPDATES_QUEUE_NAME, false);
    }

    @Bean
    FanoutExchange imagesUpdatesExchange() {
        return new FanoutExchange(UPDATES_EXCHANGE_NAME);
    }

    @Bean
    Binding binding(Queue queue, FanoutExchange fanoutExchange) {
        return BindingBuilder.bind(queue).to(fanoutExchange);
    }
}
