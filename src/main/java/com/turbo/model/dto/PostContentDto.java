package com.turbo.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.turbo.util.EncryptionService;
import lombok.Data;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class PostContentDto {

    @JsonIgnore
    private long postId;
    @JsonIgnore
    private String name;
    @JsonIgnore
    private String description;
    @JsonIgnore
    private Set<String> tags;
    @JsonIgnore
    private Set<Long> imageIds;

    public PostContentDto(
            @JsonProperty(value = "post_id", required = true) String postId,
            String name,
            String description,
            Set<String> tags,
            @JsonProperty(value = "image_ids") List<String> imageIds
    ) {
        this.postId = EncryptionService.decodeHashId(postId);
        this.name = name;
        this.description = description;
        this.tags = tags;
        this.imageIds = imageIds.stream()
                .map(EncryptionService::decodeHashId)
                .collect(Collectors.toSet());
    }
}
