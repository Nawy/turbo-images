package com.turbo.model.comment;

import com.turbo.model.DeviceType;
import com.turbo.model.IdHolder;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Implements serializable for Aerospike serialization
 */
public class Comment implements Serializable,IdHolder {

    private String id;
    private String authorId;
    private String replyId;
    private CommentReplyType replyType;
    private DeviceType authorDevice; // from what was posted
    private String content;
    private LocalDateTime postTime;
    private int rating; // upvotes - downvotes

    public Comment(
            String id,
            String authorId,
            String replyId,
            CommentReplyType replyType,
            DeviceType authorDevice,
            String content,
            LocalDateTime postTime,
            int rating
    ) {
        this.id = id;
        this.authorId = authorId;
        this.replyId = replyId;
        this.replyType = replyType;
        this.authorDevice = authorDevice;
        this.content = content;
        this.postTime = postTime;
        this.rating = rating;
    }

    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public String getAuthorId() {
        return authorId;
    }

    public String getReplyId() {
        return replyId;
    }

    public CommentReplyType getReplyType() {
        return replyType;
    }

    public DeviceType getAuthorDevice() {
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
}
