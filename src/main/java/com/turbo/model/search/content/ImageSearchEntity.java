package com.turbo.model.search.content;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.turbo.model.UserImage;
import com.turbo.model.search.field.ImageFieldNames;

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
            @JsonProperty(ImageFieldNames.ID) Long id,
            @JsonProperty(ImageFieldNames.DESCRIPTION) String description,
            @JsonProperty(ImageFieldNames.USER_ID) long userId,
            @JsonProperty(ImageFieldNames.CREATION_DATE) @JsonFormat(pattern = CREATION_DATE_PATTERN) LocalDateTime creationDate
    ) {
        this.id = id;
        this.userId = userId;
        this.description = description;
        this.creationDate = creationDate;
    }

    @JsonProperty(ImageFieldNames.ID)
    public Long getId() {
        return id;
    }

    @JsonProperty(ImageFieldNames.DESCRIPTION)
    public String getDescription() {
        return description;
    }

    @JsonProperty(ImageFieldNames.CREATION_DATE)
    @JsonFormat(pattern = CREATION_DATE_PATTERN)
    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    @JsonProperty(ImageFieldNames.USER_ID)
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
