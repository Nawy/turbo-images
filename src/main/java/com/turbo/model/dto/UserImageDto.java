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
    private String userId;

    public UserImageDto(
            @JsonProperty("id") String id,
            @JsonProperty(value = "image", required = true) Image image,
            @JsonProperty("description") String description,
            @JsonProperty(value = "user_id", required = true) String userId
    ) {
        this.id = id;
        this.image = image;
        this.description = description;
        this.userId = userId;
    }

    @JsonIgnore
    public static UserImageDto from(UserImage userImage) {
        return new UserImageDto(
                HashIdService.encodeHashId(userImage.getId()),
                userImage.getImage(),
                userImage.getDescription(),
                HashIdService.encodeHashId(userImage.getUserId())
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

    @JsonProperty("user_id")
    public String getUserId() {
        return userId;
    }
}