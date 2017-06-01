package com.turbo.model.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.turbo.model.Session;
import com.turbo.model.search.SearchIdentifier;
import com.turbo.model.IdHolder;

import java.time.LocalDateTime;
import java.util.Map;

import static org.apache.commons.lang3.ObjectUtils.firstNonNull;

/**
 * Created by ermolaev on 5/6/17.
 * <p>
 * This data for every foreign user
 */
public class User implements IdHolder, SearchIdentifier {

    private String searchId;
    private Long id;
    private String name;
    private String avatarPath;
    private Map<String, Session> sessions; // last ip from what was came in
    private String email;
    private String password;
    private LocalDateTime createDate;

    public User() {
    }

    public User(
            @JsonProperty("search_id") String searchId,
            @JsonProperty("id") Long id,
            @JsonProperty(value = "name", required = true) String name,
            @JsonProperty(value = "avatar_path") String avatarPath,
            @JsonProperty(value = "email", required = true) String email,
            @JsonProperty(value = "password", required = true) String password,
            @JsonProperty("create_date") @JsonFormat(pattern = "yyyy-MM-dd HH:mm") LocalDateTime createDate,
            Map<String, Session> sessions

    ) {
        this.searchId = searchId;
        this.id = id;
        this.name = name;
        this.avatarPath = avatarPath;
        this.sessions = sessions;
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

    //don't return this to front-end
    @JsonIgnore
    public Map<String, Session> getSessions() {
        return sessions;
    }

    // setters

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public void setSearchId(String searchId) {
        this.searchId = searchId;
    }
}
