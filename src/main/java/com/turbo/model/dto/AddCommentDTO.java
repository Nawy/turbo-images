package com.turbo.model.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.turbo.model.DeviceType;
import com.turbo.model.Rating;
import com.turbo.model.aerospike.CommentRepoModel;
import com.turbo.util.EncryptionService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;

@Getter
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@AllArgsConstructor
public class AddCommentDTO {

    private String postId;
    private String replyId;
    private DeviceType device; // from what was posted
    private String content;

    public CommentRepoModel toRepoModel(long userId) {
        return new CommentRepoModel(
                null,
                userId,
                StringUtils.isBlank(this.replyId) ? null :  EncryptionService.decodeHashId(this.replyId),
                this.device,
                this.content,
                LocalDateTime.now(),
                new Rating()
        );
    }

    public Long getDecodedPostId(){
       return StringUtils.isBlank(this.postId) ? null : EncryptionService.decodeHashId(this.postId);
    }

}
