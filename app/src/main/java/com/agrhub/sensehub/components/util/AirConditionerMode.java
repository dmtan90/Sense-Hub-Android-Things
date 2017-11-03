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

    public String getValueString(){
        String sValue = "";
        switch (value){
            case 0:
                sValue = "normal";
                break;
            case 1:
                sValue = "16";
                break;
            case 2:
                sValue = "18";
                break;
            case 3:
                sValue = "20";
                break;
            case 4:
                sValue = "22";
                break;
            case 5:
                sValue = "24";
                break;
            case 6:
                sValue = "26";
                break;
            case 7:
                sValue = "28";
                break;
            case 8:
                sValue = "30";
                break;
        }
        return sValue;
    }

    public static AirConditionerMode getAcModeFromString(String mode){
        AirConditionerMode mMode = AIR_CONDITIONER_MODE_NORMAL;
        if(mode == null){
            return mMode;
        }
        if(mode.equals("cmd_temp_16")){
            mMode = AIR_CONDITIONER_MODE_TEMP_16;
        }
        else if(mode.equals("cmd_temp_18")){
            mMode = AIR_CONDITIONER_MODE_TEMP_18;
        }
        else if(mode.equals("cmd_temp_20")){
            mMode = AIR_CONDITIONER_MODE_TEMP_20;
        }
        else if(mode.equals("cmd_temp_22")){
            mMode = AIR_CONDITIONER_MODE_TEMP_22;
        }
        else if(mode.equals("cmd_temp_24")){
            mMode = AIR_CONDITIONER_MODE_TEMP_24;
        }
        else if(mode.equals("cmd_temp_26")){
            mMode = AIR_CONDITIONER_MODE_TEMP_26;
        }
        else if(mode.equals("cmd_temp_28")){
            mMode = AIR_CONDITIONER_MODE_TEMP_28;
        }
        else if(mode.equals("cmd_temp_30")){
            mMode = AIR_CONDITIONER_MODE_TEMP_30;
        }

        return mMode;
    }
}
