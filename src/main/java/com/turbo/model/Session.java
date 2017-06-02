package com.turbo.model;

import java.io.Serializable;

/**
 * Implements serializable for Aerospike serialization
 */
public class Session implements Serializable,IdHolder {

    private final static long serialVersionUID = 42L;

    private Long id;
    private DeviceType deviceType;
    private String lastIp;
    private User user;

    public Session() {
    }

    public Session(User user) {
        this.user = user;
    }

    public Session(Long id, User user) {
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

    public DeviceType getDeviceType() {
        return deviceType;
    }

    public String getLastIp() {
        return lastIp;
    }
}
