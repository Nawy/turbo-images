package com.turbo.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.turbo.model.DeviceType;
import com.turbo.model.Rating;
import com.turbo.model.aerospike.PostRepoModel;
import com.turbo.util.EncryptionService;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
public class TransferPost {

    private Long id;
    private String name;
    private Set<Long> imageIds;
    private DeviceType deviceType;
    private Set<String> tags;
    private boolean visible;
    private String description;

    public TransferPost(
            @JsonProperty("id") String id,
            @JsonProperty("name") String name,
            @JsonProperty("image_ids") List<String> imageIds,
            @JsonProperty("device_type") DeviceType deviceType,
            @JsonProperty("tags") Set<String> tags,
            @JsonProperty("visible") boolean visible,
            @JsonProperty("description") String description
    ) {
        this.id = StringUtils.isBlank(id) ? null : EncryptionService.decodeHashId(id);
        this.name = name;
        this.imageIds = imageIds.stream().map(EncryptionService::decodeHashId).collect(Collectors.toSet());
        this.deviceType = deviceType;
        this.tags = tags;
        this.visible = visible;
        this.description = description;
    }

    public PostRepoModel toPostRepoModel(long userId) {
        return new PostRepoModel(
                id,
                name,
                new Rating(),
                0,
                imageIds,
                deviceType,
                tags,
                userId,
                LocalDateTime.now(),
                visible,
                description,
                Collections.emptyMap()
        );
    }

}
