package com.turbo.model;

import lombok.Data;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Collections;
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
            Map<Long, Comment> comments
    ) {
        this.id = id;
        this.name = name;
        this.rating = rating;
        this.views = views;
        this.images = images == null ? Collections.emptySet() : Collections.unmodifiableSet(images);
        this.deviceType = deviceType;
        this.tags = tags == null ? Collections.emptySet() : Collections.unmodifiableSet(tags);
        this.user = user;
        this.visible = visible;
        this.description = description;
        this.createDate = firstNonNull(createDate, LocalDateTime.now());
        this.comments = comments == null ? Collections.emptyMap() : Collections.unmodifiableMap(comments);
    }
}
