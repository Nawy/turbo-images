package com.turbo.model.aerospike;

import com.turbo.model.DeviceType;
import com.turbo.model.IdHolder;
import com.turbo.model.comment.CommentReplyType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CommentRepoModel implements Serializable, IdHolder {

    @Setter
    private Long id;
    private long userId;
    private long replyId;
    private CommentReplyType replyType;
    private DeviceType device; // from what was posted
    private String content;
    private LocalDateTime creationDate;
    private long ups;
    private long downs;
    private long rating; // upvotes - downvotes

}
