package com.turbo.model.comment;

import com.turbo.model.DeviceType;
import com.turbo.model.IdHolder;
import com.turbo.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Implements serializable for Aerospike serialization
 */
@Getter
@Setter
@AllArgsConstructor
public class Comment {

    private Long id;
    private User user;
    private long replyId;
    private CommentReplyType replyType;
    private DeviceType device; // from what was posted
    private String content;
    private final LocalDateTime creationDate;
    private long ups;
    private long downs;
    private long rating; // upvotes - downvotes

}
