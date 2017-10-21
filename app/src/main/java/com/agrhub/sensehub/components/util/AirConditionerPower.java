package com.agrhub.sensehub.components.util;

/**
 * Created by tanca on 10/18/2017.
 */

public enum AirConditionerPower {
    AIR_CONDITIONER_POWER_LEAVE(0),
    AIR_CONDITIONER_POWER_ON(1),
    AIR_CONDITIONER_POWER_OFF(2);

    private final int value;
    private AirConditionerPower(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public String getValueString(){
        String sValue = "";
        switch (value){
            case 0:
                sValue = "leave";
                break;
            case 1:
                sValue = "on";
                break;
            case 2:
                sValue = "off";
                break;
        }
        return sValue;
    }
}
