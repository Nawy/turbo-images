package com.turbo.repository.elasticsearch;

import com.turbo.config.ElasticsearchConfig;
import com.turbo.model.page.Paginator;
import com.turbo.model.user.User;
import org.elasticsearch.client.transport.TransportClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * Created by ermolaev on 5/25/17.
 */
@Repository
public class UserElasticRepository {

    private final TransportClient elasticClient;
    private final ElasticsearchConfig config;

    @Autowired
    public UserElasticRepository(ElasticsearchConfig config) {
        this.elasticClient = config.getElasticClient();
        this.config = config;
    }

    public void addUser(final User user) {
    }

    public void updateUser(final User user) {
    }

    public User getUserByElasticId(final String elasticId) {
        return null;
    }

    public User getUserById(final String id) {
        return null;
    }

    public Paginator<User> findUserByName(final String name) {
        return null;
    }
}
