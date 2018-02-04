package com.turbo.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.turbo.model.Comment;
import com.turbo.model.DeviceType;
import com.turbo.model.Rating;
import com.turbo.util.EncryptionService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Getter
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {

    private String id;
    private String userId;
    private String userName;
    private String replyId;
    private DeviceType device; // from what was posted
    private String content;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private LocalDateTime createDate;
    private Rating rating;


    public static CommentDto from(Comment comment) {
        return new CommentDto(
                EncryptionService.encodeHashId(comment.getId()),
                EncryptionService.encodeHashId(comment.getUser().getId()),
                comment.getUser().getName(),
                EncryptionService.encodeHashId(comment.getReplyId()),
                comment.getDevice(),
                comment.getContent(),
                comment.getCreationDate(),
                comment.getRating()
        );
    }

    public static Map<String, CommentDto> from(Map<Long, Comment> comments) {
        if(Objects.isNull(comments)) {
            return Collections.emptyMap();
        }
        return comments.entrySet().stream().collect(
                Collectors.toMap(
                        entry -> EncryptionService.encodeHashId(entry.getKey()),
                        entry -> from(entry.getValue()))
        );
    }
}
