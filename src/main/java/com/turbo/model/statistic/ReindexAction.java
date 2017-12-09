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
        @JsonSubTypes.Type(value = ReindexPostContent.class, name = ActionType.UPDATE_POST_CONTENT),
        @JsonSubTypes.Type(value = ReindexPostRating.class, name = ActionType.UPDATE_POST_RATING),
        @JsonSubTypes.Type(value = ReindexPostRating.class, name = ActionType.UPDATE_POST_VIEWS),
        @JsonSubTypes.Type(value = ReindexCommentContent.class, name = ActionType.UPDATE_COMMENT_CONTENT),
        @JsonSubTypes.Type(value = ReindexCommentRating.class, name = ActionType.UPDATE_COMMENT_RATING),
        @JsonSubTypes.Type(value = ReindexImageContent.class, name = ActionType.UPDATE_IMAGE_CONTENT),
        @JsonSubTypes.Type(value = ReindexDeleteComment.class, name = ActionType.DELETE_COMMENT),
        @JsonSubTypes.Type(value = ReindexDeleteImage.class, name = ActionType.DELETE_IMAGE),
        @JsonSubTypes.Type(value = ReindexDeletePost.class, name = ActionType.DELETE_POST),
})
public abstract class ReindexAction {
    private long id;

    public abstract ReindexAction merge(ReindexAction newValue);
    public abstract String getType();

    public <T> T getOriginalValue() {
        return (T)this;
    }
}
