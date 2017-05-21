package com.turbo.model.comment;

import com.turbo.model.ClientType;
import com.turbo.model.IdHolder;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Implements serializable for Aerospike serialization
 */
public class Comment implements Serializable,IdHolder {

    private Long id;
    private long authorId;
    private long replyId;
    private CommentReplyType replyType;
    private ClientType authorDevice; // from what was posted
    private String content;
    private LocalDateTime postTime;
    private int rating; // upvotes - downvotes

    public Comment(
            Long id,
            long authorId,
            long replyId,
            CommentReplyType replyType,
            ClientType authorDevice,
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

    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public long getAuthorId() {
        return authorId;
    }

    public long getReplyId() {
        return replyId;
    }

    public CommentReplyType getReplyType() {
        return replyType;
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
}
