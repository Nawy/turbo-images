package com.turbo.model.comment;

import com.turbo.model.DeviceType;
import com.turbo.model.IdHolder;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Implements serializable for Aerospike serialization
 */
public class Comment implements Serializable,IdHolder {

    private Long id;
    private String authorName;
    private String replyId;
    private CommentReplyType replyType;
    private DeviceType authorDevice; // from what was posted
    private String content;
    private LocalDateTime postTime;
    private int rating; // upvotes - downvotes

    public Comment(
            Long id,
            String authorName,
            String replyId,
            CommentReplyType replyType,
            DeviceType authorDevice,
            String content,
            LocalDateTime postTime,
            int rating
    ) {
        this.id = id;
        this.authorName = authorName;
        this.replyId = replyId;
        this.replyType = replyType;
        this.authorDevice = authorDevice;
        this.content = content;
        this.postTime = postTime;
        this.rating = rating;
    }

    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getAuthorName() {
        return authorName;
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
