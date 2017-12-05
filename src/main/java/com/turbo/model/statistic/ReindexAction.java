package com.turbo.model.statistic;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = ReindexPostContent.class, name = ActionType.POST_CONTENT),
        @JsonSubTypes.Type(value = ReindexPostRating.class, name = ActionType.POST_RATING),
        @JsonSubTypes.Type(value = ReindexPostRating.class, name = ActionType.POST_VIEWS),
        @JsonSubTypes.Type(value = ReindexCommentContent.class, name = ActionType.COMMENT_CONTENT),
        @JsonSubTypes.Type(value = ReindexCommentRating.class, name = ActionType.COMMENT_RATING),
        @JsonSubTypes.Type(value = ReindexImageContent.class, name = ActionType.IMAGE_CONTENT),
})
public abstract class ReindexAction {
    private long id;

    public abstract ReindexAction merge(ReindexAction newValue);
    public abstract String getType();
}
