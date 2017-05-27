package com.turbo.repository.elasticsearch;

import com.turbo.config.ElasticsearchConfig;
import com.turbo.model.Nullable;
import com.turbo.model.Post;
import com.turbo.model.page.Page;
import com.turbo.repository.elasticsearch.helper.PostField;
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
                .setQuery(QueryBuilders.termQuery(PostField.ID.getFieldName(), id))
                .get();

        List<Post> results = ElasticUtils.parseSearchResponse(response, Post.class);
        return CollectionUtils.isEmpty(results) ? null : results.get(0);
    }

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
        SearchRequestBuilder request = elasticClient
                .prepareSearch(config.getPostIndexName())
                .setTypes(ElasticUtils.getElasticTypeWithoutDate(config.getPostTypeName()))
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

        return ElasticUtils.parseSearchResponse(response, Post.class);
    }

    private List<Post> searchPostByField(
            final String fieldName,
            final String fieldValue,
            final Page page,
            @Nullable final PostField postField,
            @Nullable final SearchOrder searchOrder
    ) {
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

        return ElasticUtils.parseSearchResponse(response, Post.class);
    }
}
