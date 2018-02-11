package com.turbo.model;

import lombok.Data;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.apache.commons.lang3.ObjectUtils.firstNonNull;

/**
 * Created by rakhmetov on 09.05.17.
 * <p>
 * Just post on site with picture and comments
 */
@Data
@Getter
public class Post {

    private Long id;
    private String name;
    private Rating rating;
    private long views;
    private Set<UserImage> images;
    private DeviceType deviceType;
    private Set<String> tags;
    private LocalDateTime createDate;
    private boolean visible;
    private User user;
    private String description;
    private Map<Long, Comment> comments;
    // key is userId
    private Map<Long, RatingStatus> ratingHistory = new HashMap<>(); //who changed history and how

    public Post(
            Long id,
            String name,
            Rating rating,
            long views,
            Set<UserImage> images,
            DeviceType deviceType,
            Set<String> tags,
            User user,
            LocalDateTime createDate,
            boolean visible,
            String description,
            Map<Long, Comment> comments,
            Map<Long, RatingStatus> ratingHistory
    ) {
        this.id = id;
        this.name = name;
        this.rating = firstNonNull(rating, new Rating());
        this.views = views;
        this.images = firstNonNull(images, Collections.emptySet());
        this.deviceType = firstNonNull(deviceType, DeviceType.UNKNOWN);
        this.tags = firstNonNull(tags, Collections.emptySet());
        this.user = user;
        this.visible = visible;
        this.description = description;
        this.createDate = firstNonNull(createDate, LocalDateTime.now());
        this.comments = firstNonNull(comments, Collections.emptyMap());
        this.ratingHistory = firstNonNull(ratingHistory, Collections.emptyMap());
    }
}
