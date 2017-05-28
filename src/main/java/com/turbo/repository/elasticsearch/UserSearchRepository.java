package com.turbo.repository.elasticsearch;

import com.turbo.config.ElasticsearchConfig;
import com.turbo.model.Post;
import com.turbo.model.page.Page;
import com.turbo.model.page.Paginator;
import com.turbo.model.search.entity.PostSearchEntity;
import com.turbo.model.search.entity.UserSearchEntity;
import com.turbo.model.user.User;
import com.turbo.repository.elasticsearch.field.PostField;
import com.turbo.repository.elasticsearch.field.UserField;
import com.turbo.repository.util.ElasticUtils;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;

/**
 * Created by ermolaev on 5/25/17.
 */
@Repository
public class UserSearchRepository extends AbstractSearchRepository {

    @Autowired
    public UserSearchRepository(ElasticsearchConfig config) {
        super(config.getElasticClient(), config);
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
        Objects.requireNonNull(user.getSearchId(), "for update user you need have search id");
        final UserSearchEntity entity = new UserSearchEntity(user);

        elasticClient
                .prepareUpdate(
                        config.getUserIndexName(),
                        ElasticUtils.getElasticTypeWithDate(config.getUserTypeName(), user.getCreateDate()),
                        user.getSearchId()
                )
                .setDoc(ElasticUtils.writeAsJsonBytes(entity), XContentType.JSON)
                .get();
    }

    public User getUserByElasticId(final String searchId) {
        GetResponse response = elasticClient
                .prepareGet(
                        config.getUserIndexName(),
                        ElasticUtils.getElasticTypeWithoutDate(config.getUserTypeName()),
                        searchId
                ).get();

        return ElasticUtils.parseGetResponse(response, UserSearchEntity.class);
    }

    public User getUserById(final String id) {
        SearchResponse response = elasticClient
                .prepareSearch(config.getUserIndexName())
                .setTypes(ElasticUtils.getElasticTypeWithoutDate(config.getUserTypeName()))
                .setQuery(QueryBuilders.termQuery(UserField.ID.getFieldName(), id))
                .get();

        List<User> results = ElasticUtils.parseSearchResponse(response, UserSearchEntity.class);
        return CollectionUtils.isEmpty(results) ? null : results.get(0);
    }

    public Paginator<User> findUserByName(final String name, final Page page) {
        SearchResponse response = searchByField(
                UserField.NAME.getFieldName(),
                name,
                page,
                null,
                null
        );
        return new Paginator<>(
                page,
                ElasticUtils.parseSearchResponse(response, UserSearchEntity.class)
        );
    }
}
