package com.turbo.model.search.content;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.turbo.model.DeviceType;
import com.turbo.model.comment.Comment;
import com.turbo.model.comment.CommentReplyType;
import com.turbo.model.search.field.CommentFieldNames;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * Created by ermolaev on 5/27/17.
 * Needs for send enough data in search engine
 */
@Getter
public class CommentSearchEntity {

    public final static String CREATION_DATE_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSS";

    @JsonProperty(CommentFieldNames.ID)
    private final long id;
    @JsonProperty(CommentFieldNames.USER_ID)
    private final long userId;

    @JsonProperty(CommentFieldNames.REPLY_ID)
    private final long replyId;
    @JsonProperty(CommentFieldNames.REPLY_TYPE)
    private final CommentReplyType replyType;

    @JsonProperty(CommentFieldNames.DEVICE)
    private final DeviceType device; // from what was posted
    @JsonProperty(CommentFieldNames.CONTENT)
    private final String content;

    @JsonProperty(CommentFieldNames.CREATION_DATE)
    @JsonFormat(pattern = CREATION_DATE_PATTERN)
    private final LocalDateTime creationDate;

    @JsonProperty(CommentFieldNames.UPS)
    private final long ups;
    @JsonProperty(CommentFieldNames.DOWNS)
    private final long downs;
    @JsonProperty(CommentFieldNames.RATING)
    private final long rating;

    public CommentSearchEntity(
            @JsonProperty(CommentFieldNames.ID) long id,
            @JsonProperty(CommentFieldNames.USER_ID) long userId,
            @JsonProperty(CommentFieldNames.REPLY_ID) long replyId,
            @JsonProperty(CommentFieldNames.REPLY_TYPE) CommentReplyType replyType,
            @JsonProperty(CommentFieldNames.DEVICE) DeviceType device,
            @JsonProperty(CommentFieldNames.CONTENT) String content,
            @JsonProperty(CommentFieldNames.CREATION_DATE) @JsonFormat(pattern = CREATION_DATE_PATTERN) LocalDateTime creationDate,
            @JsonProperty(CommentFieldNames.UPS) long ups,
            @JsonProperty(CommentFieldNames.DOWNS) long downs,
            @JsonProperty(CommentFieldNames.RATING) long rating
    ) {
        this.id = id;
        this.userId = userId;
        this.replyId = replyId;
        this.replyType = replyType;
        this.device = device;
        this.content = content;
        this.creationDate = creationDate;
        this.ups = ups;
        this.downs = downs;
        this.rating = rating;
    }

    public CommentSearchEntity(final Comment comment) {
        this.id = comment.getId();
        this.userId = comment.getUser().getId();
        this.replyId = comment.getReplyId();
        this.replyType = comment.getReplyType();
        this.device = comment.getDevice();
        this.content = comment.getContent();
        this.creationDate = comment.getCreationDate();
        this.ups = comment.getUps();
        this.downs = comment.getDowns();
        this.rating = comment.getRating();
    }
}
