package com.agrhub.sensehub.components.entity;

import com.agrhub.sensehub.components.util.ControllerData;
import com.agrhub.sensehub.components.util.ControllerState;
import com.agrhub.sensehub.components.util.ControllerType;
import com.agrhub.sensehub.components.util.DeviceName;
import com.agrhub.sensehub.components.util.DeviceState;
import com.agrhub.sensehub.components.util.DeviceType;

/**
 * Created by tanca on 10/19/2017.
 */

public class Sp3SmartPlugEntity extends Entity{
    private ControllerData mController;

    public Sp3SmartPlugEntity(){
        super();
        setDeviceName(DeviceName.DB_DEVICE_NAME_SP3_SMART_PLUG);
        setDeviceState(DeviceState.DEVICE_DISCONNECTED);
        setDeviceType(DeviceType.DB_DEVICE_TYPE_CONTROLLER);

        this.mController.mControllerType = ControllerType.DEVICE_CMD_UNKNOW;
        this.mController.mControllerState = ControllerState.CONTROLLER_STATE_UNKNOWN;
    }

    public ControllerType getControllerType() {
        return mController.mControllerType;
    }

    public void setControllerType(ControllerType mControllerType) {
        this.mController.mControllerType = mControllerType;
    }

    public ControllerState getControllerState() {
        return mController.mControllerState;
    }

    public void setControllerState(ControllerState mControllerState) {
        this.mController.mControllerState = mControllerState;
    }

    @Override
    public String getData() {
        return String.format("[{\"controller_type\":%d,\"controller_is_on\":%s}]",
                getControllerType().getValue(),
                getControllerState().getValueString());
    }

    @Override
    public void updateData() {

    }
}
