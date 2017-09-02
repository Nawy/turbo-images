package com.turbo.model.search.content;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.turbo.model.DeviceType;
import com.turbo.model.aerospike.PostRepoModel;
import com.turbo.model.search.field.PostFieldNames;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
    private LocalDateTime creationDate;
    private long userId;

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
            @JsonProperty(PostFieldNames.CREATION_DATE) @JsonFormat(pattern = CREATION_DATE_PATTERN) LocalDateTime creationDate
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
    }

    public PostSearchEntity(final PostRepoModel post) {
        this.id = post.getId();
        this.name = post.getName();
        this.descriptions = new ArrayList<>(post.getImages().values());
        this.descriptions.add(post.getDescription());

        this.imageIds = new ArrayList<>(post.getImages().keySet());

        this.deviceType = post.getDeviceType();
        this.tags = post.getTags();
        this.userId = post.getUserId();
        this.ups = post.getUps();
        this.downs = post.getDowns();
        this.rating = post.getRating();
        this.views = post.getViews();
        this.creationDate = post.getCreationDateTime();
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

    @JsonProperty(PostFieldNames.NAME)
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
}
