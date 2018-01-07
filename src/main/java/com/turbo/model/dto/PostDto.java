package com.turbo.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.turbo.model.Comment;
import com.turbo.model.DeviceType;
import com.turbo.model.Post;
import com.turbo.model.Rating;
import com.turbo.util.EncryptionService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by rakhmetov on 06.06.17.
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class PostDto {

    private String id;
    private String name;
    private String description;
    private Rating rating;
    private long views;

    private Set<UserImageDto> images;

    private DeviceType deviceType;
    private Set<String> tags;
    private LocalDateTime createDate;
    private boolean visible;

    private String userId;
    private String userName;

    private List<CommentDto> comments;

    @JsonIgnore
    public static PostDto from(Post post) {
        Set<UserImageDto> userImageDtos = post.getImages().stream()
                .map(UserImageDto::from)
                .collect(Collectors.toSet());

        Map<Long, List<Comment>> commentsGrouppedByReply = post.getComments().values().stream().collect(Collectors.groupingBy(Comment::getReplyId));
        List<CommentDto> comments = CommentDto.from(
                commentsGrouppedByReply.get(null), // if replyId is null it's Post root comment
                commentsGrouppedByReply
        );

        return new PostDto(
                EncryptionService.encodeHashId(post.getId()),
                post.getName(),
                post.getDescription(),
                post.getRating(),
                post.getViews(),
                userImageDtos,
                post.getDeviceType(),
                post.getTags(),
                post.getCreateDate(),
                post.isVisible(),
                EncryptionService.encodeHashId(post.getUser().getId()),
                post.getUser().getName(),
                comments
        );
    }

    @JsonProperty("create_date")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    public LocalDateTime getCreateDate() {
        return createDate;
    }

}

