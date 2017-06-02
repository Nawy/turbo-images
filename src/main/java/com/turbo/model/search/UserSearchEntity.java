package com.turbo.model.search;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.turbo.model.User;

import java.time.LocalDateTime;

/**
 * Created by ermolaev on 5/27/17.
 * Needs for send enough data in search engine
 */
public class UserSearchEntity {

    private Long id;
    private String name;
    private String avatarPath;
    private String email;
    private String password;
    private LocalDateTime createDate;

    public UserSearchEntity(
            @JsonProperty("id") Long id,
            @JsonProperty(value = "name", required = true) String name,
            @JsonProperty(value = "avatar_path") String avatarPath,
            @JsonProperty(value = "email", required = true) String email,
            @JsonProperty(value = "password", required = true) String password,
            @JsonProperty("create_date") @JsonFormat(pattern = "yyyy-MM-dd HH:mm") LocalDateTime createDate
    ) {
        this.id = id;
        this.name = name;
        this.avatarPath = avatarPath;
        this.email = email;
        this.password = password;
        this.createDate = createDate;
    }

    public UserSearchEntity(final User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.avatarPath = user.getAvatarPath();
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.createDate = user.getCreateDate();
    }

    @JsonProperty(value = "id")
    public Long getId() {
        return id;
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

    @JsonProperty(value = "password")
    public String getPassword() {
        return password;
    }

    @JsonProperty("create_date")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    public LocalDateTime getCreateDate() {
        return createDate;
    }

}
