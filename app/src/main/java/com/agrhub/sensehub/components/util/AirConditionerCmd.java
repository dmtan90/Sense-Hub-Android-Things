package com.agrhub.sensehub.components.util;

/**
 * Created by tanca on 10/29/2017.
 */

public enum AirConditionerCmd {
    AIR_CONDITIONER_CMD_UNKNOWN(-1),
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

    public static AirConditionerCmd getAcCmdFromString(String cmd){
        AirConditionerCmd mAcCmd = AIR_CONDITIONER_CMD_UNKNOWN;
        if(cmd == null){
            return mAcCmd;
        }
        if(cmd.equals("cmd_on")){
            mAcCmd = AIR_CONDITIONER_CMD_PWR_ON;
        }
        else if(cmd.equals("cmd_off")){
            mAcCmd = AIR_CONDITIONER_CMD_PWR_OFF;
        }
        else if(cmd.equals("cmd_temp_16")){
            mAcCmd = AIR_CONDITIONER_CMD_TMP_16;
        }
        else if(cmd.equals("cmd_temp_18")){
            mAcCmd = AIR_CONDITIONER_CMD_TMP_18;
        }
        else if(cmd.equals("cmd_temp_20")){
            mAcCmd = AIR_CONDITIONER_CMD_TMP_20;
        }
        else if(cmd.equals("cmd_temp_22")){
            mAcCmd = AIR_CONDITIONER_CMD_TMP_22;
        }
        else if(cmd.equals("cmd_temp_24")){
            mAcCmd = AIR_CONDITIONER_CMD_TMP_24;
        }
        else if(cmd.equals("cmd_temp_26")){
            mAcCmd = AIR_CONDITIONER_CMD_TMP_26;
        }
        else if(cmd.equals("cmd_temp_28")){
            mAcCmd = AIR_CONDITIONER_CMD_TMP_28;
        }
        else if(cmd.equals("cmd_temp_30")){
            mAcCmd = AIR_CONDITIONER_CMD_TMP_30;
        }
        return mAcCmd;
    }
}
