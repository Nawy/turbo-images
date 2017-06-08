package com.turbo.repository.elasticsearch.content;

import com.turbo.config.ElasticsearchConfig;
import com.turbo.model.Nullable;
import com.turbo.model.exception.InternalServerErrorHttpException;
import com.turbo.model.page.Paginator;
import com.turbo.model.search.content.UserSearchEntity;
import com.turbo.model.User;
import com.turbo.repository.elasticsearch.AbstractSearchRepository;
import com.turbo.repository.elasticsearch.field.UserField;
import com.turbo.repository.elasticsearch.helper.SearchOrder;
import com.turbo.repository.util.ElasticUtils;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Objects;

/**
 * Created by ermolaev on 5/25/17.
 */
@Repository
public class UserContentRepository extends AbstractSearchRepository {

    @Autowired
    public UserContentRepository(ElasticsearchConfig config) {
        super(config.getElasticClient(), config);
    }

    public void addUser(final User user) {
        final UserSearchEntity entity = new UserSearchEntity(user);

        elasticClient
                .prepareIndex(
                        config.getUserIndexName(),
                        config.getUserTypeName()
                )
                .setSource(ElasticUtils.writeAsJsonBytes(entity), XContentType.JSON)
                .get();
    }

    public void updateUser(final User user) {
        Objects.requireNonNull(user.getId(), "for update user you need have id");

        final String elasticId = getUserElasticId(user.getId());
        elasticClient.prepareUpdate(
                config.getUserIndexName(),
                config.getUserTypeName(),
                elasticId
        ).setDoc(
                ElasticUtils.writeAsJsonBytes(new UserSearchEntity(user)),
                XContentType.JSON
        ).setRetryOnConflict(5).get();
    }

    public String getUserElasticId(final Long id) {
        final SearchResponse response = searchUniqueByField(
                config.getUserIndexName(),
                config.getUserTypeName(),
                UserField.ID.getFieldName(),
                id
        );
        if(response.getHits().getTotalHits() <= 0) {
            throw new InternalServerErrorHttpException("Not found user by id=" + id);
        }
        return ElasticUtils.parseElasticIdSearchResponse(response);
    }

    public UserSearchEntity getUserById(final String id) {
        return ElasticUtils.parseUniqueSearchResponse(
                searchUniqueByField(
                        config.getUserIndexName(),
                        config.getUserTypeName(),
                        UserField.ID.getFieldName(),
                        id
                ),
                UserSearchEntity.class
        );
    }

    public Long getUserByName(
            final String name
    ) {
        SearchResponse response = getByField(
                config.getUserIndexName(),
                config.getUserTypeName(),
                UserField.NAME.getFieldName(),
                name,
                1,
                null,
                null
        );
        return ElasticUtils.parseUniqueSearchResponse(response, UserSearchEntity.class).getId();
    }

    public UserSearchEntity getUserByEmail(
            final String email
    ) {
        SearchResponse response = getByField(
                config.getUserIndexName(),
                config.getUserTypeName(),
                UserField.EMAIL.getFieldName(),
                email,
                1,
                null,
                null
        );
        return ElasticUtils.parseUniqueSearchResponse(response, UserSearchEntity.class);
    }

    public Paginator<UserSearchEntity> findUserByName(
            final String name,
            final int page,
            @Nullable final UserField userField,
            @Nullable final SearchOrder searchOrder
    ) {
        SearchResponse response = searchByField(
                config.getUserIndexName(),
                config.getUserTypeName(),
                UserField.NAME.getFieldName(),
                name,
                page,
                Objects.nonNull(userField) ? userField.getFieldName() : null,
                searchOrder
        );
        return new Paginator<>(
                page,
                ElasticUtils.parseSearchResponse(response, UserSearchEntity.class)
        );
    }
}
