package com.turbo.repository;

import com.turbo.config.ElasticsearchConfig;
import com.turbo.model.Post;
import com.turbo.repository.util.ElasticUtils;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * Created by ermolaev on 5/15/17.
 */
@Repository
public class ElasticsearchRepository {

    private final TransportClient elasticClient;
    private final ElasticsearchConfig config;

    @Autowired
    public ElasticsearchRepository(ElasticsearchConfig config) {
        this.elasticClient = config.getElasticClient();
        this.config = config;
    }

    public void addPost(final Post post) {
        elasticClient
                .prepareIndex(config.getPostIndexName(), config.getPostTypeName())
                .setSource(ElasticUtils.writeAsJsonBytes(post), XContentType.JSON)
                .get();
    }
}
