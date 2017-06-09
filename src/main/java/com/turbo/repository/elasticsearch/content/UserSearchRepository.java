package com.turbo.repository.elasticsearch.content;

import com.turbo.config.ElasticsearchConfig;
import com.turbo.model.Nullable;
import com.turbo.model.User;
import com.turbo.model.exception.InternalServerErrorHttpException;
import com.turbo.model.search.content.UserSearchEntity;
import com.turbo.model.search.field.UserField;
import com.turbo.repository.elasticsearch.AbstractSearchRepository;
import com.turbo.repository.elasticsearch.ElasticId;
import com.turbo.repository.elasticsearch.helper.SearchOrder;
import com.turbo.repository.util.ElasticUtils;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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
                        config.getSearchUserIndexName(),
                        config.getSearchUserTypeName()
                )
                .setSource(ElasticUtils.writeAsJsonBytes(entity), XContentType.JSON)
                .get();
    }

    public void updateUser(final User user) {
        Objects.requireNonNull(user.getId(), "for update user you need have id");

        final String elasticId = getUserElasticId(user.getId());
        elasticClient.prepareUpdate(
                config.getSearchUserIndexName(),
                config.getSearchUserTypeName(),
                elasticId
        ).setDoc(
                ElasticUtils.writeAsJsonBytes(new UserSearchEntity(user)),
                XContentType.JSON
        ).setRetryOnConflict(5).get();
    }

    public String getUserElasticId(final Long id) {
        final SearchResponse response = searchUniqueByField(
                config.getSearchUserIndexName(),
                config.getSearchUserTypeName(),
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
                        config.getSearchUserIndexName(),
                        config.getSearchUserTypeName(),
                        UserField.ID.getFieldName(),
                        id
                ),
                UserSearchEntity.class
        );
    }

    /**
     * Find to elasticsearch by terms
     */
    public Long getUserByName(
            final String name
    ) {
        SearchResponse response = getByField(
                config.getSearchUserIndexName(),
                config.getSearchUserTypeName(),
                UserField.NAME.getFieldName(),
                name,
                1,
                null,
                null
        );
        if(response.getHits().getTotalHits() > 0) {
            return ElasticUtils.parseUniqueSearchResponse(response, ElasticId.class).getId();
        }
        else {
            return null;
        }
    }

    /**
     * Find to elasticsearch by terms
     */
    public Long getUserByEmail(
            final String email
    ) {
        SearchResponse response = getByField(
                config.getSearchUserIndexName(),
                config.getSearchUserTypeName(),
                UserField.EMAIL.getFieldName(),
                email,
                1,
                null,
                null
        );

        if(response.getHits().getTotalHits() > 0) {
            return ElasticUtils.parseUniqueSearchResponse(response, ElasticId.class).getId();
        }
        else {
            return null;
        }
    }

    /**
     * Find to elasticsearch with variants
     */
    public List<Long> findUserByName(
            final String name,
            final int page,
            @Nullable final UserField userField,
            @Nullable final SearchOrder searchOrder
    ) {
        SearchResponse response = searchByField(
                config.getSearchUserIndexName(),
                config.getSearchUserTypeName(),
                UserField.NAME.getFieldName(),
                name,
                page,
                Objects.nonNull(userField) ? userField.getFieldName() : null,
                searchOrder
        );

        return ElasticUtils.parseSearchResponse(response, ElasticId.class)
                .stream()
                .map(ElasticId::getId)
                .collect(Collectors.toList());
    }
}
