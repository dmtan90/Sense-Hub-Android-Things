package com.agrhub.sensehub.components.util;

/**
 * Created by tanca on 10/18/2017.
 */

public enum ControllerType {
    DEVICE_CMD_UNKNOW(0),
    DEVICE_CMD_LAMP(1),
    DEVICE_CMD_PUMP(2),
    DEVICE_CMD_MISTING(3),
    DEVICE_CMD_FAN(4),
    DEVICE_CMD_AIR_CONDITIONER(5),
    DEVICE_CMD_CO2(6),
    DEVICE_CMD_DOSING_PUMP(7),
    DEVICE_CMD_OXYGEN_PUMP(8),
    DEVICE_CMD_VALVE_INPUT(9),
    DEVICE_CMD_VALVE_OUTPUT(10),
    DEVICE_CMD_HYDRO_TANK_WASHING(11),
    DEVICE_CMD_HYDRO_TANK_CALIBRATE(12);

    private final int value;
    private ControllerType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static ControllerType getControllerType(int value){
        ControllerType mType = DEVICE_CMD_UNKNOW;
        for(ControllerType type : ControllerType.values()){
            if(type.getValue() == value){
                mType = type;
                break;
            }
        }
        return mType;
    }
}
