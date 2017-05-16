package com.turbo.repository;

import com.turbo.config.ElasticsearchConfig;
import org.elasticsearch.client.transport.TransportClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * Created by ermolaev on 5/15/17.
 */
@Repository
public class ElasticsearchRepository {

    private final TransportClient elasticClient;

    @Autowired
    public ElasticsearchRepository(ElasticsearchConfig config) {
        this.elasticClient = config.getElasticClient();
    }
}
