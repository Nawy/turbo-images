package com.turbo.repository.elasticsearch.content;

import com.turbo.config.ElasticsearchConfig;
import com.turbo.model.UserImage;
import com.turbo.model.search.content.ImageSearchEntity;
import com.turbo.repository.elasticsearch.AbstractSearchRepository;
import com.turbo.repository.util.ElasticUtils;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * Created by ermolaev on 6/24/17.
 */
@Repository
public class ImageSearchRepository extends AbstractSearchRepository {

    @Autowired
    public ImageSearchRepository(ElasticsearchConfig config, ElasticUtils elasticUtils) {
        super(config.getElasticClient(), config, elasticUtils);
    }

    public void addImage(final UserImage image) {
        elasticClient
                .prepareIndex(
                        config.getSearchImageIndexName(),
                        config.getSearchImageTypeName()
                )
                .setSource(elasticUtils.writeAsJsonBytes(image), XContentType.JSON)
                .get();
    }
}
