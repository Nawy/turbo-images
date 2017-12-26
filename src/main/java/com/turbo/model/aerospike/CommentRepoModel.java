package com.turbo.model.aerospike;

import com.turbo.model.DeviceType;
import com.turbo.model.IdHolder;
import com.turbo.model.comment.CommentReplyType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

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
    @Setter
    private long repliesAmount; // how much child comments

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        CommentRepoModel that = (CommentRepoModel) o;

        return new EqualsBuilder()
                .append(userId, that.userId)
                .append(replyId, that.replyId)
                .append(id, that.id)
                .append(replyType, that.replyType)
                .append(device, that.device)
                .append(content, that.content)
                .append(creationDate, that.creationDate)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(id)
                .append(userId)
                .append(replyId)
                .append(replyType)
                .append(device)
                .append(content)
                .append(creationDate)
                .toHashCode();
    }
}
