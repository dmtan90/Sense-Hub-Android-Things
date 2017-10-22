package com.agrhub.sensehub.components.entity;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothProfile;
import android.util.Log;

import com.agrhub.sensehub.components.ble.BLEManager;
import com.agrhub.sensehub.components.util.DeviceName;
import com.agrhub.sensehub.components.util.DeviceState;
import com.agrhub.sensehub.components.util.DeviceType;
import com.agrhub.sensehub.components.util.SensorType;

/**
 * Created by tanca on 10/19/2017.
 */

public class MiFloraSensorEntity extends Entity{
    private String mTAG = getClass().getSimpleName();
    private int mLight;
    private int mAirTemperature;
    private int mSoilHumidity;
    private int mSoilEC;
    private int mBattery;
    private BluetoothGatt mGatt;

    public MiFloraSensorEntity(){
        super();
        setDeviceName(DeviceName.DB_DEVICE_NAME_MI_FLORA);
        setDeviceState(DeviceState.DEVICE_DISCONNECTED);
        setDeviceType(DeviceType.DB_DEVICE_TYPE_SENSOR);
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

    @Override
    public void updateData() {
        BluetoothDevice device = BLEManager.instance.getBleAdapter().getRemoteDevice(getMacAddress());
        mGatt = device.connectGatt(BLEManager.instance.getContext(), false, mGattCallback);
        if(mGatt == null){
            Log.d(mTAG, "Can't connect to " + getMacAddress());
        }
    }

    private BluetoothGattCallback mGattCallback = new BluetoothGattCallback(){
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                Log.i(mTAG, "Connected to GATT client. Attempting to start service discovery");
                gatt.discoverServices();
            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                Log.i(mTAG, "Disconnected from GATT client");
            }
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicRead(gatt, characteristic, status);
        }

        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicWrite(gatt, characteristic, status);
        }
    };
}
