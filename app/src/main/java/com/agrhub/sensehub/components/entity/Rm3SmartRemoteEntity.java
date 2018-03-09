package com.agrhub.sensehub.components.entity;

import com.agrhub.sensehub.components.util.AirConditionerMode;
import com.agrhub.sensehub.components.util.AirConditionerPower;
import com.agrhub.sensehub.components.util.ControllerType;
import com.agrhub.sensehub.components.util.DeviceName;
import com.agrhub.sensehub.components.util.DeviceState;
import com.agrhub.sensehub.components.util.DeviceType;

/**
 * Created by tanca on 10/19/2017.
 */

public class Rm3SmartRemoteEntity extends Sp3SmartPlugEntity {
    private AirConditionerPower mACPower;
    private AirConditionerMode mACMode;
    public Rm3SmartRemoteEntity(){
        super();
        setDeviceName(DeviceName.DB_DEVICE_NAME_RM3_SMART_REMOTE);
        setDeviceState(DeviceState.DEVICE_DISCONNECTED);
        setDeviceType(DeviceType.DB_DEVICE_TYPE_CONTROLLER);
        this.mACPower = AirConditionerPower.AIR_CONDITIONER_POWER_OFF;
        this.mACMode = AirConditionerMode.AIR_CONDITIONER_MODE_NORMAL;
        setControllerType(ControllerType.DEVICE_CMD_AIR_CONDITIONER);
    }

    public AirConditionerPower getACPower() {
        return mACPower;
    }

    public void setACPower(AirConditionerPower mACPower) {
        this.mACPower = mACPower;
    }

    public AirConditionerMode getACMode() {
        return mACMode;
    }

    public void setACMode(AirConditionerMode mACMode) {
        this.mACMode = mACMode;
    }

    @Override
    public String getData() {
        return String.format("[{\"controller_type\":%d,\"controller_is_on\":%s,\"air_conditioner_power\":\"%s\",\"air_conditioner_temp\":\"%s\"}]",
                getControllerType().getValue(),
                getControllerState().getValueString(),
                getACPower().getValueString(),
                getACMode().getValueString());
    }

    @Override
    public void updateData() {


    }
}
