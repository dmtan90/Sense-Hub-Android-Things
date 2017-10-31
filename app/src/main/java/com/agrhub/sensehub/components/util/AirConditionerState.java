package com.agrhub.sensehub.components.util;

/**
 * Created by tanca on 10/29/2017.
 */

public class AirConditionerState {
    private AirConditionerPower mAcPower;
    private AirConditionerMode mAcMode;

    public AirConditionerState(){
        this.mAcPower = AirConditionerPower.AIR_CONDITIONER_POWER_LEAVE;
        this.mAcMode = AirConditionerMode.AIR_CONDITIONER_MODE_NORMAL;
    }

    public AirConditionerPower getAcPower() {
        return mAcPower;
    }

    public void setAcPower(AirConditionerPower mAcPower) {
        this.mAcPower = mAcPower;
    }

    public AirConditionerMode getAcMode() {
        return mAcMode;
    }

    public void setAcMode(AirConditionerMode mAcMode) {
        this.mAcMode = mAcMode;
    }
}
