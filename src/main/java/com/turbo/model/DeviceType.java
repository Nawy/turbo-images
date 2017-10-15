package com.turbo.model;

import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.ObjectUtils;

/**
 * Created by rakhmetov on 09.05.17.
 */
public enum DeviceType {

    IPHONE,
    ANDROID,
    WINDOWS_PHONE,
    PC, // WINDOWS OS
    MAC,
    LINUX,
    BROWSER,
    UNKNOWN;

    public static DeviceType getDeviceType(String value) {
        DeviceType deviceType = EnumUtils.getEnum(DeviceType.class, value);
        return ObjectUtils.firstNonNull(deviceType, UNKNOWN);
    }

    public static DeviceType DEFAULT = UNKNOWN;

}
