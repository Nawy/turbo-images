package com.turbo.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.List;

/**
 * Created by rakhmetov on 09.05.17.
 * <p>
 * Just post on site with picture and comments
 */
public class Post implements IdHolder {

    private String elasticId;
    private Long id;
    private String name;
    private String description;
    private int rating;
    private int viewCount;
    private List<String> picturePaths;
    private ClientType clientType;
    private List<String> tags;
    private long authorId;

    public Post(
            @JsonProperty(value = "id", required = true) Long id,
            @JsonProperty(value = "name", required = true) String name,
            @JsonProperty(value = "description", required = true) String description,
            @JsonProperty(value = "rating", required = true) int rating,
            @JsonProperty(value = "view_count", required = true) int viewCount,
            @JsonProperty(value = "picture_paths", required = true) List<String> picturePaths,
            @JsonProperty(value = "client_type", required = true) ClientType clientType,
            @JsonProperty(value = "tags", required = true) List<String> tags,
            @JsonProperty(value = "author_id", required = true) long authorId
    ) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.rating = rating;
        this.viewCount = viewCount;
        this.picturePaths = picturePaths;
        this.clientType = clientType;
        this.tags = tags;
        this.authorId = authorId;
    }

    @JsonProperty("id")
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("description")
    public String getDescription() {
        return description;
    }

    @JsonProperty("rating")
    public int getRating() {
        return rating;
    }

    @JsonProperty("view_count")
    public int getViewCount() {
        return viewCount;
    }

    @JsonProperty("picture_paths")
    public List<String> getPicturePaths() {
        return picturePaths;
    }

    @JsonProperty("client_type")
    public ClientType getClientType() {
        return clientType;
    }

    @JsonProperty("tags")
    public List<String> getTags() {
        return tags;
    }

    @JsonProperty("author_id")
    public long getAuthorId() {
        return authorId;
    }

    public void setElasticId(String elasticId) {
        this.elasticId = elasticId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Post post = (Post) o;

        return new EqualsBuilder()
                .append(rating, post.rating)
                .append(viewCount, post.viewCount)
                .append(authorId, post.authorId)
                .append(elasticId, post.elasticId)
                .append(id, post.id)
                .append(name, post.name)
                .append(description, post.description)
                .append(picturePaths, post.picturePaths)
                .append(clientType, post.clientType)
                .append(tags, post.tags)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(elasticId)
                .append(id)
                .append(name)
                .append(description)
                .append(rating)
                .append(viewCount)
                .append(picturePaths)
                .append(clientType)
                .append(tags)
                .append(authorId)
                .toHashCode();
    }
}
