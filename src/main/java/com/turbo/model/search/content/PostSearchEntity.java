package com.turbo.model.search.content;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.turbo.model.DeviceType;
import com.turbo.model.Post;
import com.turbo.model.UserImage;
import com.turbo.model.search.field.PostFieldNames;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by ermolaev on 5/27/17.
 * Needs for send enough data in search engine
 */
public class PostSearchEntity {

    public final static String CREATION_DATE_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSS";

    private long id;
    private String name;
    private long ups;
    private long downs;
    private long rating;
    private long views;
    private DeviceType deviceType;
    private List<String> descriptions;
    private List<Long> imageIds;
    private Set<String> tags;
    @JsonFormat(pattern = CREATION_DATE_PATTERN)
    private LocalDateTime creationDate;
    private long userId;
    private boolean isVisible;

    public PostSearchEntity(
            @JsonProperty(PostFieldNames.ID) Long id,
            @JsonProperty(PostFieldNames.NAME) String name,
            @JsonProperty(PostFieldNames.DESCRIPTIONS) List<String> descriptions,
            @JsonProperty(PostFieldNames.DEVICE_TYPE) DeviceType deviceType,
            @JsonProperty(PostFieldNames.IMAGE_IDS) List<Long> imageIds,
            @JsonProperty(PostFieldNames.TAGS) Set<String> tags,
            @JsonProperty(PostFieldNames.USER_ID) long userId,
            @JsonProperty(PostFieldNames.UPS) Long ups,
            @JsonProperty(PostFieldNames.DOWNS) Long downs,
            @JsonProperty(PostFieldNames.RATING) Long rating,
            @JsonProperty(PostFieldNames.VIEWS) Long views,
            @JsonProperty(PostFieldNames.CREATION_DATE) @JsonFormat(pattern = CREATION_DATE_PATTERN) LocalDateTime creationDate,
            @JsonProperty(PostFieldNames.VISIBLE) boolean isVisible
    ) {
        this.id = id;
        this.name = name;
        this.descriptions = descriptions;
        this.deviceType = deviceType;
        this.imageIds = imageIds;
        this.tags = tags;
        this.userId = userId;
        this.ups = ups;
        this.downs = downs;
        this.rating = rating;
        this.views = views;
        this.creationDate = creationDate;
        this.isVisible = isVisible;
    }

    public PostSearchEntity(final Post post) {
        this.id = post.getId();
        this.name = post.getName();
        this.descriptions = post.getImages().stream().map(UserImage::getDescription).collect(Collectors.toList());
        this.descriptions.add(post.getDescription());
        this.imageIds = post.getImages().stream().map(UserImage::getId).collect(Collectors.toList());
        this.deviceType = post.getDeviceType();
        this.tags = post.getTags();
        this.userId = post.getUser().getId();
        this.ups = post.getRating().getUps();
        this.downs = post.getRating().getDowns();
        this.rating = post.getRating().getRating();
        this.views = post.getViews();
        this.creationDate = post.getCreateDate();
        this.isVisible = post.isVisible();
    }

    @JsonProperty(PostFieldNames.ID)
    public Long getId() {
        return id;
    }

    @JsonProperty(PostFieldNames.NAME)
    public String getName() {
        return name;
    }

    @JsonProperty(PostFieldNames.DESCRIPTIONS)
    public List<String> getDescriptions() {
        return descriptions;
    }

    @JsonProperty(PostFieldNames.DEVICE_TYPE)
    public DeviceType getDeviceType() {
        return deviceType;
    }

    @JsonProperty(PostFieldNames.TAGS)
    public Set<String> getTags() {
        return tags;
    }

    @JsonProperty(PostFieldNames.USER_ID)
    public long getUserId() {
        return userId;
    }

    @JsonProperty(PostFieldNames.UPS)
    public Long getUps() {
        return ups;
    }

    @JsonProperty(PostFieldNames.DOWNS)
    public Long getDowns() {
        return downs;
    }

    @JsonProperty(PostFieldNames.RATING)
    public Long getRating() {
        return rating;
    }

    @JsonProperty(PostFieldNames.VIEWS)
    public Long getViews() {
        return views;
    }

    @JsonProperty(PostFieldNames.IMAGE_IDS)
    public List<Long> getImageIds() {
        return imageIds;
    }

    @JsonProperty(PostFieldNames.CREATION_DATE)
    @JsonFormat(pattern = CREATION_DATE_PATTERN)
    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    @JsonProperty(PostFieldNames.VISIBLE)
    public boolean isVisible() {
        return isVisible;
    }
}
