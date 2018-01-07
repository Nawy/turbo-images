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
import java.util.List;
import java.util.Map;
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
    private LocalDateTime postTime;
    private Rating rating;
    private List<CommentDto> commentDtos;

    public static CommentDto from(Comment comment, Map<Long, List<Comment>> allComments) {
        return new CommentDto(
                EncryptionService.encodeHashId(comment.getId()),
                EncryptionService.encodeHashId(comment.getUser().getId()),
                comment.getUser().getName(),
                EncryptionService.encodeHashId(comment.getReplyId()),
                comment.getDevice(),
                comment.getContent(),
                comment.getCreationDate(),
                comment.getRating(),
                from(
                        allComments.get(comment.getId()),
                        allComments
                )
        );
    }

    public static List<CommentDto> from(List<Comment> comments, Map<Long, List<Comment>> allComments){
        return comments.stream().map(comment -> from(comment, allComments)).collect(Collectors.toList());
    }
}
