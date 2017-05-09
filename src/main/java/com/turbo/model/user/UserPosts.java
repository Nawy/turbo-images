package com.turbo.model.user;

import org.springframework.data.cassandra.mapping.PrimaryKey;
import org.springframework.data.cassandra.mapping.Table;

/**
 * Created by rakhmetov on 09.05.17.
 */
@Table("user_posts")
public class UserPosts {

    @PrimaryKey
    private long userId;
    private long[] userPostIds; // user posts

    public UserPosts(long userId, long[] userPostIds) {
        this.userId = userId;
        this.userPostIds = userPostIds;
    }

    public long getUserId() {
        return userId;
    }

    public long[] getUserPostIds() {
        return userPostIds;
    }
}
