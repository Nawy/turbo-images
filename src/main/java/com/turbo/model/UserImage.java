package com.turbo.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

/**
 * Created by ermolaev on 6/5/17.
 */
public class UserImage {

    private Long id;
    private Image image;
    private String description;
    private long userId;
    private LocalDateTime creationDate;

    protected UserImage() {
    }

    public UserImage(
            Long id,
            Image image,
            long userId,
            String description,
            LocalDateTime creationDate
    ) {
        this.id = id;
        this.image = image;
        this.description = description;
        this.userId = userId;
        this.creationDate = creationDate;
    }

    public UserImage(Image image, long userId, String description, LocalDateTime creationDate) {
        this.image = image;
        this.description = description;
        this.userId = userId;
        this.creationDate = creationDate;
    }

    public Long getId() {
        return id;
    }

    public Image getImage() {
        return this.image;
    }

    public String getDescription() {
        return description;
    }

    @JsonProperty("user_id")
    public long getUserId() {
        return userId;
    }

    @JsonProperty("creation_date")
    public LocalDateTime getCreationDate() {
        return creationDate;
    }
}
