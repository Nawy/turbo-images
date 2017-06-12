package com.turbo.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.turbo.model.Image;
import com.turbo.model.UserImage;
import com.turbo.service.HashIdService;

/**
 * Created by rakhmetov on 06.06.17.
 */
public class UserImageDto {

    private String id;
    private Image image;
    private String description;
    private String username;

    public UserImageDto(
            @JsonProperty("id") String id,
            @JsonProperty(value = "image", required = true) Image image,
            @JsonProperty("description") String description,
            @JsonProperty(value = "user_name", required = true) String username
    ) {
        this.id = id;
        this.image = image;
        this.description = description;
        this.username = username;
    }

    @JsonIgnore
    public static UserImageDto from(UserImage userImage) {
        return new UserImageDto(
                HashIdService.encodeHashId(userImage.getId()),
                userImage.getImage(),
                userImage.getDescription(),
                userImage.getUsername()
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

    @JsonProperty("user_name")
    public String getUsername() {
        return username;
    }
}