package com.turbo.repository.elasticsearch.content;

import com.turbo.config.ElasticsearchConfig;
import com.turbo.model.UserImage;
import com.turbo.model.search.content.ImageSearchEntity;
import com.turbo.model.search.field.ImageField;
import com.turbo.repository.elasticsearch.AbstractSearchRepository;
import com.turbo.repository.util.ElasticUtils;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * Created by ermolaev on 6/24/17.
 */
@Repository
public class UserImageSearchRepository extends AbstractSearchRepository {

    @Autowired
    public UserImageSearchRepository(ElasticsearchConfig config, ElasticUtils elasticUtils) {
        super(config.getElasticClient(), config, elasticUtils);
    }

    public void addUserImage(final UserImage image) {
        ImageSearchEntity searchImage = new ImageSearchEntity(
                image.getId(),
                image.getDescription(),
                image.getImage().getThumbnail(),
                image.getImage().getSource(),
                image.getUsername(),
                image.getCreationDate()
        );

        elasticClient
                .prepareIndex(
                        config.getSearchImageIndexName(),
                        config.getSearchImageTypeName()
                )
                .setSource(elasticUtils.writeAsJsonBytes(searchImage), XContentType.JSON)
                .get();
    }

    public void delete(long userImageId){
        //TODO STUB!
        final SearchResponse response = searchUniqueByField(
                config.getSearchImageIndexName(),
                config.getSearchImageTypeName(),
                ImageField.ID.getFieldName(),
                userImageId
        );

        final String elasticId = elasticUtils.parseElasticIdSearchResponse(response);

        elasticClient.prepareDelete(
                config.getSearchImageIndexName(),
                config.getSearchImageTypeName(),
                elasticId
        );
    }

    public void editUserImage(UserImage userImage){
        //TODO STUB!
    }

    public void getUserImages(){
        //TODO STUB!
    }
}
