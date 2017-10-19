package com.agrhub.sensehub.components.util;

/**
 * Created by tanca on 10/18/2017.
 */

public enum DeviceType {
    DB_DEVICE_TYPE_UNKNOWN(0),
    DB_DEVICE_TYPE_SENSOR(1),
    DB_DEVICE_TYPE_CONTROLLER(2),
    DB_DEVICE_TYPE_CONTAINER(3),
    DB_DEVICE_TYPE_GATEWAY(4);

    private final int value;
    private DeviceType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
