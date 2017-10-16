package com.turbo.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.turbo.model.DeviceType;
import com.turbo.model.Post;
import com.turbo.model.User;
import com.turbo.model.UserImage;
import com.turbo.util.EncryptionService;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.ObjectUtils.firstNonNull;

public class PostViewDto {
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
    private UserDto user;

    private PostViewDto(
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
            UserDto user
    ) {
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
        this.user = user;
        this.description = description;
    }

    @JsonProperty("id")
    public String getId() {
        return id;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("ups")
    public long getUps() {
        return ups;
    }

    @JsonProperty("downs")
    public long getDowns() {
        return downs;
    }

    @JsonProperty("rating")
    public long getRating() {
        return rating;
    }

    @JsonProperty("views")
    public long getViews() {
        return views;
    }

    @JsonProperty("images")
    public Set<UserImageDto> getImages() {
        return images;
    }

    @JsonProperty("device_type")
    public DeviceType getDeviceType() {
        return deviceType;
    }

    @JsonProperty("tags")
    public Set<String> getTags() {
        return tags;
    }

    @JsonProperty("create_date") @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    public LocalDateTime getCreateDate() {
        return createDate;
    }

    @JsonProperty("is_visible")
    public boolean isVisible() {
        return visible;
    }

    @JsonProperty("user")
    public UserDto getUser() {
        return user;
    }

    @JsonProperty("description")
    public String getDescription() {
        return description;
    }

    public static PostViewDto from(Post that) {
       return new PostViewDto(
               EncryptionService.encodeHashId(that.getId()),
               that.getName(),
               that.getDescription(),
               that.getUps(),
               that.getDowns(),
               that.getRating(),
               that.getViews(),
               that.getImages() == null ?
                       Collections.EMPTY_SET :
                       that.getImages().keySet()
                               .stream()
                               .map(UserImageDto::from)
                               .collect(Collectors.toSet()),
               that.getDeviceType(),
               that.getTags() == null ? Collections.emptySet() : Collections.unmodifiableSet(that.getTags()),
               firstNonNull(that.getCreateDate(), LocalDateTime.now()),
               that.isVisible(),
               UserDto.from(that.getUser())
       );
    }
}
