package com.turbo.model;

import java.io.Serializable;

/**
 * Implements serializable for Aerospike serialization
 */
public class Session implements Serializable,IdHolder {

    private final static long serialVersionUID = 42L;

    private Long id;
    private Long userId;
    private DeviceType deviceType;
    private String ip;

    public Session() {
    }

    public Session(Long id, Long userId, DeviceType deviceType, String ip) {
        this.id = id;
        this.userId = userId;
        this.deviceType = deviceType;
        this.ip = ip;
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

    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }
}
