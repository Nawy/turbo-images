package com.turbo.repository.elasticsearch.content;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.turbo.config.ElasticsearchConfig;
import com.turbo.model.Nullable;
import com.turbo.model.Post;
import com.turbo.model.exception.InternalServerErrorHttpException;
import com.turbo.model.page.Page;
import com.turbo.model.search.content.PostContentEntity;
import com.turbo.repository.elasticsearch.AbstractSearchRepository;
import com.turbo.repository.elasticsearch.field.PostField;
import com.turbo.repository.elasticsearch.helper.SearchOrder;
import com.turbo.repository.util.ElasticUtils;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Created by ermolaev on 5/15/17.
 */
@Repository
public class PostContentRepository extends AbstractSearchRepository {

    @Autowired
    public PostContentRepository(ElasticsearchConfig config) {
        super(config.getElasticClient(), config);
    }

    /**
     * Add new post to search engine
     * @param post
     * @return
     */
    public void addPost(final Post post) {
        elasticClient
                .prepareIndex(
                        config.getPostIndexName(),
                        config.getPostTypeName()
                )
                .setSource(ElasticUtils.writeAsJsonBytes(new PostContentEntity(post)), XContentType.JSON)
                .get();
    }

    public void updatePost(final Post post) {
        Objects.requireNonNull(post.getId(), "for update post you need have id for update");
        final String elasticId = getPostElasticId(post.getId());
        elasticClient.prepareUpdate(
                config.getPostIndexName(),
                config.getPostTypeName(),
                elasticId
        ).setDoc(
                ElasticUtils.writeAsJsonBytes(new PostContentEntity(post)),
                XContentType.JSON
        ).setRetryOnConflict(5).get();
    }

    public String getPostElasticId(final Long id) {
        final SearchResponse response = searchUniqueByField(
                config.getPostIndexName(),
                config.getPostTypeName(),
                PostField.ID.getFieldName(),
                id
        );
        if(response.getHits().getTotalHits() <= 0) {
            throw new InternalServerErrorHttpException("Not found post by id=" + id);
        }
        return ElasticUtils.parseElasticIdSearchResponse(response);
    }

    /**
     * Find post by id (not search id)
     * @param id
     * @return
     */
    public PostContentEntity getPostById(final Long id) {
        return ElasticUtils.parseUniqueSearchResponse(
                searchUniqueByField(
                        config.getPostIndexName(),
                        config.getPostTypeName(),
                        PostField.ID.getFieldName(),
                        id
                ),
                PostContentEntity.class
        );
    }

    public List<Long> getPostByAuthor(
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

        return ElasticUtils.parseSearchResponse(response, ElasticId.class)
                .stream()
                .map(ElasticId::getId)
                .collect(Collectors.toList());
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
    public List<Long> findPostByName(
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

        return ElasticUtils.parseSearchResponse(response, ElasticId.class)
                .stream()
                .map(ElasticId::getId)
                .collect(Collectors.toList());
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
    public List<Long> findPostByDescription(
            final String description,
            final int page,
            @Nullable final PostField postField,
            @Nullable final SearchOrder searchOrder
    ) {

        final SearchResponse response = searchByField(
                config.getPostIndexName(),
                config.getPostTypeName(),
                PostField.DESCRIPTIONS.getFieldName(),
                description,
                page,
                Objects.isNull(postField) ? null : postField.getFieldName(),
                searchOrder
        );

        return ElasticUtils.parseSearchResponse(response, ElasticId.class)
                .stream()
                .map(ElasticId::getId)
                .collect(Collectors.toList());
    }

    public List<Long> findPostByTags(
            final List<String> tags,
            final int page,
            @Nullable final PostField postField,
            @Nullable final SearchOrder searchOrder
    ) {
        final Page pageObject = new Page(page);
        final SearchRequestBuilder request = elasticClient
                .prepareSearch(config.getPostIndexName())
                .setTypes(config.getPostTypeName())
                .setQuery(
                        QueryBuilders.termsQuery(
                                PostField.TAGS.getFieldName(),
                                tags.toArray()
                        )
                )
                .setFrom(pageObject.getOffset())
                .setSize(pageObject.getSize());

        if(Objects.nonNull(postField) && Objects.nonNull(searchOrder)) {
            request.addSort(
                    SortBuilders
                            .fieldSort(postField.getFieldName())
                            .order(
                                    searchOrder == SearchOrder.DESC ?
                                            SortOrder.DESC :
                                            SortOrder.ASC
                            )
            );
        }

        final SearchResponse response = request.get();

        return ElasticUtils.parseSearchResponse(response, ElasticId.class)
                .stream()
                .map(ElasticId::getId)
                .collect(Collectors.toList());
    }

    // static classes

    private static class ElasticId {
        private Long id;

        public ElasticId(@JsonProperty("id") Long id) {
            this.id = id;
        }

        @JsonProperty("id")
        public Long getId() {
            return id;
        }
    }
}
