package com.turbo.repository.elasticsearch;

import com.turbo.config.ElasticsearchConfig;
import com.turbo.model.Nullable;
import com.turbo.model.exception.data.PageSizeLimitException;
import com.turbo.model.page.Page;
import com.turbo.repository.elasticsearch.helper.SearchOrder;
import com.turbo.repository.util.ElasticUtils;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;

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
            final String indexName,
            final String typeName,
            final int page,
            @Nullable final String fieldNameSort,
            @Nullable final SearchOrder searchOrder
    ) {
        Page pageObject = new Page(page);
        if(pageObject.getSize() >= config.getMaxSizePostsPerPage()) {
            throw new PageSizeLimitException();
        }
        SearchRequestBuilder request = elasticClient
                .prepareSearch(indexName)
                .setTypes(typeName)
                .setQuery(
                        QueryBuilders.matchAllQuery()
                )
                .setFrom(pageObject.getOffset())
                .setSize(pageObject.getSize());

        if(Objects.nonNull(fieldNameSort) && Objects.nonNull(searchOrder)) {
            request.addSort(
                    SortBuilders
                            .fieldSort(fieldNameSort)
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
            final String indexName,
            final String typeName,
            final String fieldName,
            final String fieldValue,
            final int page,
            @Nullable final String fieldNameSort,
            @Nullable final SearchOrder searchOrder
    ) {
        Page pageObject = new Page(page);
        if(pageObject.getSize() >= config.getMaxSizePostsPerPage()) {
            throw new PageSizeLimitException();
        }
        SearchRequestBuilder request = elasticClient
                .prepareSearch(indexName)
                .setTypes(ElasticUtils.getElasticTypeWithoutDate(typeName))
                .setQuery(
                        QueryBuilders.matchQuery(fieldName, fieldValue)
                )
                .setFrom(pageObject.getOffset())
                .setSize(pageObject.getSize());

        if(Objects.nonNull(fieldNameSort) && Objects.nonNull(searchOrder)) {
            request.addSort(
                    SortBuilders
                            .fieldSort(fieldNameSort)
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
