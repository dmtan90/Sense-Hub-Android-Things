package com.agrhub.sensehub.components.entity;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.util.Log;

import com.agrhub.sensehub.components.ble.BLEManager;
import com.agrhub.sensehub.components.util.DeviceName;
import com.agrhub.sensehub.components.util.DeviceState;
import com.agrhub.sensehub.components.util.DeviceType;
import com.agrhub.sensehub.components.util.SensorType;

import java.util.List;
import java.util.UUID;

/**
 * Created by tanca on 10/19/2017.
 */

public class nRF51822SensorEntity extends Entity{
    private final String nRF51822_SERVICE_ID = "6e400001-b5a3-f393-e0a9-e50e24dcca9e";
    private final String nRF51822_GET_TEMP_CHAR = "6e400005-b5a3-f393-e0a9-e50e24dcca9e";
    private final String nRF51822_GET_LIGHT_CHAR = "6e400006-b5a3-f393-e0a9-e50e24dcca9e";
    private final String getnRF51822_DESCRIPTOR_ID = "00002902-0000-1000-8000-00805f9b34fb";
    private final UUID UUID_nRF51822_SERVICE_ID = UUID.fromString(nRF51822_SERVICE_ID);
    private final UUID UUID_nRF51822_GET_LIGHT = UUID.fromString(nRF51822_GET_LIGHT_CHAR);
    private final UUID UUID_nRF51822_GET_TEMP = UUID.fromString(nRF51822_GET_TEMP_CHAR);
    private final UUID UUID_nRF51822_DESCRIPTOR_ID = UUID.fromString(getnRF51822_DESCRIPTOR_ID);
    private String mTAG = getClass().getSimpleName();
    private int mLight;
    private int mAirTemperature;
    private BluetoothGatt mGatt;
    private BluetoothGattService mGattnRF51822Service;
    private boolean mIsConnecting;

    public nRF51822SensorEntity(){
        super();
        setDeviceName(DeviceName.DB_DEVICE_NAME_NRF51822_SENSOR);
        setDeviceState(DeviceState.DEVICE_DISCONNECTED);
        setDeviceType(DeviceType.DB_DEVICE_TYPE_SENSOR);

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

    @Override
    public void updateData() {
        mIsConnecting = true;
        BluetoothDevice device = BLEManager.instance.getBleAdapter().getRemoteDevice(getMacAddress());
        mGatt = device.connectGatt(BLEManager.instance.getContext(), false, mGattCallback);
        if(mGatt == null){
            Log.d(mTAG, "Can't connect to " + getMacAddress());
            mIsConnecting = false;
        }
        else{
            while(mIsConnecting){
                try{
                    Thread.sleep(500);
                }catch (Exception e){

                }
            }
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
                mIsConnecting = false;
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            Log.d(mTAG, "onServicesDiscovered");
            if(status == BluetoothGatt.GATT_SUCCESS){
                mGattnRF51822Service = gatt.getService(UUID_nRF51822_SERVICE_ID);
                if(mGattnRF51822Service != null){
                    List<BluetoothGattCharacteristic> lstChars = mGattnRF51822Service.getCharacteristics();
                    for(BluetoothGattCharacteristic mCharacteristic : lstChars){
                        List<BluetoothGattDescriptor> descriptors = mCharacteristic.getDescriptors();
                        BluetoothGattDescriptor mGattnRF51822Descriptor = mCharacteristic.getDescriptor(UUID_nRF51822_DESCRIPTOR_ID);
                        if(mGattnRF51822Descriptor != null && mCharacteristic.getUuid().equals(UUID_nRF51822_GET_TEMP)){
                            gatt.setCharacteristicNotification(mCharacteristic, true);
                            byte[] mValue = {0x01, 0x00};
                            mGattnRF51822Descriptor.setValue(mValue);
                            gatt.writeDescriptor(mGattnRF51822Descriptor);
                        }
                    }
                }
            }
            else{
                gatt.close();
                mIsConnecting = false;
            }
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            Log.d(mTAG, "onCharacteristicChanged");
            byte[] mValue = characteristic.getValue();
            if(characteristic.getUuid().equals(UUID_nRF51822_GET_TEMP)){
                mAirTemperature = (mValue[0] << 8 | mValue[1])/10;
                gatt.setCharacteristicNotification(characteristic, false);
                List<BluetoothGattCharacteristic> lstChars = mGattnRF51822Service.getCharacteristics();
                for(BluetoothGattCharacteristic mCharacteristic : lstChars){
                    List<BluetoothGattDescriptor> descriptors = mCharacteristic.getDescriptors();
                    BluetoothGattDescriptor mGattnRF51822Descriptor = mCharacteristic.getDescriptor(UUID_nRF51822_DESCRIPTOR_ID);
                    if(mGattnRF51822Descriptor != null && mCharacteristic.getUuid().equals(UUID_nRF51822_GET_LIGHT)){
                        gatt.setCharacteristicNotification(mCharacteristic, true);
                        byte[] mDesValue = {0x01, 0x00};
                        mGattnRF51822Descriptor.setValue(mDesValue);
                        gatt.writeDescriptor(mGattnRF51822Descriptor);
                    }
                }
            }
            else if(characteristic.getUuid().equals(UUID_nRF51822_GET_LIGHT)){
                mLight = (mValue[0] << 8 | mValue[1]);
                gatt.close();
                Log.d(mTAG, "onCharacteristicChanged data=" + getData());
                mIsConnecting = false;
            }
        }
    };
}
