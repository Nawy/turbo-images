package com.turbo.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.turbo.util.EncryptionService;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Objects;

@Data
@Builder
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class PostRatingDto {

    @JsonIgnore
    private long id;

    @JsonIgnore
    private long views;

    @JsonIgnore
    private long rating;

    public PostRatingDto(
            @JsonProperty(value = "id", required = true) String id,
            @JsonProperty("views") Boolean views,
            @JsonProperty("up") Boolean up,
            @JsonProperty("down") Boolean down
    ) {
        this.id = EncryptionService.decodeHashId(id);
        this.views = Objects.nonNull(views) ? 1 : 0;

        if(Objects.nonNull(up) || Objects.nonNull(down)) {
            this.rating = up ? 1L : -1L;
        } else {
            this.rating = 0;
        }
    }
}
