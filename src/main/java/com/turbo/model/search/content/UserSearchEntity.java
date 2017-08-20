package com.turbo.model.search.content;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.turbo.model.User;
import com.turbo.model.search.field.UserFieldNames;

import java.time.LocalDateTime;

/**
 * Created by ermolaev on 5/27/17.
 * Needs for send enough data in search engine
 */
public class UserSearchEntity {

    public final static String CREATION_DATE_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSS";

    private long id;
    private String name;
    private String email;
    private long rating;
    private LocalDateTime creationDate;

    public UserSearchEntity(
            @JsonProperty(value = UserFieldNames.ID, required = true) long id,
            @JsonProperty(value = UserFieldNames.NAME, required = true) String description,
            @JsonProperty(value = UserFieldNames.EMAIL, required = true) String email,
            @JsonProperty(value = UserFieldNames.RATING, required = true) long rating,
            @JsonProperty(UserFieldNames.CREATION_DATE) @JsonFormat(pattern = CREATION_DATE_PATTERN) LocalDateTime creationDate
    ) {
        this.id = id;
        this.name = description;
        this.email = email;
        this.rating = rating;
        this.creationDate = creationDate;
    }

    public UserSearchEntity(final User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.creationDate = user.getCreateDate();
    }

    @JsonProperty(UserFieldNames.ID)
    public long getId() {
        return id;
    }

    @JsonProperty(UserFieldNames.NAME)
    public String getName() {
        return name;
    }

    @JsonProperty(UserFieldNames.EMAIL)
    public String getEmail() {
        return email;
    }

    @JsonProperty(UserFieldNames.RATING)
    public long getRating() {
        return rating;
    }

    @JsonProperty(UserFieldNames.CREATION_DATE)
    @JsonFormat(pattern = CREATION_DATE_PATTERN)
    public LocalDateTime getCreationDate() {
        return creationDate;
    }

}
