package com.turbo.model.user;

import org.springframework.data.cassandra.mapping.PrimaryKey;
import org.springframework.data.cassandra.mapping.Table;

import java.util.List;

/**
 * Created by rakhmetov on 09.05.17.
 */
@Table("user_posts")
public class UserPosts {

    @PrimaryKey
    private long userId;
    private List<Long> userPostIds; // user posts

    public UserPosts() {
    }

    public UserPosts(long userId, List<Long> userPostIds) {
        this.userId = userId;
        this.userPostIds = userPostIds;
    }

    public long getUserId() {
        return userId;
    }

    public List<Long> getUserPostIds() {
        return userPostIds;
    }
}
