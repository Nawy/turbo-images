package com.turbo.model.search.content;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.turbo.model.User;

import java.time.LocalDateTime;

/**
 * Created by ermolaev on 5/27/17.
 * Needs for send enough data in search engine
 */
public class UserSearchEntity {

    private String name;
    private String avatarPath;
    private String email;
    private long rating;
    private LocalDateTime createDate;

    public UserSearchEntity(
            @JsonProperty(value = "name", required = true) String name,
            @JsonProperty(value = "email", required = true) String email,
            @JsonProperty(value = "rating", required = true) long rating,
            @JsonProperty("create_date") @JsonFormat(pattern = "yyyy-MM-dd HH:mm") LocalDateTime createDate
    ) {
        this.name = name;
        this.email = email;
        this.rating = rating;
        this.createDate = createDate;
    }

    public UserSearchEntity(final User user) {
        this.name = user.getName();
        this.avatarPath = user.getAvatarPath();
        this.email = user.getEmail();
        this.createDate = user.getCreateDate();
    }

    @JsonProperty(value = "name")
    public String getName() {
        return name;
    }

    @JsonProperty(value = "avatar_path")
    public String getAvatarPath() {
        return avatarPath;
    }

    @JsonProperty(value = "email")
    public String getEmail() {
        return email;
    }

    @JsonProperty(value = "rating")
    public long getRating() {
        return rating;
    }

    @JsonProperty("create_date")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    public LocalDateTime getCreateDate() {
        return createDate;
    }

}
