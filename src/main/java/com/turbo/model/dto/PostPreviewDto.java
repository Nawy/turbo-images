package com.turbo.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.turbo.model.DeviceType;
import com.turbo.model.Post;
import com.turbo.model.Rating;
import com.turbo.model.UserImage;
import com.turbo.util.EncryptionService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;
import java.util.Set;

import static org.apache.commons.lang3.ObjectUtils.firstNonNull;

/**
 * Created by ermolaev on 6/12/17.
 */
@Getter
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class PostPreviewDto {

    private String id;
    private String name;
    private String description;
    private Rating rating;
    private long views;
    private String previewImage;
    private DeviceType deviceType;
    private Set<String> tags;
    private LocalDateTime createDate;

    @JsonIgnore
    public static PostPreviewDto from(Post post) {
        final String imageDescription = post.getImages()
                .stream()
                .filter(image -> StringUtils.isNotBlank(image.getDescription()))
                .map(UserImage::getDescription)
                .findFirst().orElse(null);

        return new PostPreviewDto(
                EncryptionService.encodeHashId(post.getId()),
                post.getName(),
                firstNonNull(
                        post.getDescription(),
                        imageDescription,
                        ""
                ),
                post.getRating(),
                post.getViews(),
                post.getImages().stream().findFirst().get().getImage().getThumbnail(),
                post.getDeviceType(),
                post.getTags(),
                post.getCreateDate()
        );
    }

    @JsonProperty("create_date")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    public LocalDateTime getCreateDate() {
        return createDate;
    }
}
