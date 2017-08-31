package com.turbo.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.apache.commons.lang3.ObjectUtils.firstNonNull;

/**
 * Created by rakhmetov on 09.05.17.
 * <p>
 * Just post on site with picture and comments
 */
public class Post {

    private Long id;
    private String name;
    private long ups;
    private long downs;
    private long rating;
    private long views;
    private Map<UserImage, String> images; // value is description
    private DeviceType deviceType;
    private Set<String> tags;
    private LocalDateTime createDate;
    private boolean visible;
    private User user;
    private String description;

    public Post(
            @JsonProperty(value = "id") Long id,
            @JsonProperty(value = "name", required = true) String name,
            @JsonProperty(value = "ups", required = true) long ups,
            @JsonProperty(value = "downs", required = true) long downs,
            @JsonProperty(value = "rating", required = true) long rating,
            @JsonProperty(value = "views", required = true) long views,
            @JsonProperty(value = "images", required = true) Map<UserImage, String> images,
            @JsonProperty(value = "client_type", required = true) DeviceType deviceType,
            @JsonProperty(value = "tags") Set<String> tags,
            @JsonProperty(value = "user", required = true) User user,
            @JsonProperty("create_date") @JsonFormat(pattern = "yyyy-MM-dd HH:mm") LocalDateTime createDate,
            @JsonProperty(value = "visible", defaultValue = "false") boolean visible,
            @JsonProperty("description") String description
    ) {
        this.id = id;
        this.name = name;
        this.ups = ups;
        this.downs = downs;
        this.rating = rating;
        this.views = views;
        this.images = images == null ? Collections.EMPTY_MAP : Collections.unmodifiableMap(images);
        this.deviceType = deviceType;
        this.tags = tags == null ? Collections.emptySet() : Collections.unmodifiableSet(tags);
        this.user = user;
        this.visible = visible;
        this.description = description;
        this.createDate = firstNonNull(createDate, LocalDateTime.now());
    }

    @JsonProperty("id")
    public Long getId() {
        return id;
    }

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

    @JsonProperty("rating")
    public long getRating() {
        return rating;
    }

    @JsonProperty("views")
    public long getViews() {
        return views;
    }

    @JsonProperty("images")
    public Map<UserImage, String> getImages() {
        return images;
    }

    @JsonProperty("client_type")
    public DeviceType getDeviceType() {
        return deviceType;
    }

    @JsonProperty("tags")
    public Set<String> getTags() {
        return tags;
    }

    @JsonProperty("user")
    public User getUser() {
        return user;
    }

    @JsonProperty("create_date")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    public LocalDateTime getCreateDate() {
        return createDate;
    }

    @JsonProperty("visible")
    public boolean isVisible() {
        return visible;
    }

    @JsonProperty("description")
    public String getDescription() {
        return description;
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
                .append(images, post.images)
                .append(deviceType, post.deviceType)
                .append(tags, post.tags)
                .append(createDate, post.createDate)
                .append(user, post.user)
                .append(description, post.description)
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
                .append(images)
                .append(deviceType)
                .append(tags)
                .append(createDate)
                .append(visible)
                .append(user)
                .append(description)
                .toHashCode();
    }
}
