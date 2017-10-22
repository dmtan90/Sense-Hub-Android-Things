package com.agrhub.sensehub.components.entity;

import com.agrhub.sensehub.components.util.DeviceName;
import com.agrhub.sensehub.components.util.DeviceState;
import com.agrhub.sensehub.components.util.DeviceType;
import com.agrhub.sensehub.components.util.SensorType;

/**
 * Created by tanca on 10/19/2017.
 */

public abstract class Entity {
    private String mMacAddress;
    private DeviceName mDeviceName;
    private DeviceType mDeviceType;
    private DeviceState mDeviceState;

    public Entity(){
        this.mMacAddress = "00:00:00:00:00:00";
        this.mDeviceName = DeviceName.DB_DEVICE_NAME_UNKNOWN;
        this.mDeviceType = DeviceType.DB_DEVICE_TYPE_UNKNOWN;
        this.mDeviceState = DeviceState.DEVICE_CONNECTED;
    }

    public String getMacAddress() {
        return mMacAddress;
    }

    public void setMacAddress(String mMacAddress) {
        this.mMacAddress = mMacAddress;
    }

    public DeviceName getDeviceName() {
        return mDeviceName;
    }

    public void setDeviceName(DeviceName mDeviceName) {
        this.mDeviceName = mDeviceName;
    }

    public DeviceType getDeviceType() {
        return mDeviceType;
    }

    public void setDeviceType(DeviceType mDeviceType) {
        this.mDeviceType = mDeviceType;
    }

    public DeviceState getDeviceState() {
        return mDeviceState;
    }

    public void setDeviceState(DeviceState mDeviceState) {
        this.mDeviceState = mDeviceState;
    }

    public abstract String getData();
    public abstract void updateData();

    public String toString(){
        return String.format("{\"device_name\":%d,\"device_type\":%d,\"device_state\":%d,\"device_mac_address\":\"%s\",\"data\":%s}",
                getDeviceName().getValue(),
                getDeviceType().getValue(),
                getDeviceState().getValue(),
                getMacAddress(),
                getData()
        );
    }
}
