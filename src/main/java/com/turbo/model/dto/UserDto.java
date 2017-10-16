package com.turbo.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.turbo.model.User;
import com.turbo.util.EncryptionService;

import java.time.LocalDateTime;

public class UserDto {

    private String id;
    private String name; // should be unique!
    private String avatarPath;
    private LocalDateTime createDate;

    public UserDto(
            String id,
            String name,
            String avatarPath,
            LocalDateTime createDate
    ) {
        this.id = id;
        this.name = name;
        this.avatarPath = avatarPath;
        this.createDate = createDate;
    }

    @JsonProperty(value = "id")
    public String getId() {
        return id;
    }

    @JsonProperty(value = "name")
    public String getName() {
        return name;
    }

    @JsonProperty(value = "avatar_path")
    public String getAvatarPath() {
        return avatarPath;
    }

    @JsonProperty("create_date")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public static UserDto from(final User user) {
        return new UserDto(
                EncryptionService.encodeHashId(user.getId()),
                user.getName(),
                user.getAvatarPath(),
                user.getCreateDate()
        );
    }
}
