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
    private String username;

    public Session() {
    }

    public Session(String username) {
        this.username = username;
    }

    public Session(Long id, String username) {
        this.id = id;
        this.username = username;
    }

    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public DeviceType getDeviceType() {
        return deviceType;
    }

    public String getLastIp() {
        return lastIp;
    }
}
