package com.turbo.repository.elasticsearch;

import com.turbo.config.ElasticsearchConfig;
import com.turbo.model.Nullable;
import com.turbo.model.search.PostSearchEntity;
import com.turbo.repository.elasticsearch.field.PostField;
import com.turbo.repository.elasticsearch.helper.SearchOrder;
import com.turbo.repository.util.ElasticUtils;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

/**
 * Created by ermolaev on 5/15/17.
 */
@Repository
public class PostSearchRepository extends AbstractSearchRepository {

    private static DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Autowired
    public PostSearchRepository(ElasticsearchConfig config) {
        super(config.getElasticClient(), config);
    }

    /**
     * Add new post to search engine
     * @param post
     * @return
     */
    public void addPost(final PostSearchEntity post) {
        elasticClient
                .prepareIndex(
                        config.getPostIndexName(),
                        config.getPostTypeName()
                )
                .setSource(ElasticUtils.writeAsJsonBytes(post), XContentType.JSON)
                .get();
    }

    public void updatePost(final PostSearchEntity post) {
        Objects.requireNonNull(post.getId(), "for update post you need have id for update");
        final String elasticId = getPostElasticId(post.getId());
        elasticClient.prepareUpdate(
                config.getPostIndexName(),
                config.getPostTypeName(),
                elasticId
        ).setDoc(
                ElasticUtils.writeAsJsonBytes(post),
                XContentType.JSON
        ).setRetryOnConflict(5).get();
    }

    public String getPostElasticId(final Long id) {
        return ElasticUtils.parseElasticIdSearchResponse(
                searchUniqueByField(
                        config.getPostIndexName(),
                        config.getPostTypeName(),
                        PostField.ID.getFieldName(),
                        id
                )
        );
    }

    /**
     * Find post by id (not search id)
     * @param id
     * @return
     */
    public PostSearchEntity getPostById(final Long id) {
        return ElasticUtils.parseUniqueSearchResponse(
                searchUniqueByField(
                        config.getPostIndexName(),
                        config.getPostTypeName(),
                        PostField.ID.getFieldName(),
                        id
                ),
                PostSearchEntity.class
        );
    }

    public List<PostSearchEntity> getPostByAuthor(
            final String authorId,
            final int page,
            @Nullable final PostField postField,
            @Nullable final SearchOrder searchOrder
    ) {
        SearchResponse response = getByField(
                config.getPostIndexName(),
                config.getPostTypeName(),
                PostField.AUTHOR.getFieldName(),
                authorId,
                page,
                Objects.isNull(postField) ? null : postField.getFieldName(),
                searchOrder
        );

        return ElasticUtils.parseSearchResponse(response, PostSearchEntity.class);
    }

    /**
     * Find posts by name
     * Throws Exception if page size more than limit {@link ElasticsearchConfig#getMaxSizePostsPerPage()}}
     * @param name
     * @param page
     * @param postField
     * @param searchOrder
     * @return list of post
     */
    public List<PostSearchEntity> findPostByName(
            final String name,
            final int page,
            @Nullable final PostField postField,
            @Nullable final SearchOrder searchOrder
    ) {
        SearchResponse response = searchByField(
                config.getPostIndexName(),
                config.getPostTypeName(),
                PostField.NAME.getFieldName(),
                name,
                page,
                Objects.isNull(postField) ? null : postField.getFieldName(),
                searchOrder
        );

        return ElasticUtils.parseSearchResponse(response, PostSearchEntity.class);
    }

    /**
     * Find post by description
     * Throws Exception if page size more than limit {@link ElasticsearchConfig#getMaxSizePostsPerPage()}}
     * @param description
     * @param page
     * @param postField
     * @param searchOrder
     * @return list of post
     */
    public List<PostSearchEntity> findPostByDescription(
            final String description,
            final int page,
            @Nullable final PostField postField,
            @Nullable final SearchOrder searchOrder
    ) {
        final SearchResponse response = searchByField(
                config.getPostIndexName(),
                config.getPostTypeName(),
                PostField.DESCRIPTION.getFieldName(),
                description,
                page,
                Objects.isNull(postField) ? null : postField.getFieldName(),
                searchOrder
        );

        return ElasticUtils.parseSearchResponse(response, PostSearchEntity.class);
    }

    public List<PostSearchEntity> getPostsByDate(
            final LocalDate postDate,
            final int page,
            @Nullable final PostField postField,
            @Nullable final SearchOrder searchOrder
    ) {
        final SearchResponse response = searchByField(
                config.getPostIndexName(),
                config.getPostTypeName(),
                PostField.CREATION_DATE.getFieldName(),
                dateFormatter.format(postDate),
                page,
                Objects.isNull(postField) ? null : postField.getFieldName(),
                searchOrder
        );

        return ElasticUtils.parseSearchResponse(response, PostSearchEntity.class);
    }
}
