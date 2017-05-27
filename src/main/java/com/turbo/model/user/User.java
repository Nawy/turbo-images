package com.turbo.model.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.turbo.model.search.SearchIdentifier;
import com.turbo.model.IdHolder;

import java.time.LocalDateTime;

import static org.apache.commons.lang3.ObjectUtils.firstNonNull;

/**
 * Created by ermolaev on 5/6/17.
 * <p>
 * This data for every foreign user
 */
public class User implements IdHolder, SearchIdentifier {

    private String searchId;
    private String id;
    private String name;
    private String avatarPath;
    private String ip; // last ip from what was came in
    private String email;
    private String password;
    private LocalDateTime createDate;

    public User() {
    }

    public User(
            @JsonProperty("search_id") String searchId,
            @JsonProperty("id") String id,
            @JsonProperty(value = "name", required = true) String name,
            @JsonProperty(value = "avatar_path") String avatarPath,
            @JsonProperty(value = "ip", required = true) String ip,
            @JsonProperty(value = "email", required = true) String email,
            @JsonProperty(value = "password", required = true) String password,
            @JsonProperty("create_date") @JsonFormat(pattern = "yyyy-MM-dd HH:mm") LocalDateTime createDate
    ) {
        this.searchId = searchId;
        this.id = id;
        this.name = name;
        this.avatarPath = avatarPath;
        this.ip = ip;
        this.email = email;
        this.password = password;
        this.createDate = firstNonNull(createDate, LocalDateTime.now());
    }

    // getters

    @Override
    @JsonProperty(value = "search_id")
    public String getSearchId() {
        return searchId;
    }

    @JsonProperty(value = "id")
    public String getId() {
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

    @JsonProperty("create_date")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    public LocalDateTime getCreateDate() {
        return createDate;
    }

    // setters

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public void setSearchId(String searchId) {
        this.searchId = searchId;
    }
}
