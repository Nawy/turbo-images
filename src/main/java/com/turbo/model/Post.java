package com.turbo.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
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
public class Post implements Serializable, IdHolder {

    private Long id;
    private String name;
    private long ups;
    private long downs;
    private long rating;
    private long views;
    private String previewPath;
    private List<UserImage> images;
    private DeviceType deviceType;
    private List<String> tags;
    private LocalDateTime createDate;
    private boolean visible;

    private String authorId;

    public Post(
            @JsonProperty(value = "id") Long id,
            @JsonProperty(value = "name", required = true) String name,
            @JsonProperty(value = "ups", required = true) long ups,
            @JsonProperty(value = "downs", required = true) long downs,
            @JsonProperty(value = "rating", required = true) long rating,
            @JsonProperty(value = "views", required = true) long views,
            @JsonProperty(value = "preview_path", required = true) String previewPath,
            @JsonProperty(value = "images", required = true) List<UserImage> images,
            @JsonProperty(value = "client_type", required = true) DeviceType deviceType,
            @JsonProperty(value = "tags", required = true) List<String> tags,
            @JsonProperty(value = "author_id", required = true) String authorId,
            @JsonProperty("create_date") @JsonFormat(pattern = "yyyy-MM-dd HH:mm") LocalDateTime createDate,
            @JsonProperty(value = "visible", defaultValue = "false") boolean visible
    ) {
        this.id = id;
        this.name = name;
        this.ups = ups;
        this.downs = downs;
        this.rating = rating;
        this.views = views;
        this.previewPath = previewPath;
        this.images = images;
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

    @JsonProperty("rating")
    public long getRating() {
        return rating;
    }

    @JsonProperty("views")
    public long getViews() {
        return views;
    }

    @JsonProperty("images")
    public List<UserImage> getImages() {
        return images;
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
                .append(ups, post.ups)
                .append(downs, post.downs)
                .append(rating, post.rating)
                .append(views, post.views)
                .append(visible, post.visible)
                .append(id, post.id)
                .append(name, post.name)
                .append(authorId, post.authorId)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(id)
                .append(name)
                .append(ups)
                .append(downs)
                .append(rating)
                .append(views)
                .append(visible)
                .append(authorId)
                .toHashCode();
    }
}
