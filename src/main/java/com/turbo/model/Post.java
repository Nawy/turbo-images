package com.turbo.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.apache.commons.lang3.ObjectUtils.firstNonNull;

/**
 * Created by rakhmetov on 09.05.17.
 * <p>
 * Just post on site with picture and comments
 */
public class Post implements IdHolder, ElasticIdentifier {

    private String searchId;
    private String id;
    private String name;
    private String description;
    private int rating;
    private int viewCount;
    private List<String> picturePaths;
    private ClientType clientType;
    private List<String> tags;
    private LocalDateTime createDate;

    private String authorId;

    public Post(
            @JsonProperty(value = "id") String id,
            @JsonProperty("search_id") String searchId,
            @JsonProperty(value = "name", required = true) String name,
            @JsonProperty(value = "description", required = true) String description,
            @JsonProperty(value = "rating", required = true) int rating,
            @JsonProperty(value = "view_count", required = true) int viewCount,
            @JsonProperty(value = "picture_paths", required = true) List<String> picturePaths,
            @JsonProperty(value = "client_type", required = true) ClientType clientType,
            @JsonProperty(value = "tags", required = true) List<String> tags,
            @JsonProperty(value = "author_id", required = true) String authorId,
            @JsonProperty("create_date") @JsonFormat(pattern = "yyyy-MM-dd HH:mm") LocalDateTime createDate
    ) {
        this.id = id;
        this.searchId = searchId;
        this.name = name;
        this.description = description;
        this.rating = rating;
        this.viewCount = viewCount;
        this.picturePaths = picturePaths;
        this.clientType = clientType;
        this.tags = tags;
        this.authorId = authorId;
        this.createDate = firstNonNull(createDate, LocalDateTime.now());
    }

    @JsonProperty("id")
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
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
    public String getAuthorId() {
        return authorId;
    }

    @Override
    @JsonProperty("search_id")
    public String getSearchId() {
        return this.searchId;
    }

    @Override
    public void setSearchId(String searchId) {
        this.searchId = searchId;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Post post = (Post) o;

        return new EqualsBuilder()
                .append(authorId, post.authorId)
                .append(id, post.id)
                .append(name, post.name)
                .append(picturePaths, post.picturePaths)
                .append(tags, post.tags)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(id)
                .append(name)
                .append(picturePaths)
                .append(tags)
                .append(authorId)
                .toHashCode();
    }
}
