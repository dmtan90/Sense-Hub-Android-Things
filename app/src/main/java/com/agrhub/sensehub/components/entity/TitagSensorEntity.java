package com.agrhub.sensehub.components.entity;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.util.Log;

import com.agrhub.sensehub.components.ble.BLEManager;
import com.agrhub.sensehub.components.util.DeviceName;
import com.agrhub.sensehub.components.util.DeviceState;
import com.agrhub.sensehub.components.util.DeviceType;
import com.agrhub.sensehub.components.util.SensorType;

import java.util.UUID;

/**
 * Created by tanca on 10/19/2017.
 */

public class TitagSensorEntity extends AxaetSensorEntity {
    private String mTAG = getClass().getSimpleName();
    private int mBattery;
    private int mTemperature;
    private int mHumidity;
    private int mLight;
    private BluetoothGatt mGatt;
    //Battery service
    private final UUID TI_TAG_BATTERY_SERVICE_ID_16 = UUID.fromString("00001204-0000-1000-8000-00805f9b34fb");
    private final UUID TI_TAG_BATTERY_DATA_READ_ID_16 = UUID.fromString("00001204-0000-1000-8000-00805f9b34fb");
    //Humidity & Temperature service

    private final UUID TI_TAG_TEMP_HUMID_SERVICE_ID_128 = UUID.fromString("00001204-0000-1000-8000-00805f9b34fb");
    private final UUID TI_TAG_TEMP_HUMID_DATA_READ_ID_128 = UUID.fromString("00001204-0000-1000-8000-00805f9b34fb");
    private final UUID TI_TAG_TEMP_HUMID_DATA_CONF_ID_128= UUID.fromString("00001204-0000-1000-8000-00805f9b34fb");

    //Light service
    private final UUID TI_TAG_LIGHT_SERVICE_ID_128 = UUID.fromString("00001204-0000-1000-8000-00805f9b34fb");
    private final UUID TI_TAG_LIGHT_DATA_READ_ID_128 = UUID.fromString("00001204-0000-1000-8000-00805f9b34fb");
    private final UUID TI_TAG_LIGHT_DATA_CONF_ID_128 = UUID.fromString("00001204-0000-1000-8000-00805f9b34fb");


    //IR & Ambient temperature service

    private final UUID TI_TAG_IR_TEMP_SERVICE_ID_128 = UUID.fromString("00001204-0000-1000-8000-00805f9b34fb");
    private final UUID TI_TAG_IR_TEMP_DATA_READ_ID_128 = UUID.fromString("00001204-0000-1000-8000-00805f9b34fb");
    private final UUID TI_TAG_IR_TEMP_DATA_CONF_ID_128 = UUID.fromString("00001204-0000-1000-8000-00805f9b34fb");



    public TitagSensorEntity(){
        super();
        setDeviceName(DeviceName.DB_DEVICE_NAME_TI_TAG_SENSOR);
        setDeviceState(DeviceState.DEVICE_DISCONNECTED);
        setDeviceType(DeviceType.DB_DEVICE_TYPE_CONTROLLER);
        this.mLight = -1;
    }


    public int getLight() {
        return mLight;
    }

    public void setLight(int mLight) {
        this.mLight = mLight;
    }

    public int getBattery() {
        return mBattery;
    }

    public void setBattery(int mBattery) {
        this.mBattery = mBattery;
    }

    public int getTemperature() {
        return mTemperature;
    }

    public void setTemperature(int mTemperature) {
        this.mTemperature = mTemperature;
    }

    public int getHumidity() {
        return mHumidity;
    }

    public void setHumidity(int mHumidity) {
        this.mHumidity = mHumidity;
    }

    @Override
    public String getData(){
        return String.format("[{\"sensor_type\":%d,\"sensor_value\":%d}," +
                        "{\"sensor_type\":%d,\"sensor_value\":%d}," +
                        "{\"sensor_type\":%d,\"sensor_value\":%d}," +
                        "{\"sensor_type\":%d,\"sensor_value\":%d}]",
                SensorType.SENSOR_TYPE_LIGHT.getValue(), getLight(),
                SensorType.SENSOR_TYPE_AIR_TEMP.getValue(), getAirTemperature(),
                SensorType.SENSOR_TYPE_AIR_HUMIDITY.getValue(), getAirHumidity(),
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
            if (status == BluetoothGatt.GATT_SUCCESS) {
                BluetoothGattService batteryService = gatt.getService(TI_TAG_BATTERY_SERVICE_ID_16);
                BluetoothGattCharacteristic cBattery = batteryService.getCharacteristic(TI_TAG_BATTERY_DATA_READ_ID_16);
                gatt.readCharacteristic(cBattery);
            }
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            if(status == BluetoothGatt.GATT_SUCCESS){
                if(characteristic.getUuid().equals(TI_TAG_BATTERY_DATA_READ_ID_16)){
                    byte[] data = characteristic.getValue();
                    mBattery = new Byte(data[0]).intValue();
                    Log.i(mTAG,String.format("Battery: %d",mBattery));
                    // Write characteristis Humidity & Temperature
                    final byte[] mValue = {(byte)0x01};
                    gatt.getService(TI_TAG_TEMP_HUMID_SERVICE_ID_128).getCharacteristic(TI_TAG_TEMP_HUMID_DATA_CONF_ID_128).setValue(mValue);
                    gatt.writeCharacteristic(gatt.getService(TI_TAG_TEMP_HUMID_SERVICE_ID_128).getCharacteristic(TI_TAG_TEMP_HUMID_DATA_CONF_ID_128));


                }
                if(characteristic.getUuid().equals(TI_TAG_TEMP_HUMID_DATA_READ_ID_128)){
                    byte[] data = characteristic.getValue();
                    if(data.length == 4){
                        int rawTmp,rawHum;

                        rawTmp = ( data[0]|(data[1]<<8));


                        rawHum =(data[2]|(data[3]<<8));


                        mTemperature = (rawTmp / 65536)*165 - 40;
                        mHumidity = (rawHum / 65536)*100;

                        // write Characteristic IR TEM
                        final byte[] mValue = {(byte)0x01};
                        gatt.getService(TI_TAG_IR_TEMP_SERVICE_ID_128).getCharacteristic(TI_TAG_IR_TEMP_DATA_CONF_ID_128).setValue(mValue);
                        gatt.writeCharacteristic(gatt.getService(TI_TAG_IR_TEMP_SERVICE_ID_128).getCharacteristic(TI_TAG_IR_TEMP_DATA_CONF_ID_128));
                    }
                }
                if(characteristic.getUuid().equals(TI_TAG_IR_TEMP_DATA_READ_ID_128)){
                    //get data IR TEM

                    // Write Characteristic Light
                    final byte[] mValue = {(byte)0x01};
                    gatt.getService(TI_TAG_LIGHT_SERVICE_ID_128).getCharacteristic(TI_TAG_LIGHT_DATA_CONF_ID_128).setValue(mValue);
                    gatt.writeCharacteristic(gatt.getService(TI_TAG_LIGHT_SERVICE_ID_128).getCharacteristic(TI_TAG_LIGHT_DATA_CONF_ID_128));
                }
                if(characteristic.getUuid().equals(TI_TAG_LIGHT_DATA_READ_ID_128)){
                    byte[] data = characteristic.getValue();
                    if(data.length == 2){


                        // Ligh converter
                        int rawdata = (data[0]|(data[1]<<8));
                        int m = rawdata & 0x0FFF;
                        int e = (rawdata & 0xF000) >> 12;
                        e = (e == 0) ? 1 : 2 << (e - 1);
                        mLight = (int)(m * (0.01 * e));
                        gatt.close();

                    }
                }
            }
        }

        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            if(status == BluetoothGatt.GATT_SUCCESS){
                if(characteristic.getUuid().equals(TI_TAG_TEMP_HUMID_DATA_CONF_ID_128)){
                    BluetoothGattCharacteristic cTmp_hum = gatt.getService(TI_TAG_TEMP_HUMID_SERVICE_ID_128).getCharacteristic(TI_TAG_TEMP_HUMID_DATA_READ_ID_128);
                    gatt.readCharacteristic(cTmp_hum);
                }
                if(characteristic.getUuid().equals(TI_TAG_IR_TEMP_DATA_CONF_ID_128)){
                    BluetoothGattCharacteristic cIrTemp = gatt.getService(TI_TAG_IR_TEMP_SERVICE_ID_128).getCharacteristic(TI_TAG_IR_TEMP_DATA_READ_ID_128);
                    gatt.readCharacteristic(cIrTemp);
                }
                if(characteristic.getUuid().equals(TI_TAG_LIGHT_DATA_CONF_ID_128)){
                    BluetoothGattCharacteristic cLight = gatt.getService(TI_TAG_LIGHT_SERVICE_ID_128).getCharacteristic(TI_TAG_LIGHT_DATA_READ_ID_128);
                    gatt.readCharacteristic(cLight);
                }
            }
        }
    };


}

// get Battery -> write Humidity & Temperature -> read Humidity & Temperature  -> write IR and Ambient -> read IR and Ambient-> write Light Characteristic -> read Light Characteristic ->
