package com.turbo.repository.elasticsearch;

import com.turbo.config.ElasticsearchConfig;
import com.turbo.model.page.Paginator;
import com.turbo.model.search.entity.UserSearchEntity;
import com.turbo.model.user.User;
import com.turbo.repository.util.ElasticUtils;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * Created by ermolaev on 5/25/17.
 */
@Repository
public class UserSearchRepository {

    private final TransportClient elasticClient;
    private final ElasticsearchConfig config;

    @Autowired
    public UserSearchRepository(ElasticsearchConfig config) {
        this.elasticClient = config.getElasticClient();
        this.config = config;
    }

    public void addUser(final User user) {
        final UserSearchEntity entity = new UserSearchEntity(user);

        elasticClient
                .prepareIndex(
                        config.getUserIndexName(),
                        ElasticUtils.getElasticTypeWithCurrentDate(config.getUserTypeName())
                )
                .setSource(ElasticUtils.writeAsJsonBytes(entity), XContentType.JSON)
                .get();
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
