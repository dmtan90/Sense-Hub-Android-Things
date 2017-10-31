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
    private final String TITAG_BATTERY_SERVICE_ID = "";
    private final String TITAG_GET_BATTERY = "";
    private final String TITAG_AIR_TEMP_HUMIDITY_SERVICE_ID = "";
    private final String TITAG_GET_AIR_TEMP_HUMIDITY = "";
    private final String TITAG_ENABLE_AIR_TEMP_HUMIDITY = "";
    private final String TITAG_LIGHT_SERVICE_ID = "";
    private final String TITAG_GET_LIGHT = "";
    private final String TITAG_ENABLE_LIGHT = "";

    private final UUID UUID_TITAG_BATTERY_SERVICE = UUID.fromString(TITAG_BATTERY_SERVICE_ID);
    private final UUID UUID_TITAG_GET_BATTERY = UUID.fromString(TITAG_GET_BATTERY);

    private final UUID UUID_TITAG_AIR_TEMP_HUMIDITY_SERVICE = UUID.fromString(TITAG_AIR_TEMP_HUMIDITY_SERVICE_ID);
    private final UUID UUID_TITAG_GET_AIR_TEMP_HUMIDITY = UUID.fromString(TITAG_GET_AIR_TEMP_HUMIDITY);
    private final UUID UUID_TITAG_ENABLE_AIR_TEMP_HUMIDITY = UUID.fromString(TITAG_ENABLE_AIR_TEMP_HUMIDITY);

    private final UUID UUID_TITAG_LIGHT_SERVICE = UUID.fromString(TITAG_LIGHT_SERVICE_ID);
    private final UUID UUID_TITAG_GET_LIGHT = UUID.fromString(TITAG_GET_LIGHT);
    private final UUID UUID_TITAG_ENABLE_LIGHT = UUID.fromString(TITAG_ENABLE_LIGHT);

    private String mTAG = getClass().getSimpleName();
    private int mLight;
    private BluetoothGatt mGatt;
    private BluetoothGattService mGattnTitagBatteryService;
    private BluetoothGattService mGattnTitagTempHumidityService;
    private BluetoothGattService mGattnTitagLightService;
    private boolean mIsConnecting;

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
        mIsConnecting = true;
        BluetoothDevice device = BLEManager.instance.getBleAdapter().getRemoteDevice(getMacAddress());
        mGatt = device.connectGatt(BLEManager.instance.getContext(), false, mGattCallback);
        if(mGatt == null){
            Log.d(mTAG, "Can't connect to " + getMacAddress());
            mIsConnecting = false;
        }
        while (mIsConnecting){
            try{
                Thread.sleep(500);
            }catch (Exception e){

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
            if(status == BluetoothGatt.GATT_SUCCESS){
                mGattnTitagBatteryService = gatt.getService(UUID_TITAG_BATTERY_SERVICE);
                mGattnTitagTempHumidityService = gatt.getService(UUID_TITAG_AIR_TEMP_HUMIDITY_SERVICE);
                mGattnTitagLightService = gatt.getService(UUID_TITAG_LIGHT_SERVICE);
                if(mGattnTitagBatteryService != null){
                    gatt.readCharacteristic(mGattnTitagBatteryService.getCharacteristic(UUID_TITAG_GET_BATTERY));
                }
            }
            else{
                gatt.close();
                mIsConnecting = false;
            }
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            if(status == BluetoothGatt.GATT_SUCCESS){
                if(characteristic.getUuid().equals(UUID_TITAG_GET_BATTERY)){
                    byte[] mValue = characteristic.getValue();
                    int mBattery = new Byte(mValue[0]).intValue();
                    setBattery(mBattery);
                    byte[] mHumidityValue = {mValue[0]};
                    mGattnTitagTempHumidityService.getCharacteristic(UUID_TITAG_ENABLE_AIR_TEMP_HUMIDITY).setValue(mHumidityValue);
                    gatt.writeCharacteristic(mGattnTitagTempHumidityService.getCharacteristic(UUID_TITAG_ENABLE_AIR_TEMP_HUMIDITY));
                }
            }
        }

        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            if(status == BluetoothGatt.GATT_SUCCESS){
                if(characteristic.getUuid().equals(UUID_TITAG_ENABLE_AIR_TEMP_HUMIDITY)){
                    gatt.readCharacteristic(mGattnTitagTempHumidityService.getCharacteristic(UUID_TITAG_GET_AIR_TEMP_HUMIDITY));
                }

                if(characteristic.getUuid().equals(UUID_TITAG_ENABLE_LIGHT)){
                    gatt.readCharacteristic(mGattnTitagLightService.getCharacteristic(UUID_TITAG_GET_LIGHT));
                }
            }
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            if(characteristic.getUuid().equals(UUID_TITAG_GET_AIR_TEMP_HUMIDITY)){
                byte[] mValue = characteristic.getValue();
                int mRawTemp = (mValue[1] << 8 | mValue[0]);
                int mRawHum = (mValue[3] << 8 | mValue[2]);
                int mTemp = 0;
                int mHum = 0;
                sensorHdc1000Convert(mRawTemp, mRawHum, mTemp, mHum);
                setAirHumidity(mHum);
                setAirTemperature(mTemp);

                gatt.writeCharacteristic(mGattnTitagLightService.getCharacteristic(UUID_TITAG_ENABLE_LIGHT));
            }

            if(characteristic.getUuid().equals(UUID_TITAG_GET_LIGHT)){
                byte[] mValue = characteristic.getValue();
                int mRawLight = (mValue[1] << 8 | mValue[0]);
                mLight = sensorOpt3001Convert(mRawLight);
                Log.d(mTAG, "data=" + getData());
                gatt.close();
                mIsConnecting = false;
            }
        }
    };

    public void sensorHdc1000Convert(int rawTemp, int rawHum, int temp, int hum){
        //-- calculate temperature [°C]
        temp = (int)(((double) rawTemp/65536)*165 - 40);
        //-- calculate relative humidity [%RH]
        hum = (int)((double) rawHum/65536)*100;
    }

    public int sensorOpt3001Convert(int rawLight){
        int e, m;
        m =  rawLight & 0x0FFF;
        e = (rawLight & 0xF000) >> 12;
        /** e on 4 bits stored in a 16 bit unsigned => it can store 2 << (e - 1) with e < 16 */
        e = (e == 0) ? 1 : 2 << (e - 1);
        return (int)(m*(e*0.01));
    }
}
