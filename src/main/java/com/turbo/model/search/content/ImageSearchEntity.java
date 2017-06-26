package com.turbo.model.search.content;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

/**
 * Created by ermolaev on 6/24/17.
 */
public class ImageSearchEntity {

    private Long id;
    private String description;
    private String thumbnailPath;
    private String sourcePath;
    private LocalDateTime createDate;
    private String username;

    public ImageSearchEntity(
            @JsonProperty("id") Long id,
            @JsonProperty("description") String description,
            @JsonProperty("thumbnail") String thumbnailPath,
            @JsonProperty("source") String sourcePath,
            @JsonProperty("username") String username,
            @JsonProperty("create_date") LocalDateTime createDate
    ) {
        this.id = id;
        this.username = username;
        this.description = description;
        this.thumbnailPath = thumbnailPath;
        this.sourcePath = sourcePath;
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

    @JsonProperty("create_date")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    public LocalDateTime getCreateDate() {
        return createDate;
    }

    @JsonProperty("username")
    public String getUsername() {
        return username;
    }
}
