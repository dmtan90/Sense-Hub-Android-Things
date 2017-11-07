package com.agrhub.sensehub.components.entity;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.os.HandlerThread;
import android.util.Log;

import com.agrhub.sensehub.components.ble.BLEManager;
import com.agrhub.sensehub.components.util.DeviceName;
import com.agrhub.sensehub.components.util.DeviceState;
import com.agrhub.sensehub.components.util.DeviceType;
import com.agrhub.sensehub.components.util.SensorType;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;

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
    private String mFirmware;
    private BluetoothGatt mGatt;
    private final UUID FLORA_DATA_SERVICE_ID_16 = UUID.fromString("00001204-0000-1000-8000-00805f9b34fb");
    private final UUID FLORA_ENABLE_DATA_CHAR_ID_16 = UUID.fromString("00001A00-0000-1000-8000-00805f9b34fb");
    private final UUID FLORA_GET_DATA_CHAR_ID_16 = UUID.fromString("00001A01-0000-1000-8000-00805f9b34fb");
    private final UUID FLORA_FIRMWARE_CHAR_ID_16 = UUID.fromString("00001A02-0000-1000-8000-00805f9b34fb");





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
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            gatt.readCharacteristic(gatt.getService(FLORA_DATA_SERVICE_ID_16).getCharacteristic(FLORA_FIRMWARE_CHAR_ID_16));
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                if (characteristic.getUuid().equals(FLORA_FIRMWARE_CHAR_ID_16)) {
                    byte[] data = characteristic.getValue();
                    mBattery = new Byte(data[0]).intValue();
                    mFirmware = new String(data, StandardCharsets.UTF_8);
                    Log.i(mTAG, "FLORA_FIRMWARE_CHAR_ID_16");
                    Log.i(mTAG, mFirmware);
                    Log.i(mTAG, String.format("Battery: %d", mBattery));

                    // after get Firmware
                    Log.i(mTAG,"Writting Characteristic");
                    final byte[] mValue = {(byte)0xA0, (byte)0x1F};
                    BluetoothGattCharacteristic cEnable = gatt.getService(FLORA_DATA_SERVICE_ID_16).getCharacteristic(FLORA_ENABLE_DATA_CHAR_ID_16);
                    cEnable.setValue(mValue);
                    gatt.writeCharacteristic(cEnable);

                }
                if (characteristic.getUuid().equals(FLORA_GET_DATA_CHAR_ID_16)) {
                    Log.i(mTAG, "FLORA_GET_DATA_CHAR_ID_16");
                    byte[] data = characteristic.getValue();
                    mAirTemperature = (data[1]*256 + data[0])/10;
                    mLight = (data[4]*256 +data[3]);
                    mSoilHumidity = (data[6]*256 +data[7]);
                    mSoilEC = (data[9]*256 +data[8]);
                    Log.i(mTAG,String.format("AirTemperatre: %d " +
                            " Light: %d" +
                            " SoilHumidity : %d" +
                            " SoilEc : %d",mAirTemperature,mLight,mSoilHumidity,mSoilEC));
                }
                gatt.close();
            }
        }
        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            if(status==BluetoothGatt.GATT_SUCCESS){
             Log.i(mTAG,String.format("WriteCharacteristic with status : %d",status));
            if(characteristic.getUuid().equals(FLORA_ENABLE_DATA_CHAR_ID_16)){
                BluetoothGattService mifloraService = gatt.getService(FLORA_DATA_SERVICE_ID_16);
                BluetoothGattCharacteristic cGetData = mifloraService.getCharacteristic(FLORA_GET_DATA_CHAR_ID_16);
                gatt.readCharacteristic(cGetData);
            }
            }
        }
        };
}
