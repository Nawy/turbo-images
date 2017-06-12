package com.turbo.repository.elasticsearch.content;

import com.turbo.config.ElasticsearchConfig;
import com.turbo.model.Nullable;
import com.turbo.model.Post;
import com.turbo.model.exception.InternalServerErrorHttpException;
import com.turbo.model.page.Page;
import com.turbo.model.search.SearchOrder;
import com.turbo.model.search.SearchPeriod;
import com.turbo.model.search.content.PostSearchEntity;
import com.turbo.model.search.field.PostField;
import com.turbo.repository.elasticsearch.AbstractSearchRepository;
import com.turbo.repository.elasticsearch.ElasticId;
import com.turbo.repository.util.ElasticUtils;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Created by ermolaev on 5/15/17.
 */
@Repository
public class PostSearchRepository extends AbstractSearchRepository {

    @Autowired
    public PostSearchRepository(ElasticsearchConfig config) {
        super(config.getElasticClient(), config);
    }

    /**
     * Add new post to search engine
     * @param post
     * @return
     */
    public void addPost(final Post post) {
        elasticClient
                .prepareIndex(
                        config.getSearchPostIndexName(),
                        config.getSearchPostTypeName()
                )
                .setSource(ElasticUtils.writeAsJsonBytes(new PostSearchEntity(post)), XContentType.JSON)
                .get();
    }

    public void updatePost(final Post post) {
        Objects.requireNonNull(post.getId(), "for update post you need have id for update");
        final String elasticId = getPostElasticId(post.getId());
        elasticClient.prepareUpdate(
                config.getSearchPostIndexName(),
                config.getSearchPostTypeName(),
                elasticId
        ).setDoc(
                ElasticUtils.writeAsJsonBytes(new PostSearchEntity(post)),
                XContentType.JSON
        ).setRetryOnConflict(5).get();
    }

    public String getPostElasticId(final Long id) {
        final SearchResponse response = searchUniqueByField(
                config.getSearchPostIndexName(),
                config.getSearchPostTypeName(),
                PostField.ID.getFieldName(),
                id
        );
        if(response.getHits().getTotalHits() <= 0) {
            throw new InternalServerErrorHttpException("Not found post by id=" + id);
        }
        return ElasticUtils.parseElasticIdSearchResponse(response);
    }

    /**
     * Find post by id (not search id)
     * @param id
     * @return
     */
    public PostSearchEntity getPostById(final Long id) {
        return ElasticUtils.parseUniqueSearchResponse(
                searchUniqueByField(
                        config.getSearchPostIndexName(),
                        config.getSearchPostTypeName(),
                        PostField.ID.getFieldName(),
                        id
                ),
                PostSearchEntity.class
        );
    }

    public List<Long> getPostByAuthor(
            final String authorId,
            final int page,
            final SearchPeriod period,
            @Nullable final PostField postField,
            @Nullable final SearchOrder searchOrder
    ) {
        final Page queryPage = new Page(page);
        LocalDate lowestDate = null;
        if(period != SearchPeriod.ALL_TIME) {
            switch (period) {
                case DAY:{
                    lowestDate = LocalDate.now().minusDays(1);
                    break;
                }
                case WEEK:{
                    lowestDate = LocalDate.now().minusWeeks(1);
                    break;
                }
                case MONTH:{
                    lowestDate = LocalDate.now().minusMonths(1);
                    break;
                }
                case YEAR:{
                    lowestDate = LocalDate.now().minusYears(1);
                    break;
                }
            }
        }

        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery()
                .must(
                        QueryBuilders.termQuery(
                                PostField.AUTHOR.getFieldName(),
                                authorId
                        )
                );

        if(Objects.nonNull(lowestDate)) {
            boolQueryBuilder.must(
                    QueryBuilders.rangeQuery(
                            PostField.CREATION_DATE.getFieldName()
                    ).gte(lowestDate)
            );
        }

        SearchRequestBuilder request = elasticClient
                .prepareSearch(config.getSearchPostIndexName())
                .setTypes(config.getSearchPostTypeName())
                .setQuery(boolQueryBuilder)
                .setFrom(queryPage.getOffset())
                .setSize(queryPage.getSize());

        if(Objects.nonNull(postField) && Objects.nonNull(searchOrder)) {
            request.addSort(
                    SortBuilders
                            .fieldSort(postField.getFieldName())
                            .order(
                                    searchOrder == SearchOrder.DESC ?
                                            SortOrder.DESC :
                                            SortOrder.ASC
                            )
            );
        }

        return ElasticUtils.parseSearchResponse(request.get(), ElasticId.class)
                .stream()
                .map(ElasticId::getId)
                .collect(Collectors.toList());
    }

    /**
     * Find posts by name
     * Throws Exception if page size more than limit {@link ElasticsearchConfig#getMaxSizePostsPerPage()}}
     * @param name
     * @param page
     * @param postField
     * @param searchOrder
     * @return list of post
     */
    public List<Long> findPostByName(
            final String name,
            final int page,
            @Nullable final PostField postField,
            @Nullable final SearchOrder searchOrder
    ) {
        SearchResponse response = searchByField(
                config.getSearchPostIndexName(),
                config.getSearchPostTypeName(),
                PostField.NAME.getFieldName(),
                name,
                page,
                Objects.isNull(postField) ? null : postField.getFieldName(),
                searchOrder
        );

        return ElasticUtils.parseSearchResponse(response, ElasticId.class)
                .stream()
                .map(ElasticId::getId)
                .collect(Collectors.toList());
    }

    /**
     * Find post by description
     * Throws Exception if page size more than limit {@link ElasticsearchConfig#getMaxSizePostsPerPage()}}
     * @param description
     * @param page
     * @param postField
     * @param searchOrder
     * @return list of post
     */
    public List<Long> findPostByDescription(
            final String description,
            final int page,
            @Nullable final PostField postField,
            @Nullable final SearchOrder searchOrder
    ) {

        final SearchResponse response = searchByField(
                config.getSearchPostIndexName(),
                config.getSearchPostTypeName(),
                PostField.DESCRIPTIONS.getFieldName(),
                description,
                page,
                Objects.isNull(postField) ? null : postField.getFieldName(),
                searchOrder
        );

        return ElasticUtils.parseSearchResponse(response, ElasticId.class)
                .stream()
                .map(ElasticId::getId)
                .collect(Collectors.toList());
    }

    public List<Long> findPostByTags(
            final List<String> tags,
            final int page,
            @Nullable final PostField postField,
            @Nullable final SearchOrder searchOrder
    ) {
        final Page queryPage = new Page(page);
        final SearchRequestBuilder request = elasticClient
                .prepareSearch(config.getSearchPostIndexName())
                .setTypes(config.getSearchPostTypeName())
                .setQuery(
                        QueryBuilders.termsQuery(
                                PostField.TAGS.getFieldName(),
                                tags.toArray()
                        )
                )
                .setFrom(queryPage.getOffset())
                .setSize(queryPage.getSize());

        if(Objects.nonNull(postField) && Objects.nonNull(searchOrder)) {
            request.addSort(
                    SortBuilders
                            .fieldSort(postField.getFieldName())
                            .order(
                                    searchOrder == SearchOrder.DESC ?
                                            SortOrder.DESC :
                                            SortOrder.ASC
                            )
            );
        }

        final SearchResponse response = request.get();

        return ElasticUtils.parseSearchResponse(response, ElasticId.class)
                .stream()
                .map(ElasticId::getId)
                .collect(Collectors.toList());
    }

    // TODO made until date
    public List<Long> getNewestPost(
            final int page
    ) {
        final Page queryPage = new Page(page);

        final SearchRequestBuilder request = elasticClient
                .prepareSearch(config.getSearchPostIndexName())
                .setTypes(config.getSearchPostTypeName())
                .setQuery(
                        QueryBuilders.matchAllQuery()
                )
                .addSort(
                        PostField.CREATION_DATE.getFieldName(), SortOrder.DESC
                )
                .setFrom(queryPage.getOffset())
                .setSize(queryPage.getSize());

        final SearchResponse response = request.get();

        return ElasticUtils.parseSearchResponse(response, ElasticId.class)
                .stream()
                .map(ElasticId::getId)
                .collect(Collectors.toList());
    }

    public List<Long> getSortedPost(
            final int page,
            final PostField postField,
            final SearchOrder searchOrder
    ) {
        final Page queryPage = new Page(page);

        final SearchRequestBuilder request = elasticClient
                .prepareSearch(config.getSearchPostIndexName())
                .setTypes(config.getSearchPostTypeName())
                .setQuery(
                        QueryBuilders.matchAllQuery()
                )
                .addSort(
                SortBuilders
                        .fieldSort(postField.getFieldName())
                        .order(
                                searchOrder == SearchOrder.DESC ?
                                        SortOrder.DESC :
                                        SortOrder.ASC
                        )
                )
                .setFrom(queryPage.getOffset())
                .setSize(queryPage.getSize());

        final SearchResponse response = request.get();

        return ElasticUtils.parseSearchResponse(response, ElasticId.class)
                .stream()
                .map(ElasticId::getId)
                .collect(Collectors.toList());
    }

    // static classes

}
