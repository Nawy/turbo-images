package com.turbo.model.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.turbo.model.IdHolder;

/**
 * Created by ermolaev on 5/6/17.
 * <p>
 * This data for every foreign user
 */
public class User implements IdHolder {

    private String id;
    private String name;
    private String avatarPath;
    private String ip; // last ip from what was came in
    private String email;
    private String password;

    public User() {
    }

    public User(
            @JsonProperty("id") String id,
            @JsonProperty(value = "name", required = true) String name,
            @JsonProperty(value = "avatar_path") String avatarPath,
            @JsonProperty(value = "ip", required = true) String ip,
            @JsonProperty(value = "email", required = true) String email,
            @JsonProperty(value = "password", required = true) String password) {
        this.id = id;
        this.name = name;
        this.avatarPath = avatarPath;
        this.ip = ip;
        this.email = email;
        this.password = password;
    }

    @JsonIgnore
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
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

    @JsonProperty(value = "ip")
    public String getIp() {
        return ip;
    }

    @JsonProperty(value = "email")
    public String getEmail() {
        return email;
    }

    @JsonProperty(value = "password")
    public String getPassword() {
        return password;
    }
}
