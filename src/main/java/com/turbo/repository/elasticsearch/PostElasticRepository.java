package com.turbo.repository.elasticsearch;

import com.turbo.config.ElasticsearchConfig;
import com.turbo.model.Nullable;
import com.turbo.model.Post;
import com.turbo.repository.elasticsearch.helper.SearchSort;
import com.turbo.repository.elasticsearch.helper.PostFields;
import com.turbo.repository.util.ElasticUtils;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.SortBuilders;
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
public class PostElasticRepository {

    private final TransportClient elasticClient;
    private final ElasticsearchConfig config;

    @Autowired
    public PostElasticRepository(ElasticsearchConfig config) {
        this.elasticClient = config.getElasticClient();
        this.config = config;
    }

    public void addPost(final Post post) {
        elasticClient
                .prepareIndex(
                        config.getPostIndexName(),
                        ElasticUtils.getElasticTypeWithCurrentDate(config.getPostTypeName())
                )
                .setSource(ElasticUtils.writeAsJsonBytes(post), XContentType.JSON)
                .get();
    }

    public void updatePost(final Post post) {
//        elasticClient
//                .prepareUpdate(
//                        ElasticUtils.getElasticTypeWithoutDate(config.getPostIndexName()),
//                        post.
    }

    public Post getPostByElasticId(final String elasticId) {
        GetResponse response = elasticClient
                .prepareGet(
                        config.getPostIndexName(),
                        config.getPostIndexName(),
                        elasticId
                ).get();

        return ElasticUtils.parseGetResponse(response, Post.class);
    }

    public Post getPostById(final String id) {
        SearchResponse response = elasticClient
                .prepareSearch(config.getPostIndexName())
                .setTypes(ElasticUtils.getElasticTypeWithoutDate(config.getPostIndexName()))
                .setQuery(QueryBuilders.termQuery(PostFields.ID, id))
                .get();

        List<Post> results = ElasticUtils.parseSearchResponse(response, Post.class);
        return CollectionUtils.isEmpty(results) ? null : results.get(0);
    }

    public List<Post> getPostByName(
            final String name,
            final int from,
            final int size,
            @Nullable final SearchSort searchSort
    ) {
        return searchPostByField(PostFields.NAME, name, from, size, searchSort);
    }

    public List<Post> getPostByDescription(
            final String description,
            final int from,
            final int size,
            @Nullable final SearchSort searchSort
    ) {
        return searchPostByField(PostFields.DESCRIPTION, description, from, size, searchSort);
    }

    public List<Post> getLastPosts(
            final int from,
            final int size,
            final int lastDays,
            @Nullable final SearchSort searchSort
    ) {
        return searchPostByDate(
                from,
                size,
                ElasticUtils.getElasticTypeWithLastDays(
                        config.getPostTypeName(),
                        lastDays
                ),
                searchSort
        );
    }

    public List<Post> getPostsByDate(
            final int from,
            final int size,
            LocalDate postDate,
            @Nullable final SearchSort searchSort
    ) {
        return searchPostByDate(
                from,
                size,
                ElasticUtils.getElasticTypeWithDay(
                        config.getPostTypeName(),
                        postDate
                ),
                searchSort
        );
    }

    public List<Post> getPosts(
            final int from,
            final int size,
            @Nullable final SearchSort searchSort
    ) {
        return searchPostByDate(
                from,
                size,
                ElasticUtils.getElasticTypeWithoutDate(config.getPostTypeName()),
                searchSort
        );
    }




    private List<Post> searchPostByDate(
            final int from,
            final int size,
            final String typeName,
            @Nullable final SearchSort searchSort
    ) {
        SearchRequestBuilder request = elasticClient
                .prepareSearch(config.getPostIndexName())
                .setTypes(ElasticUtils.getElasticTypeWithoutDate(config.getPostTypeName()))
                .setQuery(
                        QueryBuilders.matchAllQuery()
                )
                .setFrom(from)
                .setSize(size);

        if(Objects.nonNull(searchSort)) {
            request.addSort(
                    SortBuilders
                            .fieldSort(searchSort.getFieldName())
                            .order(searchSort.getOrder())
            );
        }
        final SearchResponse response = request.get();

        return ElasticUtils.parseSearchResponse(response, Post.class);
    }

    private List<Post> searchPostByField(
            final String fieldName,
            final String fieldValue,
            final int from,
            final int size,
            @Nullable final SearchSort searchSort
    ) {
        SearchRequestBuilder request = elasticClient
                .prepareSearch(config.getPostIndexName())
                .setTypes(ElasticUtils.getElasticTypeWithoutDate(config.getPostTypeName()))
                .setQuery(
                        QueryBuilders.matchQuery(fieldName, fieldValue)
                )
                .setFrom(from)
                .setSize(size);

        if(Objects.nonNull(searchSort)) {
            request.addSort(
                    SortBuilders
                            .fieldSort(searchSort.getFieldName())
                            .order(searchSort.getOrder())
            );
        }

        final SearchResponse response = request.get();

        return ElasticUtils.parseSearchResponse(response, Post.class);
    }
}
