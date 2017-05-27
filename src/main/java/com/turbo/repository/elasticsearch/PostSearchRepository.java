package com.turbo.repository.elasticsearch;

import com.turbo.config.ElasticsearchConfig;
import com.turbo.model.Nullable;
import com.turbo.model.Post;
import com.turbo.model.exception.data.PageSizeLimitException;
import com.turbo.model.page.Page;
import com.turbo.model.search.entity.PostSearchEntity;
import com.turbo.repository.elasticsearch.field.PostField;
import com.turbo.repository.elasticsearch.helper.SearchOrder;
import com.turbo.repository.util.ElasticUtils;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

/**
 * Created by ermolaev on 5/15/17.
 */
@Repository
public class PostSearchRepository {

    private final TransportClient elasticClient;
    private final ElasticsearchConfig config;

    @Autowired
    public PostSearchRepository(ElasticsearchConfig config) {
        this.elasticClient = config.getElasticClient();
        this.config = config;
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
                        ElasticUtils.getElasticTypeWithCurrentDate(config.getPostTypeName())
                )
                .setSource(ElasticUtils.writeAsJsonBytes(post), XContentType.JSON)
                .get();
    }

    /**
     * Update post by search_id
     * ATTENTION! may take a log time and cause optimistic locking exception
     * @param post
     * @return
     */
    public void updatePost(final Post post) {
        Objects.requireNonNull(post.getSearchId(), "for update you need have search id");
        elasticClient
                .prepareUpdate(
                        config.getPostIndexName(),
                        ElasticUtils.getElasticTypeWithoutDate(config.getPostIndexName()),
                        post.getSearchId()
                )
                .setDoc(ElasticUtils.writeAsJsonBytes(post), XContentType.JSON)
                .get();
    }

    /**
     * Find post by search_id
     * @param searchId
     * @return
     */
    public Post getPostByElasticId(final String searchId) {
        GetResponse response = elasticClient
                .prepareGet(
                        config.getPostIndexName(),
                        config.getPostIndexName(),
                        searchId
                ).get();

        return ElasticUtils.parseGetResponse(response, PostSearchEntity.class);
    }


    /**
     * Find post by id (not search id)
     * @param id
     * @return
     */
    public Post getPostById(final String id) {
        SearchResponse response = elasticClient
                .prepareSearch(config.getPostIndexName())
                .setTypes(ElasticUtils.getElasticTypeWithoutDate(config.getPostIndexName()))
                .setQuery(QueryBuilders.termQuery(PostField.ID.getFieldName(), id))
                .get();

        List<Post> results = ElasticUtils.parseSearchResponse(response, PostSearchEntity.class);
        return CollectionUtils.isEmpty(results) ? null : results.get(0);
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
    public List<Post> getPostByName(
            final String name,
            final Page page,
            @Nullable final PostField postField,
            @Nullable final SearchOrder searchOrder
    ) {
        return searchPostByField(
                PostField.NAME.getFieldName(),
                name,
                page,
                postField,
                searchOrder
        );
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
    public List<Post> getPostByDescription(
            final String description,
            final Page page,
            @Nullable final PostField postField,
            @Nullable final SearchOrder searchOrder
    ) {
        return searchPostByField(
                PostField.DESCRIPTION.getFieldName(),
                description,
                page,
                postField,
                searchOrder
        );
    }

    /**
     * Find last posts from current(inclusive)
     * Throws Exception if page size more than limit {@link ElasticsearchConfig#getMaxSizePostsPerPage()}}
     * @param lastDays counts of last days from current (inclusive)
     * @param page
     * @param postField
     * @param searchOrder
     * @return list of post
     */
    public List<Post> getLastPosts(
            final Page page,
            final int lastDays,
            @Nullable final PostField postField,
            @Nullable final SearchOrder searchOrder
    ) {
        return searchPostByDate(
                page,
                ElasticUtils.getElasticTypeWithLastDays(
                        config.getPostTypeName(),
                        lastDays
                ),
                postField,
                searchOrder
        );
    }

    /**
     * Find posts in specific date
     * Throws Exception if page size more than limit {@link ElasticsearchConfig#getMaxSizePostsPerPage()}}
     * @param postDate
     * @param page
     * @param postField
     * @param searchOrder
     * @return list of post
     */
    public List<Post> getPostsByDate(
            final Page page,
            LocalDate postDate,
            @Nullable final PostField postField,
            @Nullable final SearchOrder searchOrder
    ) {
        return searchPostByDate(
                page,
                ElasticUtils.getElasticTypeWithDay(
                        config.getPostTypeName(),
                        postDate
                ),
                postField,
                searchOrder
        );
    }

    /**
     * Get all posts
     * Throws Exception if page size more than limit {@link ElasticsearchConfig#getMaxSizePostsPerPage()}}
     * @param page
     * @param postField
     * @param searchOrder
     * @return list of post
     */
    public List<Post> getPosts(
            final Page page,
            @Nullable final PostField postField,
            @Nullable final SearchOrder searchOrder
    ) {
        return searchPostByDate(
                page,
                ElasticUtils.getElasticTypeWithoutDate(config.getPostTypeName()),
                postField,
                searchOrder
        );
    }

    private List<Post> searchPostByDate(
            final Page page,
            final String typeName,
            @Nullable final PostField postField,
            @Nullable final SearchOrder searchOrder
    ) {
        if(page.getSize() >= config.getMaxSizePostsPerPage()) {
            throw new PageSizeLimitException();
        }
        SearchRequestBuilder request = elasticClient
                .prepareSearch(config.getPostIndexName())
                .setTypes(typeName)
                .setQuery(
                        QueryBuilders.matchAllQuery()
                )
                .setFrom(page.getOffset())
                .setSize(page.getSize());

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

        return ElasticUtils.parseSearchResponse(response, PostSearchEntity.class);
    }

    private List<Post> searchPostByField(
            final String fieldName,
            final String fieldValue,
            final Page page,
            @Nullable final PostField postField,
            @Nullable final SearchOrder searchOrder
    ) {
        if(page.getSize() >= config.getMaxSizePostsPerPage()) {
            throw new PageSizeLimitException();
        }
        SearchRequestBuilder request = elasticClient
                .prepareSearch(config.getPostIndexName())
                .setTypes(ElasticUtils.getElasticTypeWithoutDate(config.getPostTypeName()))
                .setQuery(
                        QueryBuilders.matchQuery(fieldName, fieldValue)
                )
                .setFrom(page.getOffset())
                .setSize(page.getSize());

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

        return ElasticUtils.parseSearchResponse(response, PostSearchEntity.class);
    }
}
