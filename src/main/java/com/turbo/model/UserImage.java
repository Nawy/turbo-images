package com.turbo.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Created by ermolaev on 6/5/17.
 */
public class UserImage implements Serializable, IdHolder {

    private Long id;
    private String thumbnail;
    private String source;
    private String description;
    private String username;
    private String album;
    private LocalDateTime createDate;

    public UserImage(
            @JsonProperty("id") Long id,
            @JsonProperty(value = "thumbnail", required = true) String thumbnail,
            @JsonProperty(value = "source", required = true) String source,
            @JsonProperty("description") String description,
            @JsonProperty("album") String album,
            @JsonProperty(value = "username", required = true) String username,
            @JsonProperty("create_date") LocalDateTime createDate
    ) {
        this.id = id;
        this.thumbnail = thumbnail;
        this.source = source;
        this.description = description;
        this.username = username;
        this.createDate = createDate;
        this.album = album;
    }

    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public String getSource() {
        return source;
    }

    public String getDescription() {
        return description;
    }

    @JsonProperty("username")
    public String getUsername() {
        return username;
    }

    @JsonProperty("create_date")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public String getAlbum() {
        return album;
    }
}
