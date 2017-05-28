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
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;

import java.util.List;
import java.util.Objects;

/**
 * Created by ermolaev on 5/28/17.
 */
public abstract class AbstractSearchRepository {
    protected final TransportClient elasticClient;
    protected final ElasticsearchConfig config;

    public AbstractSearchRepository(TransportClient elasticClient, ElasticsearchConfig config) {
        this.elasticClient = elasticClient;
        this.config = config;
    }

    protected SearchResponse searchByDate(
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

        return request.get();
    }

    protected SearchResponse searchByField(
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

        return request.get();
    }
}
