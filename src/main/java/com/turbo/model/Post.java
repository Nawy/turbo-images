package com.turbo.model;

import java.util.List;

/**
 * Created by rakhmetov on 09.05.17.
 * <p>
 * Just post on site with picture and comments
 */
public class Post {

    private Long id;
    private String name;
    private String description;
    private int rating; // upvotes - downvotes
    private int viewsAmount; // how often people view this post
    private List<String> picturePaths; // max 20
    private ClientType clientType;
    private List<String> tag;
    private long authorId;

    public Post(
            Long id,
            String name,
            String description,
            int rating,
            int viewsAmount,
            List<String> picturePaths,
            ClientType clientType,
            List<String> tag,
            long authorId
    ) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.rating = rating;
        this.viewsAmount = viewsAmount;
        this.picturePaths = picturePaths;
        this.clientType = clientType;
        this.tag = tag;
        this.authorId = authorId;
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

    public ClientType getClientType() {
        return clientType;
    }

    public List<String> getTag() {
        return tag;
    }

    public long getAuthorId() {
        return authorId;
    }
}
