package com.turbo.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * Created by ermolaev on 6/5/17.
 */
public class UserImage implements Serializable, IdHolder {

    private Long id;
    private String image; // image url/hash?
    private String smallImage; // small preview image url/hash?
    private String description;
    private long userId;

    public UserImage(
            @JsonProperty("id") Long id,
            @JsonProperty(value = "image",required = true) String image,
            @JsonProperty("description") String description,
            @JsonProperty(value = "small_image",required = true) String smallImage,
            @JsonProperty(value = "user_id",required = true) long userId
    ) {
        this.id = id;
        this.image = image;
        this.smallImage = smallImage;
        this.description = description;
        this.userId = userId;
    }

    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @JsonProperty("small_image")
    public String getSmallImage() {
        return smallImage;
    }

    public String getImage() {
        return image;
    }

    public String getDescription() {
        return description;
    }

    @JsonProperty("user_id")
    public long getUserId() {
        return userId;
    }
}
