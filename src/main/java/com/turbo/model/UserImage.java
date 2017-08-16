package com.turbo.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        UserImage userImage = (UserImage) o;

        return new EqualsBuilder()
                .append(userId, userImage.userId)
                .append(image, userImage.image)
                .append(creationDate, userImage.creationDate)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(image)
                .append(userId)
                .append(creationDate)
                .toHashCode();
    }
}
