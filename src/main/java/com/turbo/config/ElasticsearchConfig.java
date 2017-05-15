package com.turbo.config;

import org.elasticsearch.client.transport.TransportClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by ermolaev on 5/15/17.
 */
@Configuration
public class ElasticsearchConfig {



    @Bean
    public TransportClient transportClient() {
        return null
    }
}
