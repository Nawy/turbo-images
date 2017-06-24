package com.turbo.model.search.content;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.turbo.model.UserImage;

import java.time.LocalDate;

/**
 * Created by ermolaev on 6/24/17.
 */
public class ImageSearchEntity {

    private Long id;
    private String description;
    private String thumbnailPath;
    private String sourcePath;
    private String album;
    private LocalDate createDate;
    private String username;

    public ImageSearchEntity(
            @JsonProperty("id") Long id,
            @JsonProperty("description") String description,
            @JsonProperty("thumbnail") String thumbnailPath,
            @JsonProperty("source") String sourcePath,
            @JsonProperty("album") String album,
            @JsonProperty("username") String username,
            @JsonProperty("create_date") LocalDate createDate
    ) {
        this.id = id;
        this.username = username;
        this.description = description;
        this.thumbnailPath = thumbnailPath;
        this.sourcePath = sourcePath;
        this.album = album;
        this.createDate = createDate;
    }

    @JsonProperty("description")
    public String getDescription() {
        return description;
    }

    @JsonProperty("thumbnail")
    public String getThumbnailPath() {
        return thumbnailPath;
    }

    @JsonProperty("source")
    public String getSourcePath() {
        return sourcePath;
    }

    @JsonProperty("id")
    public Long getId() {
        return id;
    }

    @JsonProperty("album")
    public String getAlbum() {
        return album;
    }

    @JsonProperty("create_date")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    public LocalDate getCreateDate() {
        return createDate;
    }

    @JsonProperty("username")
    public String getUsername() {
        return username;
    }
}
