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
    private String username;

    public UserImage(
            @JsonProperty("id") Long id,
            @JsonProperty(value = "image", required = true) Image image,
            @JsonProperty("description") String description,
            @JsonProperty(value = "username", required = true) String username
    ) {
        this.id = id;
        this.image = image;
        this.description = description;
        this.username = username;
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

    @JsonProperty("username")
    public String getUsername() {
        return username;
    }
}
