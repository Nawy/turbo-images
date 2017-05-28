package com.turbo.repository.elasticsearch;

import com.turbo.config.ElasticsearchConfig;
import com.turbo.model.Nullable;
import com.turbo.model.Post;
import com.turbo.model.page.Page;
import com.turbo.model.search.entity.PostSearchEntity;
import com.turbo.repository.elasticsearch.field.PostField;
import com.turbo.repository.elasticsearch.helper.SearchOrder;
import com.turbo.repository.util.ElasticUtils;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
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
public class PostSearchRepository extends AbstractSearchRepository {

    @Autowired
    public PostSearchRepository(ElasticsearchConfig config) {
        super(config.getElasticClient(), config);
    }

    /**
     * Add new post to search engine
     * @param post
     * @return
     */
    public void addPost(final Post post) {
        final PostSearchEntity entity = new PostSearchEntity(post);

        elasticClient
                .prepareIndex(
                        config.getPostIndexName(),
                        ElasticUtils.getElasticTypeWithCurrentDate(config.getPostTypeName())
                )
                .setSource(ElasticUtils.writeAsJsonBytes(entity), XContentType.JSON)
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
        final PostSearchEntity entity = new PostSearchEntity(post);

        elasticClient
                .prepareUpdate(
                        config.getPostIndexName(),
                        ElasticUtils.getElasticTypeWithoutDate(config.getPostIndexName()),
                        post.getSearchId()
                )
                .setDoc(ElasticUtils.writeAsJsonBytes(entity), XContentType.JSON)
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
        SearchResponse response = searchByField(
                PostField.NAME.getFieldName(),
                name,
                page,
                postField,
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
    public List<Post> getPostByDescription(
            final String description,
            final Page page,
            @Nullable final PostField postField,
            @Nullable final SearchOrder searchOrder
    ) {
        final SearchResponse response = searchByField(
                PostField.DESCRIPTION.getFieldName(),
                description,
                page,
                postField,
                searchOrder
        );

        return ElasticUtils.parseSearchResponse(response, PostSearchEntity.class);
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
        SearchResponse response = searchByDate(
                page,
                ElasticUtils.getElasticTypeWithLastDays(
                        config.getPostTypeName(),
                        lastDays
                ),
                postField,
                searchOrder
        );

        return ElasticUtils.parseSearchResponse(response, PostSearchEntity.class);
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
        SearchResponse response = searchByDate(
                page,
                ElasticUtils.getElasticTypeWithDay(
                        config.getPostTypeName(),
                        postDate
                ),
                postField,
                searchOrder
        );

        return ElasticUtils.parseSearchResponse(response, PostSearchEntity.class);
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
        SearchResponse response = searchByDate(
                page,
                ElasticUtils.getElasticTypeWithoutDate(config.getPostTypeName()),
                postField,
                searchOrder
        );

        return ElasticUtils.parseSearchResponse(response, PostSearchEntity.class);
    }


}
