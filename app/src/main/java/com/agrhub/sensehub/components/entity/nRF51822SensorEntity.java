package com.agrhub.sensehub.components.entity;

import com.agrhub.sensehub.components.util.DeviceName;
import com.agrhub.sensehub.components.util.DeviceState;
import com.agrhub.sensehub.components.util.SensorType;

/**
 * Created by tanca on 10/19/2017.
 */

public class nRF51822SensorEntity extends Entity{
    private int mLight;
    private int mAirTemperature;

    public nRF51822SensorEntity(){
        super();
        setDeviceName(DeviceName.DB_DEVICE_NAME_NRF51822_SENSOR);
        setDeviceState(DeviceState.DEVICE_DISCONNECTED);

        this.mLight = -1;
        this.mAirTemperature = -1;
    }

    public int getLight() {
        return mLight;
    }

    public void setLight(int mLight) {
        this.mLight = mLight;
    }

    public int getAirTemperature() {
        return mAirTemperature;
    }

    public void setAirTemperature(int mAirTemperature) {
        this.mAirTemperature = mAirTemperature;
    }

    @Override
    public String getData(){
        return String.format("[{\"sensor_type\":%d,\"sensor_value\":%d}," +
                        "{\"sensor_type\":%d,\"sensor_value\":%d}]",
                SensorType.SENSOR_TYPE_LIGHT.getValue(), getLight(),
                SensorType.SENSOR_TYPE_AIR_TEMP.getValue(), getAirTemperature()
        );
    }
}
