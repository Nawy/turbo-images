package com.turbo.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.turbo.model.DeviceType;
import com.turbo.model.comment.Comment;
import com.turbo.model.comment.CommentReplyType;
import com.turbo.util.EncryptionService;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@AllArgsConstructor
public class CommentDto {

    private String id;
    private String userId;
    private String userName;
    private String replyId;
    private CommentReplyType replyType;
    private DeviceType device; // from what was posted
    private String content;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private LocalDateTime postTime;
    private long ups;
    private long downs;
    private long rating; // upvotes - downvotes

    private long repliesAmount;

    public static CommentDto from(Comment comment) {
        return new CommentDto(
                EncryptionService.encodeHashId(comment.getId()),
                EncryptionService.encodeHashId(comment.getUser().getId()),
                comment.getUser().getName(),
                EncryptionService.encodeHashId(comment.getReplyId()),
                comment.getReplyType(),
                comment.getDevice(),
                comment.getContent(),
                comment.getCreationDate(),
                comment.getUps(),
                comment.getDowns(),
                comment.getRating(),
                comment.getRepliesAmount()
        );
    }

    public static List<CommentDto> from(List<Comment> comments){
        return comments.stream().map(CommentDto::from).collect(Collectors.toList());
    }
}
