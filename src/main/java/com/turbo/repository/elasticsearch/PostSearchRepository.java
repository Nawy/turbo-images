package com.turbo.repository.elasticsearch;

import com.turbo.config.ElasticsearchConfig;
import com.turbo.model.Nullable;
import com.turbo.model.Post;
import com.turbo.model.exception.data.NotFoundException;
import com.turbo.model.page.Page;
import com.turbo.model.page.Paginator;
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
        Objects.requireNonNull(post.getSearchId(), "for update post you need have search id");
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
                        ElasticUtils.getElasticTypeWithoutDate(config.getPostTypeName()),
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
        if(CollectionUtils.isEmpty(results)) {
            throw new NotFoundException();
        } else {
            return results.get(0);
        }
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
    public Paginator<Post> getPostByName(
            final String name,
            final Page page,
            @Nullable final PostField postField,
            @Nullable final SearchOrder searchOrder
    ) {
        SearchResponse response = searchByField(
                config.getPostIndexName(),
                ElasticUtils.getElasticTypeWithoutDate(config.getPostTypeName()),
                PostField.NAME.getFieldName(),
                name,
                page,
                Objects.isNull(postField) ? null : postField.getFieldName(),
                searchOrder
        );

        return new Paginator<>(
                page,
                ElasticUtils.parseSearchResponse(response, PostSearchEntity.class)
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
    public Paginator<Post> getPostByDescription(
            final String description,
            final Page page,
            @Nullable final PostField postField,
            @Nullable final SearchOrder searchOrder
    ) {
        final SearchResponse response = searchByField(
                config.getPostIndexName(),
                ElasticUtils.getElasticTypeWithoutDate(config.getPostTypeName()),
                PostField.NAME.getFieldName(),
                description,
                page,
                Objects.isNull(postField) ? null : postField.getFieldName(),
                searchOrder
        );

        return new Paginator<>(
                page,
                ElasticUtils.parseSearchResponse(response, PostSearchEntity.class)
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
    public Paginator<Post> getLastPosts(
            final Page page,
            final int lastDays,
            @Nullable final PostField postField,
            @Nullable final SearchOrder searchOrder
    ) {
        final SearchResponse response = searchByDate(
                config.getPostIndexName(),
                ElasticUtils.getElasticTypeWithLastDays(config.getPostTypeName(), lastDays),
                page,
                Objects.isNull(postField) ? null : postField.getFieldName(),
                searchOrder
        );

        return new Paginator<>(
                page,
                ElasticUtils.parseSearchResponse(response, PostSearchEntity.class)
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
    public Paginator<Post> getPostsByDate(
            final Page page,
            LocalDate postDate,
            @Nullable final PostField postField,
            @Nullable final SearchOrder searchOrder
    ) {
        final SearchResponse response = searchByDate(
                config.getPostIndexName(),
                ElasticUtils.getElasticTypeWithDate(config.getPostTypeName(), postDate),
                page,
                Objects.isNull(postField) ? null : postField.getFieldName(),
                searchOrder
        );

        return new Paginator<>(
                page,
                ElasticUtils.parseSearchResponse(response, PostSearchEntity.class)
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
    public Paginator<Post> getPosts(
            final Page page,
            @Nullable final PostField postField,
            @Nullable final SearchOrder searchOrder
    ) {
        final SearchResponse response = searchByDate(
                config.getPostIndexName(),
                ElasticUtils.getElasticTypeWithoutDate(config.getPostTypeName()),
                page,
                Objects.isNull(postField) ? null : postField.getFieldName(),
                searchOrder
        );

        return new Paginator<>(
                page,
                ElasticUtils.parseSearchResponse(response, PostSearchEntity.class)
        );
    }
}
