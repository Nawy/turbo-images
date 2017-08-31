package com.turbo.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.turbo.model.UserImage;
import com.turbo.util.EncryptionService;

import java.time.LocalDateTime;

import static com.google.common.base.MoreObjects.firstNonNull;

/**
 * Created by rakhmetov on 06.06.17.
 */
public class UserImageDto {

    private String id;
    private ImageDto image;
    private String name;
    private String description;
    private LocalDateTime creationDate;

    public UserImageDto(
            String id,
            ImageDto image,
            String name,
            String description,
            LocalDateTime creationDate
    ) {
        this.id = id;
        this.image = image;
        this.name = name;
        this.description = description;
        this.creationDate = creationDate;
    }

    @JsonIgnore
    public static UserImageDto from(UserImage userImage) {
        return new UserImageDto(
                EncryptionService.encodeHashId(userImage.getId()),
                ImageDto.from(userImage.getImage()),
                firstNonNull(userImage.getName(), ""),
                firstNonNull(userImage.getDescription(), ""),
                userImage.getCreationDate()
        );
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public ImageDto getImage() {
        return image;
    }

    public String getDescription() {
        return description;
    }

    @JsonProperty("creation_date")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    public LocalDateTime getCreationDate() {
        return creationDate;
    }
}