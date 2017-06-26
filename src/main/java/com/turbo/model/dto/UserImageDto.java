package com.turbo.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.turbo.model.Image;
import com.turbo.model.UserImage;
import com.turbo.service.HashIdService;

import java.time.LocalDateTime;

/**
 * Created by rakhmetov on 06.06.17.
 */
public class UserImageDto {

    private String id;
    private Image image;
    private String description;
    private String username;
    private LocalDateTime creationDate;

    public UserImageDto(
            String id,
            Image image,
            String username,
            String description,
            LocalDateTime creationDate
    ) {
        this.id = id;
        this.image = image;
        this.description = description;
        this.username = username;
        this.creationDate = creationDate;
    }

    @JsonIgnore
    public static UserImageDto from(UserImage userImage) {
        return new UserImageDto(
                HashIdService.encodeHashId(userImage.getId()),
                userImage.getImage(),
                userImage.getUsername(),
                userImage.getDescription(),
                userImage.getCreationDate()
        );
    }

    public String getId() {
        return id;
    }

    public Image getImage() {
        return image;
    }

    public String getDescription() {
        return description;
    }

    @JsonProperty("username")
    public String getUsername() {
        return username;
    }

    @JsonProperty("creation_date")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    public LocalDateTime getCreationDate() {
        return creationDate;
    }
}