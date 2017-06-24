package com.turbo.repository.elasticsearch.stat;

import com.turbo.config.ElasticsearchConfig;
import com.turbo.model.Nullable;
import com.turbo.model.page.Page;
import com.turbo.model.search.SearchOrder;
import com.turbo.model.search.field.stat.PostDiffStatField;
import com.turbo.model.search.field.stat.PostStatField;
import com.turbo.model.search.field.stat.PostStatPeriod;
import com.turbo.repository.elasticsearch.AbstractSearchRepository;
import com.turbo.repository.elasticsearch.ElasticId;
import com.turbo.repository.util.ElasticUtils;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by ermolaev on 6/4/17.
 */
@Repository
public class PostStatRepository extends AbstractSearchRepository {

    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM-dd");
    private static final DateTimeFormatter weekFormatter = DateTimeFormatter.ofPattern("ww");
    private static final DateTimeFormatter monthFormatter = DateTimeFormatter.ofPattern("MM");

    @Autowired
    public PostStatRepository(ElasticsearchConfig config, ElasticUtils elasticUtils) {
        super(config.getElasticClient(), config, elasticUtils);
    }

    /**
     * Get post in specific period(date -> periodField), sorted by sortingField in searchOrder
     * with search query by field with value
     * @return
     */
    public PostStatBuilder getPostStat() {
        return new PostStatBuilder(this::getPostStat);
    }

    private List<Long> getPostStat(
            PostStatBuilder request
    ) {
        final String filteredDate = getFormattedDate(request.getDate(), request.getPeriodField());
        SearchRequestBuilder builder = elasticClient
                .prepareSearch(config.getStatPostsIndexName())
                .setTypes(elasticUtils.getTypePerYear(config.getStatPostsTypeName(), request.getDate()));

        // create query builder
        BoolQueryBuilder boolBuilder = QueryBuilders.boolQuery();

        if(Objects.nonNull(filteredDate)) {
            //set specific date period
            boolBuilder = setSearchDate(request, filteredDate, boolBuilder);
        }

        //set filter
        if(Objects.nonNull(request.getField()) && StringUtils.isNotBlank(request.getValue())) {
            setSearchQuery(request, boolBuilder);
        }

        builder = builder.setQuery(boolBuilder);

        // set sorting
        if(Objects.nonNull(filteredDate)) {
            builder = builder.addSort(setSearchSortingNested(request, filteredDate));
        } else {
            builder = builder.addSort(setSearchSorting(request));
        }

        SearchResponse response = builder
                .setFrom(request.getPage().getOffset())
                .setSize(request.getPage().getSize())
                .get();

        return elasticUtils.parseSearchResponse(response, ElasticId.class)
                .stream()
                .map(ElasticId::getId)
                .collect(Collectors.toList());
    }

    private BoolQueryBuilder setSearchQuery(PostStatBuilder request, BoolQueryBuilder builder) {
        return builder.must(
                QueryBuilders.matchQuery(
                        request.getField().getFieldName(),
                        request.getValue()
                )
        );
    }

    private BoolQueryBuilder setSearchDate(PostStatBuilder request, String dateString, BoolQueryBuilder builder) {

        final String searchFieldName = request.getPeriodField().getFieldName(PostDiffStatField.DATE);

        return builder.must(
                QueryBuilders.termQuery(
                        searchFieldName,
                        dateString
                )
        );
    }

    private FieldSortBuilder setSearchSortingNested(PostStatBuilder request, String dateString) {
        return SortBuilders
                .fieldSort(request.getPeriodField().getFieldName(request.getSortingField()))
                .setNestedPath(request.getPeriodField().getFieldName())
                .setNestedFilter(
                        QueryBuilders.termQuery(
                                request.getPeriodField().getFieldName(PostDiffStatField.DATE),
                                dateString
                        )
                )
                .order(
                        request.getSearchOrder() == SearchOrder.DESC ?
                                SortOrder.DESC :
                                SortOrder.ASC
                );
    }
    private FieldSortBuilder setSearchSorting(PostStatBuilder request) {
        return SortBuilders
                .fieldSort(request.getPeriodField().getFieldName(request.getSortingField()))
                .order(
                        request.getSearchOrder() == SearchOrder.DESC ?
                                SortOrder.DESC :
                                SortOrder.ASC
                );
    }

    // builder

    public static class PostStatBuilder {
        private @Nullable PostStatField field;
        private @Nullable String value;

        private Page page;

        private LocalDate date;
        private PostStatPeriod periodField;

        private PostDiffStatField sortingField;
        private SearchOrder searchOrder;

        private Function<PostStatBuilder, List<Long>> requestFunc;

        public PostStatBuilder(Function<PostStatBuilder, List<Long>> requestFunc) {
            this.requestFunc = requestFunc;
        }

        public List<Long> execute() {
            return requestFunc.apply(this);
        }

        public PostStatBuilder match(PostStatField field, String value) {
            this.field = field;
            this.value = value;
            return this;
        }

        public PostStatBuilder date(LocalDate date, PostStatPeriod periodField) {
            this.date = date;
            this.periodField = periodField;
            return this;
        }

        public PostStatBuilder page(int number) {
            this.page = new Page(number);
            return this;
        }

        public PostStatBuilder sort(PostDiffStatField sortingField, SearchOrder searchOrder) {
            this.sortingField = sortingField;
            this.searchOrder = searchOrder;
            return this;
        }

        // getter function

        protected PostStatField getField() {
            return field;
        }

        protected String getValue() {
            return value;
        }

        protected Page getPage() {
            return page;
        }

        protected LocalDate getDate() {
            return date;
        }

        protected PostStatPeriod getPeriodField() {
            return periodField;
        }

        protected PostDiffStatField getSortingField() {
            return sortingField;
        }

        protected SearchOrder getSearchOrder() {
            return searchOrder;
        }
    }

    private String getFormattedDate(
            final LocalDate date,
            final PostStatPeriod periodField
    ) {
        switch (periodField) {
            case DAYS: return dateFormatter.format(date);
            case WEEKS: return weekFormatter.format(date);
            case MONTHS: return monthFormatter.format(date);
            default: return null;
        }
    }
}
