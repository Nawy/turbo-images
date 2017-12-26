package com.turbo.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.turbo.model.DeviceType;
import com.turbo.model.aerospike.CommentRepoModel;
import com.turbo.model.comment.CommentReplyType;
import com.turbo.util.EncryptionService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;

@Getter
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@AllArgsConstructor
public class CommentModificationDTO {

    private String id;
    private String userId;
    private String replyId;
    private CommentReplyType replyType;
    private DeviceType device; // from what was posted
    private String content;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private LocalDateTime creationDate;
    private long ups;
    private long downs;
    private long rating; // upvotes - downvotes

    public CommentRepoModel toRepoModel() {
        return new CommentRepoModel(
                StringUtils.isBlank(this.id) ? null : EncryptionService.decodeHashId(this.id),
                EncryptionService.decodeHashId(this.userId),
                EncryptionService.decodeHashId(this.replyId),
                this.replyType,
                this.device,
                this.content,
                this.creationDate,
                this.ups,
                this.downs,
                this.rating,
                0
        );
    }

}
