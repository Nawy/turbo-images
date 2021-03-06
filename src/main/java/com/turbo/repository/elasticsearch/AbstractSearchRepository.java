package com.turbo.repository.elasticsearch;

import com.turbo.config.ElasticsearchConfig;
import com.turbo.model.Nullable;
import com.turbo.model.page.Page;
import com.turbo.model.search.SearchOrder;
import com.turbo.model.search.content.ImageSearchEntity;
import com.turbo.util.ElasticUtils;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;

import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * Created by ermolaev on 5/28/17.
 */
public abstract class AbstractSearchRepository {

    protected static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(ImageSearchEntity.CREATION_DATE_PATTERN);

    protected final TransportClient elasticClient;
    protected final ElasticsearchConfig config;
    protected final ElasticUtils elasticUtils;

    public AbstractSearchRepository(TransportClient elasticClient, ElasticsearchConfig config, ElasticUtils elasticUtils) {
        this.elasticClient = elasticClient;
        this.config = config;
        this.elasticUtils = elasticUtils;
    }

    protected SearchResponse searchUniqueByField(
            final String indexName,
            final String typeName,
            final String fieldName,
            final Object fieldValue
    ) {
        SearchResponse response = elasticClient
                .prepareSearch(indexName)
                .setTypes(typeName)
                .setQuery(
                        QueryBuilders.matchQuery(fieldName, fieldValue)
                )
                .setFrom(0)
                .setSize(1)
                .get();

        return response;
    }

    protected SearchResponse searchByField(
            final String indexName,
            final String typeName,
            final String fieldName,
            final Object fieldValue,
            final int page,
            @Nullable final String fieldNameSort,
            @Nullable final SearchOrder searchOrder
    ) {
        Page queryPage = new Page(page);
        SearchRequestBuilder request = elasticClient
                .prepareSearch(indexName)
                .setTypes(typeName)
                .setQuery(
                        QueryBuilders.matchQuery(fieldName, fieldValue)
                )
                .setFrom(queryPage.getOffset())
                .setSize(queryPage.getSize());

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

    protected SearchResponse getByField(
            final String indexName,
            final String typeName,
            final String fieldName,
            final String fieldValue,
            final int page,
            @Nullable final String fieldNameSort,
            @Nullable final SearchOrder searchOrder
    ) {
        Page pageObject = new Page(page);
        SearchRequestBuilder request = elasticClient
                .prepareSearch(indexName)
                .setTypes(typeName)
                .setQuery(
                        QueryBuilders.termQuery(fieldName, fieldValue)
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
