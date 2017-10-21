package com.agrhub.sensehub.components.entity;

import com.agrhub.sensehub.components.util.DeviceName;
import com.agrhub.sensehub.components.util.DeviceState;
import com.agrhub.sensehub.components.util.SensorType;

/**
 * Created by tanca on 10/19/2017.
 */

public class MiFloraSensorEntity extends Entity{
    private int mLight;
    private int mAirTemperature;
    private int mSoilHumidity;
    private int mSoilEC;
    private int mBattery;

    public MiFloraSensorEntity(){
        super();
        setDeviceName(DeviceName.DB_DEVICE_NAME_MI_FLORA);
        setDeviceState(DeviceState.DEVICE_DISCONNECTED);
        this.mLight = -1;
        this.mAirTemperature = -1;
        this.mSoilEC = -1;
        this.mBattery = -1;
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

    public int getSoilHumidity() {
        return mSoilHumidity;
    }

    public void setSoilHumidity(int mSoilHumidity) {
        this.mSoilHumidity = mSoilHumidity;
    }

    public int getSoilEC() {
        return mSoilEC;
    }

    public void setSoilEC(int mSoilEC) {
        this.mSoilEC = mSoilEC;
    }

    public int getBattery() {
        return mBattery;
    }

    public void setBattery(int mBattery) {
        this.mBattery = mBattery;
    }

    public String getData(){
        return String.format("[{\"sensor_type\":%d,\"sensor_value\":%d}," +
                        "{\"sensor_type\":%d,\"sensor_value\":%d}," +
                        "{\"sensor_type\":%d,\"sensor_value\":%d}," +
                        "{\"sensor_type\":%d,\"sensor_value\":%d}," +
                        "{\"sensor_type\":%d,\"sensor_value\":%d}]",
                SensorType.SENSOR_TYPE_LIGHT.getValue(), getLight(),
                SensorType.SENSOR_TYPE_AIR_TEMP.getValue(), getAirTemperature(),
                SensorType.SENSOR_TYPE_SOIL_HUMIDITY.getValue(), getSoilHumidity(),
                SensorType.SENSOR_TYPE_SOIL_EC.getValue(), getSoilEC(),
                SensorType.SENSOR_TYPE_BAT.getValue(), getBattery()
        );
    }
}
