package com.turbo.model.aerospike;

import com.turbo.model.DeviceType;
import com.turbo.model.IdHolder;
import com.turbo.model.Comment;
import com.turbo.model.Rating;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CommentRepoModel implements Serializable, IdHolder {

    @Setter
    private Long id;
    private Long userId;
    private Long replyId; // parent comment id
    private DeviceType device; // from what was posted
    private String content;
    private LocalDateTime creationDate;
    private Rating rating;

}
