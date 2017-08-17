package com.turbo.model.search.content;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.turbo.model.UserImage;

import java.time.LocalDateTime;

/**
 * Created by ermolaev on 6/24/17.
 */
public class ImageSearchEntity {

    public final static String CREATION_DATE_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSS";

    private Long id;
    private String description;
    private LocalDateTime creationDate;
    private long userId;

    public ImageSearchEntity(
            @JsonProperty("id") Long id,
            @JsonProperty("description") String description,
            @JsonProperty("user_id") long userId,
            @JsonProperty("creation_date") @JsonFormat(pattern = CREATION_DATE_PATTERN) LocalDateTime creationDate
    ) {
        this.id = id;
        this.userId = userId;
        this.description = description;
        this.creationDate = creationDate;
    }

    @JsonProperty("description")
    public String getDescription() {
        return description;
    }

    @JsonProperty("id")
    public Long getId() {
        return id;
    }

    @JsonProperty("create_date")
    @JsonFormat(pattern = CREATION_DATE_PATTERN)
    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    @JsonProperty("user_id")
    public long getUserId() {
        return userId;
    }

    public static ImageSearchEntity from(final UserImage image) {
        return new ImageSearchEntity(
                image.getId(),
                image.getDescription(),
                image.getUserId(),
                image.getCreationDate()
        );
    }
}
