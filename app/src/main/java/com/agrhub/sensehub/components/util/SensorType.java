package com.agrhub.sensehub.components.util;

/**
 * Created by tanca on 10/18/2017.
 */

public enum SensorType {
    SENSOR_TYPE_UNKNOWN(0),
    SENSOR_TYPE_LIGHT(1),
    SENSOR_TYPE_AIR_TEMP(2),
    SENSOR_TYPE_AIR_HUMIDITY(3),
    SENSOR_TYPE_SOIL_TEMP(4),
    SENSOR_TYPE_SOIL_HUMIDITY(5),
    SENSOR_TYPE_SOIL_EC(6),
    SENSOR_TYPE_SOIL_PH(7),
    SENSOR_TYPE_WATER_TEMP(8),
    SENSOR_TYPE_WATER_EC(9),
    SENSOR_TYPE_WATER_PH(10),
    SENSOR_TYPE_WATER_ORP(11),
    SENSOR_TYPE_BAT(12),
    SENSOR_TYPE_CO2(13),
    SENSOR_TYPE_WATER_LEVEL(14),
    SENSOR_TYPE_WATER_DETECT(15),
    SENSOR_TYPE_ERROR_DETECT(16);

    private final int value;
    private SensorType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
