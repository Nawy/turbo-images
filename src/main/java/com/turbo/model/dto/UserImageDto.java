package com.turbo.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.turbo.model.UserImage;
import com.turbo.service.HashIdService;

/**
 * Created by rakhmetov on 06.06.17.
 */
public class UserImageDto {

    private String id;
    private String image;
    private String smallImage;
    private String description;
    private String userId;

    public UserImageDto(
            @JsonProperty("id") String id,
            @JsonProperty(value = "image", required = true) String image,
            @JsonProperty(value = "small_image", required = true) String smallImage,
            @JsonProperty("description") String description,
            @JsonProperty(value = "user_id", required = true) String userId
    ) {
        this.id = id;
        this.image = image;
        this.smallImage = smallImage;
        this.description = description;
        this.userId = userId;
    }

    @JsonIgnore
    public static UserImageDto from(UserImage userImage) {
        return new UserImageDto(
                HashIdService.encodeHashId(userImage.getId()),
                userImage.getImage(),
                userImage.getSmallImage(),
                userImage.getDescription(),
                HashIdService.encodeHashId(userImage.getUserId())
        );
    }

    public String getId() {
        return id;
    }

    public String getImage() {
        return image;
    }

    @JsonProperty("small_image")
    public String getSmallImage() {
        return smallImage;
    }

    public String getDescription() {
        return description;
    }

    @JsonProperty("user_id")
    public String getUserId() {
        return userId;
    }
}