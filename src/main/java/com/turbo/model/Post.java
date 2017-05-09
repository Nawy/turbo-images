package com.turbo.model;

import org.springframework.data.cassandra.mapping.PrimaryKey;
import org.springframework.data.cassandra.mapping.Table;

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
    private String[] picturePaths;
    private Comment[] comments;

    public Post() {
    }

    public Post(Long id, String name, String description, int rating, int viewsAmount, String[] picturePaths, Comment[] comments) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.rating = rating;
        this.viewsAmount = viewsAmount;
        this.picturePaths = picturePaths;
        this.comments = comments;
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

    public String[] getPicturePaths() {
        return picturePaths;
    }

    public Comment[] getComments() {
        return comments;
    }
}
