package com.turbo.model.search.content;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.turbo.model.DeviceType;

import java.time.LocalDateTime;
import java.util.List;

import static com.google.common.base.MoreObjects.firstNonNull;

/**
 * Created by ermolaev on 5/27/17.
 * Needs for send enough data in search engine
 */
public class PostContentEntity {
    private Long id;
    private String name;
    private String description;
    private DeviceType deviceType;
    private List<String> tags;
    private String authorId;
    private Long ups;
    private Long downs;
    private Long rating;
    private Long views;
    private LocalDateTime createDate;

    public PostContentEntity(
            @JsonProperty("id") Long id,
            @JsonProperty("name") String name,
            @JsonProperty("description") String description,
            @JsonProperty("device_type") DeviceType deviceType,
            @JsonProperty("tags") List<String> tags,
            @JsonProperty("author_id") String authorId,
            @JsonProperty("ups") Long ups,
            @JsonProperty("downs") Long downs,
            @JsonProperty("rating") Long rating,
            @JsonProperty("views") Long views,
            @JsonProperty("create_date") @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm") LocalDateTime createDate
    ) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.deviceType = deviceType;
        this.tags = tags;
        this.authorId = authorId;
        this.ups = ups;
        this.downs = downs;
        this.rating = rating;
        this.views = views;
        this.createDate = createDate;
    }

    @JsonProperty("id")
    public Long getId() {
        return id;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("description")
    public String getDescription() {
        return description;
    }

    @JsonProperty("device_type")
    public DeviceType getDeviceType() {
        return deviceType;
    }

    @JsonProperty("tags")
    public List<String> getTags() {
        return tags;
    }

    @JsonProperty("author_id")
    public String getAuthorId() {
        return authorId;
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

    @JsonProperty("create_date")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    public LocalDateTime getCreateDate() {
        return createDate;
    }
}
