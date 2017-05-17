package com.turbo.model;

import com.turbo.model.user.User;

import java.io.Serializable;
import java.util.UUID;

/**
 * Implements serializable for Aerospike serialization
 */
public class Session implements Serializable {

    private final static long serialVersionUID = 42L;

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
