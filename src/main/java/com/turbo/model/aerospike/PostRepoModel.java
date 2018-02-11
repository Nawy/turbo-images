package com.turbo.model.aerospike;

import com.turbo.model.DeviceType;
import com.turbo.model.IdHolder;
import com.turbo.model.Rating;
import com.turbo.model.RatingStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.apache.commons.lang3.ObjectUtils.firstNonNull;

/**
 * Just post on site with picture and comments
 */
@Getter
@NoArgsConstructor
public class PostRepoModel implements Serializable, IdHolder {

    @Setter
    private Long id;
    private String name;
    @Setter
    private Rating rating = new Rating();
    @Setter
    private long views;
    private Set<Long> images; // key is UserImage_ID, value is description
    private DeviceType deviceType;
    private Set<String> tags;
    private LocalDateTime creationDateTime;
    private boolean visible;
    private long userId;
    private String description;
    @Setter
    private Map<Long, CommentRepoModel> comments; // key is id

    // key is userId
    @Setter
    private Map<Long, RatingStatus> ratingHistory = new HashMap<>(); //who changed history and how

    public PostRepoModel(
            Long id,
            String name,
            Rating rating,
            long views,
            Set<Long> images,
            DeviceType deviceType,
            Set<String> tags,
            long userId,
            LocalDateTime creationDateTime,
            boolean visible,
            String description,
            Map<Long, CommentRepoModel> comments
    ) {
        this.id = id;
        this.name = name;
        this.rating = firstNonNull(rating, new Rating());
        this.views = views;
        this.images = firstNonNull(images, Collections.emptySet());
        this.deviceType = firstNonNull(deviceType, DeviceType.UNKNOWN);
        this.tags = firstNonNull(tags, Collections.emptySet());
        this.userId = userId;
        this.visible = visible;
        this.description = description;
        this.creationDateTime = firstNonNull(creationDateTime, LocalDateTime.now());
        this.comments = firstNonNull(comments, Collections.emptyMap());
        this.ratingHistory = firstNonNull(ratingHistory, Collections.emptyMap());
    }

}
