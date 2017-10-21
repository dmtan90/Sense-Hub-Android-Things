package com.agrhub.sensehub.components.util;

/**
 * Created by tanca on 10/18/2017.
 */

public enum DeviceName {
    DB_DEVICE_NAME_UNKNOWN(0),
    DB_DEVICE_NAME_SP3_SMART_PLUG(1),
    DB_DEVICE_NAME_SONOFF_SMART_PLUG(2),
    DB_DEVICE_NAME_XIAOMI_SMART_PLUG(3),
    DB_DEVICE_NAME_MI_FLORA(4),
    DB_DEVICE_NAME_AXAET_AIR_SENSOR(5),
    DB_DEVICE_NAME_TI_TAG_SENSOR(6),
    DB_DEVICE_NAME_FIOT_SMART_TANK(7),
    DB_DEVICE_NAME_RM3_SMART_REMOTE(8),
    DB_DEVICE_NAME_GATEWAY(9),
    DB_DEVICE_NAME_NRF51822_SENSOR(10),
    DB_DEVICE_NAME_GATEWAY_ANDROID_THINGS(11);

    private final int value;
    private DeviceName(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
