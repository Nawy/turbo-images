package com.turbo.model.aerospike;

import com.turbo.model.IdHolder;
import com.turbo.model.UserImage;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * For contentSavingOnly
 */
public class UserImageContent implements Serializable, IdHolder {

    private Long id;
    private long imageId;
    private String description;
    private String username;
    private LocalDateTime createDate;

    public UserImageContent(UserImage userImage) {
        this.id = userImage.getId();
        this.imageId = userImage.getImage().getId();
        this.description = userImage.getDescription();
        this.username = userImage.getUsername();
        this.createDate = userImage.getCreationDate();
    }

    public UserImageContent(Long id, long imageId, String description, String username, LocalDateTime createDate) {
        this.id = id;
        this.imageId = imageId;
        this.description = description;
        this.username = username;
        this.createDate = createDate;
    }

    public UserImageContent(long imageId, String username, String description, LocalDateTime createDate) {
        this.imageId = imageId;
        this.description = description;
        this.username = username;
        this.createDate = createDate;
    }

    protected UserImageContent() {
    }

    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public long getImageId() {
        return imageId;
    }

    public String getDescription() {
        return description;
    }

    public String getUsername() {
        return username;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }
}
