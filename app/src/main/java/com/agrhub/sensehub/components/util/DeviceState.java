package com.agrhub.sensehub.components.util;

/**
 * Created by tanca on 10/18/2017.
 */

public enum DeviceState {
    DEVICE_CONNECTED(0),
    DEVICE_DISCONNECTED(1),
    DEVICE_ERROR(2);

    private final int value;
    private DeviceState(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
