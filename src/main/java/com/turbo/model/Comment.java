package com.turbo.model;

import com.turbo.model.aerospike.CommentRepoModel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Implements serializable for Aerospike serialization
 */
@Getter
@AllArgsConstructor
public class Comment {

    private Long id;
    private User user;
    private Long replyId; // parent comment id
    private DeviceType device; // from what was posted
    private String content;
    private final LocalDateTime creationDate;
    private Rating rating = new Rating();
    private boolean deleted;
    // key is userId
    private Map<Long, RatingStatus> ratingHistory = new HashMap<>(); //who changed history and how

    public Comment(CommentRepoModel commentRepoModel, User user) {
        this(
                commentRepoModel.getId(),
                user,
                commentRepoModel.getReplyId(),
                commentRepoModel.getDevice(),
                commentRepoModel.getContent(),
                commentRepoModel.getCreationDate(),
                commentRepoModel.getRating(),
                commentRepoModel.isDeleted(),
                commentRepoModel.getRatingHistory()
        );
    }
}
