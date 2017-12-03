package com.turbo.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.turbo.model.DeviceType;
import com.turbo.model.Post;
import com.turbo.model.UserImage;
import com.turbo.model.exception.NotFoundHttpException;
import com.turbo.util.EncryptionService;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by rakhmetov on 06.06.17.
 */
public class PostDto {

    private String id;
    private String name;
    private String description;
    private long ups;
    private long downs;
    private long rating;
    private long views;
    private Set<UserImageDto> images;
    private DeviceType deviceType;
    private Set<String> tags;
    private LocalDateTime createDate;
    private boolean visible;

    private String userId;
    private String userName;

    public PostDto(
            String id,
            String name,
            String description,
            long ups,
            long downs,
            long rating,
            long views,
            Set<UserImageDto> images,
            DeviceType deviceType,
            Set<String> tags,
            LocalDateTime createDate,
            boolean visible,
            String userId,
            String userName
    ) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.ups = ups;
        this.downs = downs;
        this.rating = rating;
        this.views = views;
        this.images = images;
        this.deviceType = deviceType;
        this.tags = tags;
        this.createDate = createDate;
        this.visible = visible;
        this.userId = userId;
        this.userName = userName;
    }

    @JsonIgnore
    public static PostDto from(Post post) {
        Set<UserImageDto> userImageDtos = post.getImages().stream()
                .map(UserImageDto::from)
                .collect(Collectors.toSet());

        return new PostDto(
                EncryptionService.encodeHashId(post.getId()),
                post.getName(),
                post.getDescription(),
                post.getUps(),
                post.getDowns(),
                post.getRating(),
                post.getViews(),
                userImageDtos,
                post.getDeviceType(),
                post.getTags(),
                post.getCreateDate(),
                post.isVisible(),
                EncryptionService.encodeHashId(post.getUser().getId()),
                post.getUser().getName()
        );
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public long getUps() {
        return ups;
    }

    public long getDowns() {
        return downs;
    }

    public long getRating() {
        return rating;
    }

    public long getViews() {
        return views;
    }

    public Set<UserImageDto> getImages() {
        return images;
    }

    @JsonProperty("device_type")
    public DeviceType getDeviceType() {
        return deviceType;
    }

    public Set<String> getTags() {
        return tags;
    }

    @JsonProperty("create_date")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    public LocalDateTime getCreateDate() {
        return createDate;
    }

    @JsonProperty("visible")
    public boolean isVisible() {
        return visible;
    }

    public String getDescription() {
        return description;
    }

    @JsonProperty("user_id")
    public String getUserId() {
        return userId;
    }

    @JsonProperty("user_name")
    public String getUserName() {
        return userName;
    }
}

