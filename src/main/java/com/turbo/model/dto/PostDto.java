package com.turbo.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.turbo.model.DeviceType;
import com.turbo.model.Post;
import com.turbo.util.EncryptionService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by rakhmetov on 06.06.17.
 */
public class PostDto {

    private String id;
    private String name;
    private long ups;
    private long downs;
    private long rating;
    private long views;
    private Set<UserImageDto> images;
    private DeviceType deviceType;
    private Set<String> tags;
    private LocalDateTime createDate;
    private boolean visible;

    public PostDto(String id, String name, long ups, long downs, long rating, long views, Set<UserImageDto> images, DeviceType deviceType, Set<String> tags, LocalDateTime createDate, boolean visible) {
        this.id = id;
        this.name = name;
        this.ups = ups;
        this.downs = downs;
        this.rating = rating;
        this.views = views;
        this.images = images;
        this.deviceType = deviceType;
        this.tags = tags;
        this.createDate = createDate;
        this.visible = visible;
    }

    @JsonIgnore
    public static PostDto from(Post post) {
        Set<UserImageDto> userImageDtos = post.getImages().stream()
                .map(UserImageDto::from)
                .collect(Collectors.toSet());

        return new PostDto(
                EncryptionService.encodeHashId(post.getId()),
                post.getName(),
                post.getUps(),
                post.getDowns(),
                post.getRating(),
                post.getViews(),
                userImageDtos,
                post.getDeviceType(),
                post.getTags(),
                post.getCreateDate(),
                post.isVisible()
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
    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public boolean isVisible() {
        return visible;
    }
}

