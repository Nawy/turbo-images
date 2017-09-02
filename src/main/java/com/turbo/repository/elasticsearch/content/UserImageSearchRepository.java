package com.turbo.repository.elasticsearch.content;

import com.turbo.config.ElasticsearchConfig;
import com.turbo.model.UserImage;
import com.turbo.model.search.content.ImageSearchEntity;
import com.turbo.model.search.field.ImageField;
import com.turbo.repository.elasticsearch.AbstractSearchRepository;
import com.turbo.repository.elasticsearch.ElasticId;
import com.turbo.util.ElasticUtils;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by ermolaev on 6/24/17.
 */
@Repository
public class UserImageSearchRepository extends AbstractSearchRepository {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(ImageSearchEntity.CREATION_DATE_PATTERN);

    @Autowired
    public UserImageSearchRepository(ElasticsearchConfig config, ElasticUtils elasticUtils) {
        super(config.getElasticClient(), config, elasticUtils);
    }

    public void addUserImage(final UserImage image) {
        ImageSearchEntity searchImage = ImageSearchEntity.from(image);

        elasticClient
                .prepareIndex(
                        config.getSearchImageIndexName(),
                        config.getSearchImageTypeName()
                )
                .setSource(elasticUtils.writeAsJsonBytes(searchImage), XContentType.JSON)
                .get();
    }

    public void delete(long userImageId){
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

    public void updateUserImage(UserImage image){
        //FIXME I'm not working!!
        /*ImageSearchEntity searchImage = ImageSearchEntity.from(image);

        final SearchResponse response = searchUniqueByField(
                config.getSearchImageIndexName(),
                config.getSearchImageTypeName(),
                ImageField.ID.getFieldName(),
                image.getId()
        );

        final String elasticId = elasticUtils.parseElasticIdSearchResponse(response);
        elasticClient.prepareUpdate(
                config.getSearchImageIndexName(),
                config.getSearchImageTypeName(),
                elasticId
        ).setDoc(searchImage).get();*/
    }

    public List<Long> getUserImages(final Long userId, final LocalDateTime lastDate, final int pageSize){
        SearchResponse response = elasticClient
                .prepareSearch(config.getSearchImageIndexName())
                .setTypes(config.getSearchImageTypeName())
                .addSort(ImageField.CREATION_DATE.getFieldName(), SortOrder.DESC)
                .setQuery(
                        QueryBuilders.boolQuery()
                            .must(
                                    QueryBuilders.termQuery(ImageField.USER_ID.getFieldName(), userId)
                            )
                            .must(
                                    QueryBuilders.rangeQuery(ImageField.CREATION_DATE.getFieldName()).lte(formatter.format(lastDate))
                            )
                )
                .setSize(pageSize)
                .get();

        return elasticUtils.parseSearchResponse(response, ElasticId.class)
                .stream()
                .map(ElasticId::getId)
                .collect(Collectors.toList());
    }
}
