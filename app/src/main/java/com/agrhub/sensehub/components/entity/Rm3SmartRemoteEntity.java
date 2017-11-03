package com.agrhub.sensehub.components.entity;

import com.agrhub.sensehub.components.connector.BroadlinkConnector;
import com.agrhub.sensehub.components.connector.Rm3Connector;
import com.agrhub.sensehub.components.connector.Sp3Connector;
import com.agrhub.sensehub.components.devicemanager.DeviceManager;
import com.agrhub.sensehub.components.util.AirConditionerCmd;
import com.agrhub.sensehub.components.util.AirConditionerMode;
import com.agrhub.sensehub.components.util.AirConditionerPower;
import com.agrhub.sensehub.components.util.AirConditionerState;
import com.agrhub.sensehub.components.util.ControllerState;
import com.agrhub.sensehub.components.util.ControllerType;
import com.agrhub.sensehub.components.util.DeviceName;
import com.agrhub.sensehub.components.util.DeviceState;
import com.agrhub.sensehub.components.util.DeviceType;
import com.agrhub.sensehub.components.wifi.WifiManager;

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

    public boolean setACPower(AirConditionerPower mACPower) {
        boolean rs = false;
        Rm3Connector mConnector = (Rm3Connector)getConnector();
        AirConditionerState state = new AirConditionerState();
        state.setAcMode(getACMode());
        state.setAcPower(mACPower);

        rs = mConnector.setState(state);
        if(rs){
            this.mACPower = mACPower;
        }
        return rs;
    }

    public AirConditionerMode getACMode() {
        return mACMode;
    }

    public boolean setACMode(AirConditionerMode mACMode) {
        boolean rs = false;
        Rm3Connector mConnector = (Rm3Connector)getConnector();
        AirConditionerState state = new AirConditionerState();
        state.setAcMode(mACMode);
        state.setAcPower(getACPower());

        rs = mConnector.setState(state);
        if(rs){
            this.mACMode = mACMode;
        }
        return rs;
    }

    public boolean learningCMD(AirConditionerCmd cmd){
        boolean rs = false;
        Rm3Connector mConnector = (Rm3Connector)getConnector();
        rs = mConnector.learnIRCode(cmd);
        return rs;
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
        BroadlinkConnector mConnector = getConnector();
        if(mConnector == null){
            String ip = WifiManager.instance.getDevice(getMacAddress());
            mConnector = new Rm3Connector();
            mConnector.setMac(getMacAddress());
            mConnector.setIP(ip);
            mConnector.setContext(getContext());
            int i = 0;
            while(!mConnector.authorize() && i < 3){
                i++;
            }

            if(i >= 3){
                return;
            }
        }
        AirConditionerState state = ((Rm3Connector)mConnector).getACState();
        mACPower = state.getAcPower();
        mACMode = state.getAcMode();
    }
}
