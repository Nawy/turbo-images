package com.turbo.model.aerospike;

import com.turbo.model.DeviceType;
import com.turbo.model.IdHolder;
import com.turbo.model.Post;
import com.turbo.model.UserImage;
import com.turbo.model.dto.TransferPost;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.ObjectUtils.firstNonNull;

/**
 * Created by rakhmetov on 09.05.17.
 * <p>
 * Just post on site with picture and comments
 */
public class PostRepoModel implements Serializable, IdHolder {

    private Long id;
    private String name;
    private long ups;
    private long downs;
    private long rating;
    private long views;
    private Set<Long> images; // key is UserImage_ID, value is description
    private DeviceType deviceType;
    private Set<String> tags;
    private LocalDateTime creationDateTime;
    private boolean visible;
    private long userId;
    private String description;

    public PostRepoModel() {
    }

    public PostRepoModel(
            Long id,
            String name,
            long ups,
            long downs,
            long rating,
            long views,
            Set<Long> images,
            DeviceType deviceType,
            Set<String> tags,
            long userId,
            LocalDateTime creationDateTime,
            boolean visible,
            String description
    ) {
        this.id = id;
        this.name = name;
        this.ups = ups;
        this.downs = downs;
        this.rating = rating;
        this.views = views;
        this.images = images == null ? Collections.emptySet() : Collections.unmodifiableSet(images);
        this.deviceType = deviceType;
        this.tags = tags == null ? Collections.emptySet() : Collections.unmodifiableSet(tags);
        this.userId = userId;
        this.visible = visible;
        this.description = description;
        this.creationDateTime = firstNonNull(creationDateTime, LocalDateTime.now());
    }

    public PostRepoModel(Post post) {
        this.id = post.getId();
        this.name = post.getName();
        this.ups = post.getUps();
        this.downs = post.getDowns();
        this.rating = post.getRating();
        this.views = post.getViews();
        this.images = post.getImages().stream().map(UserImage::getId).collect(Collectors.toSet());
        this.deviceType = post.getDeviceType();
        this.tags = Collections.unmodifiableSet(post.getTags());
        this.userId = post.getUser().getId();
        this.visible = post.isVisible();
        this.description = post.getDescription();
        this.creationDateTime = firstNonNull(post.getCreateDate(), LocalDateTime.now());
    }

    public PostRepoModel(TransferPost transferPost, long userId) {
        this(
                null,
                transferPost.getName(),
                0,
                0,
                0,
                0,
                transferPost.getImageIds(),
                transferPost.getDeviceType(),
                transferPost.getTags(),
                userId,
                LocalDateTime.now(),
                true,
                transferPost.getDescription()
        );
    }

    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
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

    public Set<Long> getImages() {
        return images;
    }

    public DeviceType getDeviceType() {
        return deviceType;
    }

    public Set<String> getTags() {
        return tags;
    }

    public long getUserId() {
        return userId;
    }

    public LocalDateTime getCreationDateTime() {
        return creationDateTime;
    }

    public boolean isVisible() {
        return visible;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        PostRepoModel that = (PostRepoModel) o;

        return new EqualsBuilder()
                .append(userId, that.userId)
                .append(id, that.id)
                .append(name, that.name)
                .append(creationDateTime, that.creationDateTime)
                .append(description, that.description)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(id)
                .append(name)
                .append(creationDateTime)
                .append(userId)
                .append(description)
                .toHashCode();
    }
}
