package com.turbo.model.search.content;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.turbo.model.DeviceType;
import com.turbo.model.Post;
import com.turbo.model.UserImage;
import com.turbo.model.aerospike.PostRepoModel;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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
    private List<String> tags;
    private LocalDateTime creationDate;
    private long userId;

    public PostSearchEntity(
            @JsonProperty("id") Long id,
            @JsonProperty("name") String name,
            @JsonProperty("descriptions") List<String> descriptions,
            @JsonProperty("device_type") DeviceType deviceType,
            @JsonProperty("image_ids") List<Long> imageIds,
            @JsonProperty("tags") List<String> tags,
            @JsonProperty("user_id") long userId,
            @JsonProperty("ups") Long ups,
            @JsonProperty("downs") Long downs,
            @JsonProperty("rating") Long rating,
            @JsonProperty("views") Long views,
            @JsonProperty("create_date") @JsonFormat(pattern = CREATION_DATE_PATTERN) LocalDateTime creationDate
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

    @JsonProperty("id")
    public Long getId() {
        return id;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("descriptions")
    public List<String> getDescriptions() {
        return descriptions;
    }

    @JsonProperty("device_type")
    public DeviceType getDeviceType() {
        return deviceType;
    }

    @JsonProperty("tags")
    public List<String> getTags() {
        return tags;
    }

    @JsonProperty("user_id")
    public long getUserId() {
        return userId;
    }

    @JsonProperty("ups")
    public Long getUps() {
        return ups;
    }

    @JsonProperty("downs")
    public Long getDowns() {
        return downs;
    }

    @JsonProperty("rating")
    public Long getRating() {
        return rating;
    }

    @JsonProperty("views")
    public Long getViews() {
        return views;
    }

    @JsonProperty("image_ids")
    public List<Long> getImageIds() {
        return imageIds;
    }

    @JsonProperty("create_date")
    @JsonFormat(pattern = CREATION_DATE_PATTERN)
    public LocalDateTime getCreationDate() {
        return creationDate;
    }
}
