package com.turbo.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by ermolaev on 6/5/17.
 */
public class UserImage {

    private long id;
    private Image image;
    private String description;
    private String userId;

    public UserImage(
            @JsonProperty("id") long id,
            @JsonProperty("image") Image image,
            @JsonProperty("description") String description,
            @JsonProperty("user_id") String userId
    ) {
        this.id = id;
        this.image = image;
        this.description = description;
        this.userId = userId;
    }

    public long getId() {
        return id;
    }

    public Image getImage() {
        return image;
    }

    public String getDescription() {
        return description;
    }

    public String getUserId() {
        return userId;
    }
}
