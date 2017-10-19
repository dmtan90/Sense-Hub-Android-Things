package com.agrhub.sensehub.components.util;

/**
 * Created by tanca on 10/18/2017.
 */

public enum AirConditionerMode {
    AIR_CONDITIONER_MODE_NORMAL(0),
    AIR_CONDITIONER_MODE_TEMP_16(1),
    AIR_CONDITIONER_MODE_TEMP_18(2),
    AIR_CONDITIONER_MODE_TEMP_20(3),
    AIR_CONDITIONER_MODE_TEMP_22(4),
    AIR_CONDITIONER_MODE_TEMP_24(5),
    AIR_CONDITIONER_MODE_TEMP_26(6),
    AIR_CONDITIONER_MODE_TEMP_28(7),
    AIR_CONDITIONER_MODE_TEMP_30(8);

    private final int value;
    private AirConditionerMode(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
