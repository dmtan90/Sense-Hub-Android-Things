package com.agrhub.sensehub.components.util;

/**
 * Created by tanca on 10/18/2017.
 */

public enum SensorType {
    SENSOR_TYPE_UNKNOWN(0),
    SENSOR_TYPE_LIGHT(2),
    SENSOR_TYPE_AIR_TEMP(3),
    SENSOR_TYPE_AIR_HUMIDITY(4),
    SENSOR_TYPE_SOIL_TEMP(5),
    SENSOR_TYPE_SOIL_HUMIDITY(6),
    SENSOR_TYPE_SOIL_EC(7),
    SENSOR_TYPE_SOIL_PH(8),
    SENSOR_TYPE_WATER_TEMP(9),
    SENSOR_TYPE_WATER_EC(10),
    SENSOR_TYPE_WATER_PH(11),
    SENSOR_TYPE_WATER_ORP(12),
    SENSOR_TYPE_BAT(13),
    SENSOR_TYPE_CO2(14),
    SENSOR_TYPE_WATER_LEVEL(15),
    SENSOR_TYPE_WATER_DETECT(16),
    SENSOR_TYPE_ERROR_DETECT(17);

    private final int value;
    private SensorType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
