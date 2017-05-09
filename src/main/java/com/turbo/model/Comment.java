package com.turbo.model;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by rakhmetov on 09.05.17.
 */
public class Comment {

    private long authorId;
    private ClientType authorDevice; // from what was posted
    private String content;
    private LocalDateTime postTime;
    private int rating; // upvotes - downvotes
    private List<Comment> replies; // what other people reply on that comment

    public Comment(long authorId, ClientType authorDevice, String content, LocalDateTime postTime, int rating, List<Comment> replies) {
        this.authorId = authorId;
        this.authorDevice = authorDevice;
        this.content = content;
        this.postTime = postTime;
        this.rating = rating;
        this.replies = replies;
    }

    public long getAuthorId() {
        return authorId;
    }

    public ClientType getAuthorDevice() {
        return authorDevice;
    }

    public String getContent() {
        return content;
    }

    public LocalDateTime getPostTime() {
        return postTime;
    }

    public int getRating() {
        return rating;
    }

    public List<Comment> getReplies() {
        return replies;
    }
}
