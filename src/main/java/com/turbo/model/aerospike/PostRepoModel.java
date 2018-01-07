package com.turbo.model.aerospike;

import com.turbo.model.DeviceType;
import com.turbo.model.IdHolder;
import com.turbo.model.Rating;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collections;
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
    private Rating rating;
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
        this.rating = rating;
        this.views = views;
        this.images = images == null ? Collections.emptySet() : Collections.unmodifiableSet(images);
        this.deviceType = deviceType;
        this.tags = tags == null ? Collections.emptySet() : Collections.unmodifiableSet(tags);
        this.userId = userId;
        this.visible = visible;
        this.description = description;
        this.creationDateTime = firstNonNull(creationDateTime, LocalDateTime.now());
        this.comments = comments == null ? Collections.emptyMap() : Collections.unmodifiableMap(comments);
    }

}
