package com.turbo.repository.elasticsearch.stat;

import com.turbo.config.ElasticsearchConfig;
import com.turbo.model.Nullable;
import com.turbo.model.search.field.UserField;
import com.turbo.repository.elasticsearch.AbstractSearchRepository;
import com.turbo.repository.elasticsearch.helper.SearchOrder;
import com.turbo.repository.util.ElasticUtils;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

/**
 * Created by ermolaev on 6/4/17.
 */
@Repository
public class PostStatRepository extends AbstractSearchRepository {

    @Autowired
    public PostStatRepository(ElasticsearchConfig config) {
        super(config.getElasticClient(), config);
    }

    public List<Long> getPostPerDay(
            final LocalDate date,
            @Nullable UserField orderField,
            @Nullable SearchOrder searchOrder
    ) {
//        SearchRequestBuilder builder = elasticClient
//                .prepareSearch(config.getStatPostsIndexName())
//                .setTypes(ElasticUtils.getTypePerYear(config.getStatPostsTypeName(), date))
//
        // TODO
        return Collections.emptyList();
    }
}
