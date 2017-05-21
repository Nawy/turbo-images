package com.turbo.model;

import com.turbo.model.user.User;

import java.io.Serializable;
import java.util.UUID;

/**
 * Implements serializable for Aerospike serialization
 */
public class Session implements Serializable,IdHolder {

    private final static long serialVersionUID = 42L;

    private Long id;
    private User user;

    public Session() {
    }

    public Session(User user) {
        this.user = user;
    }

    public Session(long id, User user) {
        this.id = id;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }
}
