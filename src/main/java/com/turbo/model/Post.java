package com.turbo.model;

import org.springframework.data.cassandra.mapping.PrimaryKey;
import org.springframework.data.cassandra.mapping.Table;

import java.util.List;

/**
 * Created by rakhmetov on 09.05.17.
 * <p>
 * Just post on site with picture and comments
 */
@Table("post")
public class Post {

    @PrimaryKey
    private Long id;
    private String name;
    private String description;
    private int rating; // upvotes - downvotes
    private int viewsAmount; // how often people view this post
    private List<String> picturePaths; // max 20
    private List<Comment> comments;
    private ClientType clientType;
    private List<String> tag;

    public Post() {
    }

    public Post(
            Long id,
            String name,
            String description,
            int rating,
            int viewsAmount,
            List<String> picturePaths,
            List<Comment> comments,
            ClientType clientType,
            List<String> tag
    ) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.rating = rating;
        this.viewsAmount = viewsAmount;
        this.picturePaths = picturePaths;
        this.comments = comments;
        this.clientType = clientType;
        this.tag = tag;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getRating() {
        return rating;
    }

    public int getViewsAmount() {
        return viewsAmount;
    }

    public List<String> getPicturePaths() {
        return picturePaths;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public ClientType getClientType() {
        return clientType;
    }

    public List<String> getTag() {
        return tag;
    }
}
