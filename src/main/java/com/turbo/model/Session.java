package com.turbo.model;

import com.turbo.model.user.User;

import java.util.UUID;

/**
 * Created by rakhmetov on 09.05.17.
 */
public class Session {

    private long id;
    private User user;

    public Session() {
    }

    public Session(User user) {
        id = UUID.randomUUID().getMostSignificantBits();
        this.user = user;
    }

    public Session(long id, User user) {
        this.id = id;
        this.user = user;
    }

    public long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }
}
