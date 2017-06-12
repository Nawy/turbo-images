package com.turbo.repository.elasticsearch.stat;

import com.turbo.config.ElasticsearchConfig;
import com.turbo.model.Nullable;
import com.turbo.model.page.Page;
import com.turbo.model.search.SearchOrder;
import com.turbo.model.search.field.stat.*;
import com.turbo.repository.elasticsearch.AbstractSearchRepository;
import com.turbo.repository.elasticsearch.ElasticId;
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
     * Get post in specific period(date -> periodField), sorted by sortingField in searchOrder
     * with search query by field with value
     * @param field
     * @param value
     * @param page
     * @param date
     * @param periodField
     * @param sortingField
     * @param searchOrder
     * @return
     */
    public List<Long> getPostStat(
            @Nullable final PostStatField field,
            @Nullable final String value,
            final Page page,
            final LocalDate date,
            final PostStatPeriod periodField,
            final PostDiffStatField sortingField,
            final SearchOrder searchOrder
    ) {
        SearchRequestBuilder builder = elasticClient
                .prepareSearch(config.getStatPostsIndexName())
                .setTypes(ElasticUtils.getTypePerYear(config.getStatPostsTypeName(), date));

        // create query builder
        BoolQueryBuilder boolBuilder = QueryBuilders.boolQuery();

        //set specific date period
        boolBuilder.must(
                QueryBuilders.termQuery(
                        periodField.getFieldName(PostDiffStatField.DATE),
                        dateFormatter.format(date)
                )
        );

        //set filter
        if(Objects.nonNull(field) && StringUtils.isNotBlank(value)) {
            boolBuilder.must(
                    QueryBuilders.matchQuery(
                            field.getFieldName(),
                            value
                    )
            );
        }

        // set sorting
        builder.setQuery(boolBuilder).addSort(
                SortBuilders
                        .fieldSort(periodField.getFieldName(sortingField))
                        .order(
                                searchOrder == SearchOrder.DESC ?
                                        SortOrder.DESC :
                                        SortOrder.ASC
                        )
        );

        SearchResponse response = builder
                .setFrom(page.getOffset())
                .setSize(page.getSize())
                .get();

        return ElasticUtils.parseSearchResponse(response, ElasticId.class)
                .stream()
                .map(ElasticId::getId)
                .collect(Collectors.toList());
    }
}
