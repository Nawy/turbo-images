package com.turbo.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.turbo.model.DeviceType;
import com.turbo.model.Post;
import com.turbo.model.UserImage;
import com.turbo.service.HashIdService;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.ObjectUtils.firstNonNull;

/**
 * Created by ermolaev on 6/12/17.
 */
public class PostSearchDto {


    private String id;
    private String name;
    private String desciption;
    private long ups;
    private long downs;
    private long rating;
    private long views;
    private String previewImage;
    private DeviceType deviceType;
    private List<String> tags;
    private LocalDateTime createDate;

    public PostSearchDto(
            String id,
            String name,
            String desciption,
            long ups,
            long downs,
            long rating,
            long views,
            String previewImage,
            DeviceType deviceType,
            List<String> tags,
            LocalDateTime createDate
    ) {
        this.id = id;
        this.name = name;
        this.desciption = desciption;
        this.ups = ups;
        this.downs = downs;
        this.rating = rating;
        this.views = views;
        this.previewImage = previewImage;
        this.deviceType = deviceType;
        this.tags = tags;
        this.createDate = createDate;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDesciption() {
        return desciption;
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

    public List<String> getTags() {
        return tags;
    }

    @JsonProperty("create_date")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    public LocalDateTime getCreateDate() {
        return createDate;
    }

    @JsonIgnore
    public static PostSearchDto from(Post post) {
        final UserImage imageWithDescription= post.getImages().stream()
                .filter(
                        image -> StringUtils.isNotBlank(image.getDescription())
                )
                .findFirst().orElse(null);

        return new PostSearchDto(
                HashIdService.encodeHashId(post.getId()),
                post.getName(),
                firstNonNull(
                        post.getDescription(),
                        imageWithDescription.getDescription(),
                        ""
                ),
                post.getUps(),
                post.getDowns(),
                post.getRating(),
                post.getViews(),
                post.getImages().get(0).getImage().getThumbnail(),
                post.getDeviceType(),
                post.getTags(),
                post.getCreateDate()
        );
    }
}
