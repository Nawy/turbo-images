package com.turbo.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * Created by ermolaev on 6/5/17.
 */
public class UserImage implements Serializable, IdHolder {

    private Long id;
    private Image image;
    private String description;
    private long userId;

    public UserImage(
            @JsonProperty("id") Long id,
            @JsonProperty(value = "image", required = true) Image image,
            @JsonProperty("description") String description,
            @JsonProperty(value = "user_id", required = true) long userId
    ) {
        this.id = id;
        this.image = image;
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

    public Image getImage() {
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
