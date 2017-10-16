package com.turbo.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.turbo.model.DeviceType;
import com.turbo.model.Post;
import com.turbo.model.User;
import com.turbo.model.UserImage;
import com.turbo.util.EncryptionService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class TransferPost {
    private String name;
    private Set<Long> imageIds; // value is description
    private DeviceType deviceType;
    private Set<String> tags;
    private boolean visible;
    private String description;

    public TransferPost(
            @JsonProperty("name") String name,
            @JsonProperty("image_ids") Set<Long> imageIds,
            @JsonProperty("device_type") DeviceType deviceType,
            @JsonProperty("tags") Set<String> tags,
            @JsonProperty("visible") boolean visible,
            @JsonProperty("description") String description
    ) {
        this.name = name;
        this.imageIds = imageIds;
        this.deviceType = deviceType;
        this.tags = tags;
        this.visible = visible;
        this.description = description;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("image_ids")
    public Set<Long> getImageIds() {
        return imageIds;
    }


    @JsonProperty("device_type")
    public DeviceType getDeviceType() {
        return deviceType;
    }

    @JsonProperty("tags")
    public Set<String> getTags() {
        return tags;
    }

    @JsonProperty("visible")
    public boolean isVisible() {
        return visible;
    }

    @JsonProperty("description")
    public String getDescription() {
        return description;
    }
}
