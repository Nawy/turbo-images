package com.turbo.repository.elasticsearch.stat;

import com.turbo.config.ElasticsearchConfig;
import com.turbo.repository.elasticsearch.AbstractSearchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * Created by ermolaev on 6/4/17.
 */
@Repository
public class UserStatRepository extends AbstractSearchRepository {

    @Autowired
    public UserStatRepository(ElasticsearchConfig config) {
        super(config.getElasticClient(), config);
    }
}
