package com.turbo.repository.elasticsearch.stat;

import com.turbo.config.ElasticsearchConfig;
import com.turbo.model.Nullable;
import com.turbo.model.search.field.stat.*;
import com.turbo.repository.elasticsearch.AbstractSearchRepository;
import com.turbo.repository.elasticsearch.ElasticId;
import com.turbo.model.search.SearchOrder;
import com.turbo.repository.util.ElasticUtils;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Created by ermolaev on 6/4/17.
 */
@Repository
public class PostStatRepository extends AbstractSearchRepository {

    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Autowired
    public PostStatRepository(ElasticsearchConfig config) {
        super(config.getElasticClient(), config);
    }

    /**
     * Get post per specific day, sorted by @orderField in @searchOrder and
     * filter by field @field with value @value
     * @param field
     * @param date
     * @param orderField
     * @param searchOrder
     * @return list of posts' id
     */
    public List<Long> getPostPerDayByOrder(
            @Nullable final PostStatField field,
            @Nullable final String value,
            final LocalDate date,
            final PostDayStatField orderField,
            final SearchOrder searchOrder
    ) {
        return getPostPerRangeByOrder(
                Objects.nonNull(field) ? field.getFieldName() : null,
                value,
                PostStatField.DAYS_DATE.getFieldName(),
                date,
                orderField.getFieldName(),
                searchOrder
        );
    }

    /**
     * Get post per specific week, sorted by @orderField in @searchOrder and
     * filter by field @field with value @value
     * @param field
     * @param date
     * @param orderField
     * @param searchOrder
     * @return list of posts' id
     */
    public List<Long> getPostPerWeekByOrder(
            @Nullable final PostStatField field,
            @Nullable final String value,
            final LocalDate date,
            final PostWeeksStatField orderField,
            final SearchOrder searchOrder
    ) {
        return getPostPerRangeByOrder(
                Objects.nonNull(field) ? field.getFieldName() : null,
                value,
                PostStatField.WEEKS_DATE.getFieldName(),
                date,
                orderField.getFieldName(),
                searchOrder
        );
    }

    /**
     * Get post per specific month, sorted by @orderField in @searchOrder and
     * filter by field @field with value @value
     * @param field
     * @param date
     * @param orderField
     * @param searchOrder
     * @return list of posts' id
     */
    public List<Long> getPostPerMonthByOrder(
            @Nullable final PostStatField field,
            @Nullable final String value,
            final LocalDate date,
            final PostMonthStatField orderField,
            final SearchOrder searchOrder
    ) {
        return getPostPerRangeByOrder(
                Objects.nonNull(field) ? field.getFieldName() : null,
                value,
                PostStatField.MONTHS_DATE.getFieldName(),
                date,
                orderField.getFieldName(),
                searchOrder
        );
    }

    /**
     * Get post per specific year, sorted by @orderField in @searchOrder and
     * filter by field @field with value @value
     * @param field
     * @param date
     * @param orderField
     * @param searchOrder
     * @return list of posts' id
     */
    public List<Long> getPostPerYearByOrder(
            @Nullable final PostStatField field,
            @Nullable final String value,
            final LocalDate date,
            final PostYearStatField orderField,
            final SearchOrder searchOrder
    ) {
        return getPostPerRangeByOrder(
                Objects.nonNull(field) ? field.getFieldName() : null,
                value,
                PostStatField.YEAR_DATE.getFieldName(),
                date,
                orderField.getFieldName(),
                searchOrder
        );
    }

    // inner methods

    private List<Long> getPostPerRangeByOrder(
            @Nullable final String field,
            @Nullable final String value,
            final String rangeDateField, // days, weeks, months, year
            final LocalDate date,
            final String orderField,
            final SearchOrder searchOrder
    ) {
        SearchRequestBuilder builder = elasticClient
                .prepareSearch(config.getStatPostsIndexName())
                .setTypes(ElasticUtils.getTypePerYear(config.getStatPostsTypeName(), date));

        // create query builder
        BoolQueryBuilder boolBuilder = QueryBuilders.boolQuery();
        boolBuilder.must(
                QueryBuilders.termQuery(
                        rangeDateField,
                        dateFormatter.format(date)
                )
        );
        if(Objects.nonNull(field) && StringUtils.isNotBlank(value)) {
            boolBuilder.must(
                    QueryBuilders.matchQuery(
                            field,
                            value
                    )
            );
        }

        builder.setQuery(boolBuilder).addSort(
                SortBuilders
                        .fieldSort(orderField)
                        .order(
                                searchOrder == SearchOrder.DESC ?
                                        SortOrder.DESC :
                                        SortOrder.ASC
                        )
        );

        SearchResponse response = builder.get();

        return ElasticUtils.parseSearchResponse(response, ElasticId.class)
                .stream()
                .map(ElasticId::getId)
                .collect(Collectors.toList());
    }
}
