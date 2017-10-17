package com.turbo.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.turbo.model.DeviceType;
import com.turbo.model.Post;
import com.turbo.model.UserImage;
import com.turbo.util.EncryptionService;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;
import java.util.Set;

import static org.apache.commons.lang3.ObjectUtils.firstNonNull;

/**
 * Created by ermolaev on 6/12/17.
 */
public class PostPreview {

    private String id;
    private String name;
    private String description;
    private long ups;
    private long downs;
    private long rating;
    private long views;
    private String previewImage;
    private DeviceType deviceType;
    private Set<String> tags;
    private LocalDateTime createDate;

    public PostPreview(
            String id,
            String name,
            String description,
            long ups,
            long downs,
            long rating,
            long views,
            String previewImage,
            DeviceType deviceType,
            Set<String> tags,
            LocalDateTime createDate
    ) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.ups = ups;
        this.downs = downs;
        this.rating = rating;
        this.views = views;
        this.previewImage = previewImage;
        this.deviceType = deviceType;
        this.tags = tags;
        this.createDate = createDate;
    }

    @JsonIgnore
    public static PostPreview from(Post post) {
        final String imageDescription = post.getImages()
                .stream()
                .filter(image -> StringUtils.isNotBlank(image.getDescription()))
                .map(UserImage::getDescription)
                .findFirst().orElse(null);

        return new PostPreview(
                EncryptionService.encodeHashId(post.getId()),
                post.getName(),
                firstNonNull(
                        post.getDescription(),
                        imageDescription,
                        ""
                ),
                post.getUps(),
                post.getDowns(),
                post.getRating(),
                post.getViews(),
                post.getImages().stream().findFirst().get().getImage().getThumbnail(),
                post.getDeviceType(),
                post.getTags(),
                post.getCreateDate()
        );
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
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

    @JsonProperty("preview_image")
    public String getPreviewImage() {
        return previewImage;
    }

    @JsonProperty("device_type")
    public DeviceType getDeviceType() {
        return deviceType;
    }

    public Set<String> getTags() {
        return tags;
    }

    @JsonProperty("create_date")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    public LocalDateTime getCreateDate() {
        return createDate;
    }
}
