package com.turbo.model.search;

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
public class PostSearchEntity {
    private Long id;
    private String name;
    private String description;
    private long ups;
    private long downs;
    private long viewCount;
    private String previewPath;
    private DeviceType deviceType;
    private List<String> tags;
    private String authorId;
    private LocalDateTime createDate;
    private boolean visible;

    public PostSearchEntity(
            @JsonProperty(value = "id") Long id,
            @JsonProperty(value = "name", required = true) String name,
            @JsonProperty(value = "description", required = true) String description,
            @JsonProperty(value = "ups", required = true) long ups,
            @JsonProperty(value = "downs", required = true) long downs,
            @JsonProperty(value = "view_count", required = true) long viewCount,
            @JsonProperty(value = "preview_path", required = true) String previewPath,
            @JsonProperty(value = "client_type", required = true) DeviceType deviceType,
            @JsonProperty(value = "tags", required = true) List<String> tags,
            @JsonProperty(value = "author_id", required = true) String authorId,
            @JsonProperty("create_date") @JsonFormat(pattern = "yyyy-MM-dd HH:mm") LocalDateTime createDate,
            @JsonProperty(value = "visible") boolean visible
    ) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.ups = ups;
        this.downs = downs;
        this.viewCount = viewCount;
        this.previewPath = previewPath;
        this.deviceType = deviceType;
        this.tags = tags;
        this.authorId = authorId;
        this.createDate = firstNonNull(createDate, LocalDateTime.now());
        this.visible = visible;
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

    @JsonProperty("ups")
    public long getUps() {
        return ups;
    }

    @JsonProperty("downs")
    public long getDowns() {
        return downs;
    }

    @JsonProperty("view_count")
    public long getViewCount() {
        return viewCount;
    }

    @JsonProperty("preview_path")
    public String getPreviewPath() {
        return previewPath;
    }

    @JsonProperty("client_type")
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

    @JsonProperty("visible")
    public boolean isVisible() {
        return visible;
    }

    @JsonProperty("create_date")
    public LocalDateTime getCreateDate() {
        return createDate;
    }

}
