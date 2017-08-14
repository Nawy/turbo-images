package com.turbo.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.time.LocalDateTime;

import static org.apache.commons.lang3.ObjectUtils.firstNonNull;

/**
 * Created by ermolaev on 5/6/17.
 * <p>
 * This data for every foreign user
 */
public class User implements Serializable, IdHolder {

    private Long id;
    private String name; // should be unique!
    private String avatarPath;
    private String email; // should be unique!
    private String password;
    private LocalDateTime createDate;

    public User() {
    }

    public User(
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
        this.createDate = firstNonNull(createDate, LocalDateTime.now());
    }

    public User(String name, String avatarPath, String email, String password) {
        this.name = name;
        this.avatarPath = avatarPath;
        this.email = email;
        this.password = password;
    }

    public User(
            User oldUser,
            User newUser
    ) {
        this.id = oldUser.id;
        this.name = firstNonNull(oldUser.name, newUser.name);
        this.avatarPath = firstNonNull(oldUser.avatarPath, newUser.avatarPath);
        this.email = firstNonNull(oldUser.email, newUser.email);
        this.password = firstNonNull(oldUser.password, newUser.password);
        this.createDate = oldUser.createDate;
    }

    //will be id
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

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }
}
