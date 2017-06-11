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
public class User implements Serializable {

    private String name;
    private String avatarPath;
    private String ip; // last ip from what was came in
    private String email;
    private String password;
    private LocalDateTime createDate;

    public User() {
    }

    public User(
            @JsonProperty(value = "name", required = true) String name,
            @JsonProperty(value = "avatar_path") String avatarPath,
            @JsonProperty(value = "email", required = true) String email,
            @JsonProperty(value = "password", required = true) String password,
            @JsonProperty("create_date") @JsonFormat(pattern = "yyyy-MM-dd HH:mm") LocalDateTime createDate,
            @JsonProperty(value = "ip", required = true) String ip
    ) {
        this.name = name;
        this.avatarPath = avatarPath;
        this.email = email;
        this.password = password;
        this.createDate = firstNonNull(createDate, LocalDateTime.now());
        this.ip = ip;
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

    public String getIp() {
        return ip;
    }
}
