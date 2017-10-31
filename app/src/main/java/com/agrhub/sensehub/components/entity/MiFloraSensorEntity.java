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

import java.util.Arrays;
import java.util.UUID;

/**
 * Created by tanca on 10/19/2017.
 */

public class MiFloraSensorEntity extends Entity{
    private final String MI_FLORA_SERVICE_ID = "00001204-0000-1000-8000-00805f9b34fb";
    private final String MI_FLORA_ENABLE_DATA = "00001A00-0000-1000-8000-00805f9b34fb";
    private final String MI_FLORA_GET_DATA = "00001A01-0000-1000-8000-00805f9b34fb";
    private final String MI_FLORA_GET_FIRMWARE = "00001A02-0000-1000-8000-00805f9b34fb";
    private final UUID UUID_MI_FLORA_SERVICE_ID = UUID.fromString(MI_FLORA_SERVICE_ID);
    private final UUID UUID_MI_FLORA_ENABLE_DATA = UUID.fromString(MI_FLORA_ENABLE_DATA);
    private final UUID UUID_MI_FLORA_GET_DATA = UUID.fromString(MI_FLORA_GET_DATA);
    private final UUID UUID_MI_FLORA_FIRMWARE = UUID.fromString(MI_FLORA_GET_FIRMWARE);

    private String mTAG = getClass().getSimpleName();
    private int mLight;
    private int mAirTemperature;
    private int mSoilHumidity;
    private int mSoilEC;
    private int mBattery;
    private String mFirmware;

    private BluetoothGattService mGattMiFloraService;
    private boolean mIsConnecting;

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
        mIsConnecting = true;
        BluetoothDevice device = BLEManager.instance.getBleAdapter().getRemoteDevice(getMacAddress());
        BluetoothGatt mGatt = device.connectGatt(BLEManager.instance.getContext(), false, mGattCallback);
        if(mGatt == null){
            Log.d(mTAG, "Can't connect to " + getMacAddress());
            mIsConnecting = false;
        }
        while(mIsConnecting){
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
                for(BluetoothGattService service : gatt.getServices()){
                    Log.d(mTAG, "service: " + service.getUuid().toString());
                }
                //Log.d(mTAG, "mGattMiFloraService: " + UUID_MI_FLORA_SERVICE_ID.toString());
                mGattMiFloraService = gatt.getService(UUID_MI_FLORA_SERVICE_ID);
                if(mGattMiFloraService != null){
                    boolean rs = gatt.readCharacteristic(mGattMiFloraService.getCharacteristic(UUID_MI_FLORA_FIRMWARE));
                    if(!rs){
                        Log.i(mTAG, "Can't read mGattMiFloraFwCharacteristic");
                    }
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
                Log.d(mTAG, "characteristic: " + characteristic.getUuid().toString());
                if(characteristic.equals(mGattMiFloraService.getCharacteristic(UUID_MI_FLORA_FIRMWARE))){
                    byte[] datas = characteristic.getValue();
                    int mBat = new Byte(datas[0]).intValue();
                    mFirmware = "";
                    for(int i = 1; i < datas.length; i++){
                        Byte b = new Byte(datas[i]);
                        if(b.intValue() >= 32 && b.intValue() <= 126){
                            mFirmware += (char)(b&0xFF);
                        }
                    }
                    setBattery(mBat);
                    Log.d(mTAG, String.format("Battery=%d - Firmware=%s", mBat, mFirmware));

                    //enable read data
                    BluetoothGattCharacteristic mGattMiFloraEnableDataCharacteristic =
                            mGattMiFloraService.getCharacteristic(UUID_MI_FLORA_ENABLE_DATA);
                    byte[] mValue = {(byte)0xA0, (byte)0x1F};
                    mGattMiFloraEnableDataCharacteristic.setValue(mValue);
                    boolean rs = gatt.writeCharacteristic(mGattMiFloraEnableDataCharacteristic);
                    if(!rs){
                        Log.i(mTAG, "Can't write mGattMiFloraEnableDataCharacteristic");
                    }
                }
                else if(characteristic.equals(mGattMiFloraService.getCharacteristic(UUID_MI_FLORA_GET_DATA))){
                    byte[] datas = characteristic.getValue();
                    if(datas.length > 9){
                        mAirTemperature = ((datas[1]*0xFF) + datas[0])/10;
                        mLight = (datas[4]*0xFF + datas[3]);
                        mSoilHumidity = (datas[6]*0xFF + datas[7]);
                        mSoilEC = (datas[9]*0xFF + datas[8]);
                        Log.d(mTAG, String.format("Data=%s", getData()));
                        gatt.close();
                        mIsConnecting = false;
                    }
                }
            }
        }

        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            if(status == BluetoothGatt.GATT_SUCCESS){
                boolean rs = gatt.readCharacteristic(mGattMiFloraService.getCharacteristic(UUID_MI_FLORA_GET_DATA));
                if(!rs){
                    Log.i(mTAG, "Can't read mGattMiFloraGetDataCharacteristic");
                }
            }
        }
    };
}
