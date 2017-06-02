package com.turbo.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

import static org.apache.commons.lang3.ObjectUtils.firstNonNull;

/**
 * Created by ermolaev on 5/6/17.
 * <p>
 * This data for every foreign user
 */
public class User implements IdHolder {

    private Long id;
    private String name;
    private String avatarPath;
    private String ip; // last ip from what was came in
    private String email;
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
            @JsonProperty("create_date") @JsonFormat(pattern = "yyyy-MM-dd HH:mm") LocalDateTime createDate,
            @JsonProperty(value = "ip", required = true) String ip
    ) {
        this.id = id;
        this.name = name;
        this.avatarPath = avatarPath;
        this.email = email;
        this.password = password;
        this.createDate = firstNonNull(createDate, LocalDateTime.now());
        this.ip = ip;
    }

    @JsonProperty(value = "id")
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
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

    public String getIp() {
        return ip;
    }
}
