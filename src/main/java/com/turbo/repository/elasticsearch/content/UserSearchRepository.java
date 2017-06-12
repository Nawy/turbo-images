package com.turbo.repository.elasticsearch.content;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.turbo.config.ElasticsearchConfig;
import com.turbo.model.Nullable;
import com.turbo.model.User;
import com.turbo.model.exception.InternalServerErrorHttpException;
import com.turbo.model.search.content.UserSearchEntity;
import com.turbo.model.search.field.UserField;
import com.turbo.repository.elasticsearch.AbstractSearchRepository;
import com.turbo.model.search.SearchOrder;
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
        Objects.requireNonNull(user.getName(), "for update user you need have name");

        final String elasticId = getUserElasticId(user.getName());
        elasticClient.prepareUpdate(
                config.getSearchUserIndexName(),
                config.getSearchUserTypeName(),
                elasticId
        ).setDoc(
                ElasticUtils.writeAsJsonBytes(new UserSearchEntity(user)),
                XContentType.JSON
        ).setRetryOnConflict(5).get();
    }

    public String getUserElasticId(final String name) {
        final SearchResponse response = searchUniqueByField(
                config.getSearchUserIndexName(),
                config.getSearchUserTypeName(),
                UserField.NAME.getFieldName(),
                name
        );
        if(response.getHits().getTotalHits() <= 0) {
            throw new InternalServerErrorHttpException("Not found user by name=" + name);
        }
        return ElasticUtils.parseElasticIdSearchResponse(response);
    }

    /**
     * Find to elasticsearch by terms
     */
    public String getUserByEmail(
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
            return ElasticUtils.parseUniqueSearchResponse(response, UserNameDto.class).getName();
        }
        else {
            return null;
        }
    }

    /**
     * Find to user by username with variants
     */
    public List<String> findUserByName(
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

        return ElasticUtils.parseSearchResponse(response, UserNameDto.class)
                .stream()
                .map(UserNameDto::getName)
                .collect(Collectors.toList());
    }

    private static class UserNameDto {

        private String name;
        public UserNameDto(@JsonProperty(value = "name", required = true) String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
}
