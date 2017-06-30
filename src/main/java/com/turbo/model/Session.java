package com.turbo.model;

import java.io.Serializable;

/**
 * Implements serializable for Aerospike serialization
 */
public class Session implements Serializable {

    private final static long serialVersionUID = 42L;

    private Long userId;
    private DeviceType deviceType;
    private String ip;

    public Session() {
    }

    public Session(long userId) {
        this.userId = userId;
    }

    public Session(long userId, DeviceType deviceType, String ip) {
        this.userId = userId;
        this.deviceType = deviceType;
        this.ip = ip;
    }

    public Long getUserId() {
        return userId;
    }

    public DeviceType getDeviceType() {
        return deviceType;
    }

    public String getIp() {
        return ip;
    }
}
