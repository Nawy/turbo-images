package com.turbo.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

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
    private Rating rating;
    private boolean deleted;

}
