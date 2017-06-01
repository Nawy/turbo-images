package com.turbo.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.turbo.model.search.SearchIdentifier;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import static org.apache.commons.lang3.ObjectUtils.firstNonNull;

/**
 * Created by rakhmetov on 09.05.17.
 * <p>
 * Just post on site with picture and comments
 */
public class Post implements Serializable, IdHolder, SearchIdentifier {

    private String searchId;
    private Long id;
    private String name;
    private String description;
    private long ups;
    private long downs;
    private long viewCount;
    private String previewPath;
    private List<String> picturePaths;
    private DeviceType deviceType;
    private List<String> tags;
    private LocalDateTime createDate;
    private boolean visible;

    private String authorId;

    public Post(
            @JsonProperty(value = "id") Long id,
            @JsonProperty("search_id") String searchId,
            @JsonProperty(value = "name", required = true) String name,
            @JsonProperty(value = "description", required = true) String description,
            @JsonProperty(value = "ups", required = true) long ups,
            @JsonProperty(value = "downs", required = true) long downs,
            @JsonProperty(value = "view_count", required = true) long viewCount,
            @JsonProperty(value = "preview_path", required = true) String previewPath,
            @JsonProperty(value = "picture_paths", required = true) List<String> picturePaths,
            @JsonProperty(value = "client_type", required = true) DeviceType deviceType,
            @JsonProperty(value = "tags", required = true) List<String> tags,
            @JsonProperty(value = "author_id", required = true) String authorId,
            @JsonProperty("create_date") @JsonFormat(pattern = "yyyy-MM-dd HH:mm") LocalDateTime createDate,
            @JsonProperty(value = "visible", defaultValue = "false") boolean visible
    ) {
        this.id = id;
        this.searchId = searchId;
        this.name = name;
        this.description = description;
        this.ups = ups;
        this.downs = downs;
        this.viewCount = viewCount;
        this.previewPath = previewPath;
        this.picturePaths = picturePaths;
        this.deviceType = deviceType;
        this.tags = tags;
        this.authorId = authorId;
        this.visible = visible;
        this.createDate = firstNonNull(createDate, LocalDateTime.now());
    }

    @JsonProperty("id")
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
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

    @JsonProperty("preview_path")
    public String getPreviewPath() {
        return previewPath;
    }

    @JsonProperty("view_count")
    public long getViewCount() {
        return viewCount;
    }

    @JsonProperty("picture_paths")
    public List<String> getPicturePaths() {
        return picturePaths;
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

    @Override
    @JsonProperty("search_id")
    public String getSearchId() {
        return this.searchId;
    }

    @Override
    public void setSearchId(String searchId) {
        this.searchId = searchId;
    }

    @JsonProperty("create_date")
    public LocalDateTime getCreateDate() {
        return createDate;
    }

    @JsonProperty("visible")
    public boolean isVisible() {
        return visible;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Post post = (Post) o;

        return new EqualsBuilder()
                .append(authorId, post.authorId)
                .append(id, post.id)
                .append(name, post.name)
                .append(picturePaths, post.picturePaths)
                .append(tags, post.tags)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(id)
                .append(name)
                .append(picturePaths)
                .append(tags)
                .append(authorId)
                .toHashCode();
    }
}
