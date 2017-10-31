package com.agrhub.sensehub.components.util;

/**
 * Created by tanca on 10/29/2017.
 */

public enum AirConditionerCmd {
    AIR_CONDITIONER_CMD_PWR_ON(0),
    AIR_CONDITIONER_CMD_PWR_OFF(1),
    AIR_CONDITIONER_CMD_TMP_16(2),
    AIR_CONDITIONER_CMD_TMP_18(3),
    AIR_CONDITIONER_CMD_TMP_20(4),
    AIR_CONDITIONER_CMD_TMP_22(5),
    AIR_CONDITIONER_CMD_TMP_24(6),
    AIR_CONDITIONER_CMD_TMP_26(7),
    AIR_CONDITIONER_CMD_TMP_28(8),
    AIR_CONDITIONER_CMD_TMP_30(9);

    private int mValue;
    private AirConditionerCmd(int value){
        this.mValue = value;
    }

    public int getValue(){
        return this.mValue;
    }

    public String getStringValue(){
        String mStringValue = "";
        if(AIR_CONDITIONER_CMD_PWR_ON.getValue() == getValue()) {
            mStringValue = "Power On";
        }
        else if(AIR_CONDITIONER_CMD_PWR_OFF.getValue() == getValue()) {
            mStringValue = "Power Off";
        }
        else if(AIR_CONDITIONER_CMD_TMP_16.getValue() == getValue()) {
            mStringValue = "Temp 16*C";
        }
        else if(AIR_CONDITIONER_CMD_TMP_18.getValue() == getValue()) {
            mStringValue = "Temp 18*C";
        }
        else if(AIR_CONDITIONER_CMD_TMP_20.getValue() == getValue()) {
            mStringValue = "Temp 20*C";
        }
        else if(AIR_CONDITIONER_CMD_TMP_22.getValue() == getValue()) {
            mStringValue = "Temp 22*C";
        }
        else if(AIR_CONDITIONER_CMD_TMP_24.getValue() == getValue()) {
            mStringValue = "Temp 24*C";
        }
        else if(AIR_CONDITIONER_CMD_TMP_26.getValue() == getValue()) {
            mStringValue = "Temp 26*C";
        }
        else if(AIR_CONDITIONER_CMD_TMP_28.getValue() == getValue()) {
            mStringValue = "Temp 28*C";
        }
        else if(AIR_CONDITIONER_CMD_TMP_30.getValue() == getValue()) {
            mStringValue = "Temp 30*C";
        }
        return mStringValue;
    }

}
