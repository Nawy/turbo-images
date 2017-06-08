package com.turbo.model.search.content;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.turbo.model.DeviceType;
import com.turbo.model.Post;
import com.turbo.model.UserImage;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.google.common.base.MoreObjects.firstNonNull;

/**
 * Created by ermolaev on 5/27/17.
 * Needs for send enough data in search engine
 */
public class PostSearchEntity {

    private Long id;
    private String name;
    private Long ups;
    private Long downs;
    private Long rating;
    private Long views;
    private DeviceType deviceType;
    private List<String> descriptions;
    private List<String> tags;
    private LocalDateTime createDate;
    private String authorId;

    public PostSearchEntity(
            @JsonProperty("id") Long id,
            @JsonProperty("name") String name,
            @JsonProperty("descriptions") List<String> descriptions,
            @JsonProperty("device_type") DeviceType deviceType,
            @JsonProperty("tags") List<String> tags,
            @JsonProperty("author_id") String authorId,
            @JsonProperty("ups") Long ups,
            @JsonProperty("downs") Long downs,
            @JsonProperty("rating") Long rating,
            @JsonProperty("views") Long views,
            @JsonProperty("create_date") @JsonFormat(pattern = "yyyy-MM-dd HH:mm") LocalDateTime createDate
    ) {
        this.id = id;
        this.name = name;
        this.descriptions = descriptions;
        this.deviceType = deviceType;
        this.tags = tags;
        this.authorId = authorId;
        this.ups = ups;
        this.downs = downs;
        this.rating = rating;
        this.views = views;
        this.createDate = createDate;
    }

    public PostSearchEntity(final Post post) {
        this.id = post.getId();
        this.name = post.getName();
        this.descriptions = post.getImages()
                .stream()
                .map(UserImage::getDescription)
                .collect(Collectors.toList());
        this.deviceType = post.getDeviceType();
        this.tags = post.getTags();
        this.authorId = post.getAuthorId();
        this.ups = post.getUps();
        this.downs = post.getDowns();
        this.rating = post.getRating();
        this.views = post.getViews();
        this.createDate = post.getCreateDate();
    }

    @JsonProperty("id")
    public Long getId() {
        return id;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("descriptions")
    public List<String> getDescriptions() {
        return descriptions;
    }

    @JsonProperty("device_type")
    public DeviceType getDeviceType() {
        return deviceType;
    }

    @JsonProperty("tags")
    public List<String> getTags() {
        return tags;
    }

    @JsonProperty("author_id")
    public String getAuthorId() {
        return authorId;
    }

    @JsonProperty("ups")
    public Long getUps() {
        return ups;
    }

    @JsonProperty("downs")
    public Long getDowns() {
        return downs;
    }

    @JsonProperty("rating")
    public Long getRating() {
        return rating;
    }

    @JsonProperty("views")
    public Long getViews() {
        return views;
    }

    @JsonProperty("create_date")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    public LocalDateTime getCreateDate() {
        return createDate;
    }

    /*@JsonIgnore
    public Post toPost(){

    }*/
}
