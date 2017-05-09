package com.turbo.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.cassandra.mapping.PrimaryKey;
import org.springframework.data.cassandra.mapping.Table;

import java.util.UUID;

/**
 * Created by ermolaev on 5/6/17.
 */
@Table("image_info")
public class ImageInfo {

    @PrimaryKey
    private long id;
    private String name;
    private String description;
    private long upvote;
    private long downvote;
    private String path;
    private long author;

    public ImageInfo() {
    }

    public ImageInfo(
            @JsonProperty("id") long id,
            @JsonProperty("name") String name,
            @JsonProperty("description") String description,
            @JsonProperty("upvote") long upvote,
            @JsonProperty("downvote") long downvote,
            @JsonProperty("path") String path,
            @JsonProperty("author") long author
    ) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.upvote = upvote;
        this.downvote = downvote;
        this.path = path;
        this.author = author;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public long getUpvote() {
        return upvote;
    }

    public long getDownvote() {
        return downvote;
    }

    public String getPath() {
        return path;
    }

    public long getAuthor() {
        return author;
    }
}
