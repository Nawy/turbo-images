package com.turbo.model;

import org.springframework.data.cassandra.mapping.PrimaryKey;
import org.springframework.data.cassandra.mapping.Table;

/**
 * Created by ermolaev on 5/6/17.
 *
 * This data for every foreign user
 */
@Table("user_public")
public class UserPublicData {

    @PrimaryKey
    private Long id;
    private String nickname;
    private String avatarPath;

    private long[] userPostIds; // user posts

    public UserPublicData() {
    }

    public UserPublicData(Long id, String nickname, String avatarPath, long[] userPostIds) {
        this.id = id;
        this.nickname = nickname;
        this.avatarPath = avatarPath;
        this.userPostIds = userPostIds;
    }

    public Long getId() {
        return id;
    }

    public String getNickname() {
        return nickname;
    }

    public String getAvatarPath() {
        return avatarPath;
    }

    public long[] getUserPostIds() {
        return userPostIds;
    }
}
