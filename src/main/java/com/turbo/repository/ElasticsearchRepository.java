package com.turbo.repository;

import com.turbo.config.ElasticsearchConfig;
import com.turbo.model.Post;
import com.turbo.repository.util.ElasticUtils;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Created by ermolaev on 5/15/17.
 */
@Repository
public class ElasticsearchRepository {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private final TransportClient elasticClient;
    private final ElasticsearchConfig config;

    @Autowired
    public ElasticsearchRepository(ElasticsearchConfig config) {
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

    public Post getPostById(final String elasticId) {
        GetResponse response = elasticClient
                .prepareGet(
                        config.getPostIndexName(),
                        config.getPostIndexName(),
                        elasticId
                ).get();

        return ElasticUtils.parseGetResponse(response, Post.class);
    }

    public List<Post> getPostByName(final String name, final int from, final int size) {
        return searchPostByField("name", name, from, size);
    }

    public List<Post> getPostByDescription(final String description, final int from, final int size) {
        return searchPostByField("description", description, from, size);
    }

    private List<Post> searchPostByField(final String fieldName, final String fieldValue, final int from, final int size) {
        SearchResponse response = elasticClient
                .prepareSearch(config.getPostIndexName())
                .setTypes(ElasticUtils.getElasticTypeWithoutDate(config.getPostTypeName()))
                .setQuery(
                        QueryBuilders.matchQuery(fieldName, fieldValue)
                )
                .setFrom(from)
                .setSize(size)
                .get();

        return ElasticUtils.parseSearchResponse(response, Post.class);
    }
}
