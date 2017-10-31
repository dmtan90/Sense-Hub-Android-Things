package com.agrhub.sensehub.components.entity;

import com.agrhub.sensehub.components.connector.BroadlinkConnector;
import com.agrhub.sensehub.components.connector.Sp3Connector;
import com.agrhub.sensehub.components.util.ControllerData;
import com.agrhub.sensehub.components.util.ControllerState;
import com.agrhub.sensehub.components.util.ControllerType;
import com.agrhub.sensehub.components.util.DeviceName;
import com.agrhub.sensehub.components.util.DeviceState;
import com.agrhub.sensehub.components.util.DeviceType;
import com.agrhub.sensehub.components.util.NetworkUtils;

/**
 * Created by tanca on 10/19/2017.
 */

public class Sp3SmartPlugEntity extends Entity{
    private ControllerData mController;
    private Sp3Connector mConnector = null;

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
        if(mConnector == null){
            mConnector = new Sp3Connector();
            mConnector.setMac(getMacAddress());
            int i = 0;
            while(!mConnector.authorize() && i < 3){
                i++;
            }

            if(i >= 3){
                return;
            }
        }
        boolean state = mConnector.getState(true);
        mController.mControllerState = (state ? ControllerState.CONTROLLER_STATE_ON : ControllerState.CONTROLLER_STATE_OFF);
    }

    public Sp3Connector getConnector() {
        return mConnector;
    }

    public void setConnector(Sp3Connector mConnector) {
        this.mConnector = mConnector;
    }
}
