package com.turbo.repository.elasticsearch.content;

import com.turbo.config.ElasticsearchConfig;
import com.turbo.model.aerospike.CommentRepoModel;
import com.turbo.model.comment.Comment;
import com.turbo.model.comment.CommentReplyType;
import com.turbo.model.exception.InternalServerErrorHttpException;
import com.turbo.model.search.SearchOrder;
import com.turbo.model.search.content.CommentSearchEntity;
import com.turbo.model.search.field.CommentField;
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
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Repository
public class CommentSearchRepository extends AbstractSearchRepository {

    @Autowired
    public CommentSearchRepository(ElasticsearchConfig config, ElasticUtils elasticUtils) {
        super(config.getElasticClient(), config, elasticUtils);
    }

    public void add(final CommentRepoModel comment) {
        elasticClient
                .prepareIndex(
                        config.getSearchCommentIndexName(),
                        config.getSearchCommentTypeName()
                )
                .setSource(elasticUtils.writeAsJsonBytes(new CommentSearchEntity(comment)), XContentType.JSON)
                .get();
    }

    public List<Long> getByReply(
            final long replyId,
            final CommentReplyType replyType,
            final int pageSize,
            final CommentField commentField,
            final SearchOrder searchOrder
    ) {
        SearchResponse response = elasticClient
                .prepareSearch(config.getSearchCommentIndexName())
                .setTypes(config.getSearchCommentTypeName())
                .addSort(
                        commentField.getFieldName(),
                        searchOrder == SearchOrder.DESC ? SortOrder.DESC : SortOrder.ASC
                )
                .setQuery(
                        QueryBuilders.boolQuery()
                                .must(
                                        QueryBuilders.termQuery(CommentField.REPLY_ID.getFieldName(), replyId)
                                )
                                .must(
                                        QueryBuilders.matchQuery(CommentField.REPLY_TYPE.getFieldName(), replyType.toString())
                                )
                )
                .setSize(pageSize)
                .get();

        return elasticUtils.parseSearchResponse(response, ElasticId.class)
                .stream()
                .map(ElasticId::getId)
                .collect(Collectors.toList());
    }

    public void update(final CommentRepoModel comment) {
        Objects.requireNonNull(comment.getId(), "for update comment you need have id for update");
        update(new CommentSearchEntity(comment));
    }

    public void update(final CommentSearchEntity comment){
        final String elasticId = getElasticId(comment.getId());
        elasticClient.prepareUpdate(
                config.getSearchCommentIndexName(),
                config.getSearchCommentTypeName(),
                elasticId
        ).setDoc(
                elasticUtils.writeAsJsonBytes(comment),
                XContentType.JSON
        ).setRetryOnConflict(5).get();
    }

    public CommentSearchEntity get(final long id) {
        SearchResponse response = elasticClient
                .prepareSearch(config.getSearchCommentIndexName())
                .setTypes(config.getSearchCommentTypeName())
                .addSort(CommentField.CREATION_DATE.getFieldName(), SortOrder.DESC)
                .setQuery(
                        QueryBuilders.boolQuery()
                                .must(
                                        QueryBuilders.matchQuery(CommentField.ID.getFieldName(), id)
                                )
                )
                .setSize(1)
                .get();

        return elasticUtils.parseUniqueSearchResponse(response, CommentSearchEntity.class);
    }

    private String getElasticId(final Long id) {
        final SearchResponse response = searchUniqueByField(
                config.getSearchCommentIndexName(),
                config.getSearchCommentTypeName(),
                CommentField.ID.getFieldName(),
                id
        );
        if (response.getHits().getTotalHits() <= 0) {
            throw new InternalServerErrorHttpException("Not found comment by id=" + id);
        }
        return elasticUtils.parseElasticIdSearchResponse(response);
    }

    public List<Long> get(final LocalDateTime lastDate, final int pageSize) {
        SearchResponse response = elasticClient
                .prepareSearch(config.getSearchCommentIndexName())
                .setTypes(config.getSearchCommentTypeName())
                .addSort(CommentField.CREATION_DATE.getFieldName(), SortOrder.DESC)
                .setQuery(
                        QueryBuilders.boolQuery()
                                .must(
                                        QueryBuilders.rangeQuery(CommentField.CREATION_DATE.getFieldName()).lte(formatter.format(lastDate))
                                )
                )
                .setSize(pageSize)
                .get();

        return elasticUtils.parseSearchResponse(response, ElasticId.class)
                .stream()
                .map(ElasticId::getId)
                .collect(Collectors.toList());
    }

    public void delete(final Long id) {
        final String elasticId = getElasticId(id);
        elasticClient.prepareDelete(
                config.getSearchCommentIndexName(),
                config.getSearchCommentTypeName(),
                elasticId
        );
    }
}
