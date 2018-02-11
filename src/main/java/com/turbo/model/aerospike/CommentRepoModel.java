package com.turbo.model.aerospike;

import com.turbo.model.DeviceType;
import com.turbo.model.IdHolder;
import com.turbo.model.Rating;
import com.turbo.model.RatingStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CommentRepoModel implements Serializable, IdHolder {

    @Setter
    private Long id;
    private Long userId;
    private Long replyId; // parent comment id
    private DeviceType device = DeviceType.UNKNOWN; // from what was posted
    @Setter
    private String content;
    private LocalDateTime creationDate;
    @Setter
    private Rating rating = new Rating();
    @Setter
    private boolean deleted;

    // key is userId
    @Setter
    private Map<Long, RatingStatus> ratingHistory = new HashMap<>(); // who and how changed rating

}
