package com.turbo.model;

import com.turbo.model.user.User;
import org.springframework.data.cassandra.mapping.PrimaryKey;
import org.springframework.data.cassandra.mapping.Table;

/**
 * Created by rakhmetov on 09.05.17.
 */
@Table("session")
public class Session {

    @PrimaryKey
    private long id;
    private User user;

    public Session() {
    }

    public Session(User user) {
        this.user = user;
    }

    public long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }
}
