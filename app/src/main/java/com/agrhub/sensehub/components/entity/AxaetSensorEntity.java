package com.agrhub.sensehub.components.entity;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattServerCallback;

import com.agrhub.sensehub.components.ble.BLEManager;
import com.agrhub.sensehub.components.util.DeviceName;
import com.agrhub.sensehub.components.util.DeviceState;
import com.agrhub.sensehub.components.util.DeviceType;
import com.agrhub.sensehub.components.util.SensorType;

/**
 * Created by tanca on 10/19/2017.
 */

public class AxaetSensorEntity extends Entity {
    private int mAirTemperature;
    private int mAirHumidity;
    private int mBattery;

    public AxaetSensorEntity(){
        super();
        setDeviceName(DeviceName.DB_DEVICE_NAME_AXAET_AIR_SENSOR);
        setDeviceState(DeviceState.DEVICE_DISCONNECTED);
        setDeviceType(DeviceType.DB_DEVICE_TYPE_SENSOR);
        this.mAirHumidity = -1;
        this.mAirHumidity = -1;
        this.mBattery = -1;
    }

    public int getAirTemperature() {
        return mAirTemperature;
    }

    public void setAirTemperature(int mAirTemperature) {
        this.mAirTemperature = mAirTemperature;
    }

    public int getAirHumidity() {
        return mAirHumidity;
    }

    public void setAirHumidity(int mAirHumidity) {
        this.mAirHumidity = mAirHumidity;
    }

    public int getBattery() {
        return mBattery;
    }

    public void setBattery(int mBattery) {
        this.mBattery = mBattery;
    }

    @Override
    public void updateData() {
        return;
    }

    @Override
    public String getData(){
        return String.format("[{\"sensor_type\":%d,\"sensor_value\":%d}," +
                "{\"sensor_type\":%d,\"sensor_value\":%d}," +
                "{\"sensor_type\":%d,\"sensor_value\":%d}]",
                SensorType.SENSOR_TYPE_AIR_TEMP.getValue(), getAirTemperature(),
                SensorType.SENSOR_TYPE_AIR_HUMIDITY.getValue(), getAirHumidity(),
                SensorType.SENSOR_TYPE_BAT.getValue(), getBattery()
                );
    }
}
