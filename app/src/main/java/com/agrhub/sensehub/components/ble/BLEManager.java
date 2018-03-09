package com.agrhub.sensehub.components.ble;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.ScanRecord;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.util.Log;

import com.agrhub.sensehub.components.devicemanager.DeviceManager;
import com.agrhub.sensehub.components.entity.AxaetSensorEntity;
import com.agrhub.sensehub.components.entity.Entity;
import com.agrhub.sensehub.components.entity.MiFloraSensorEntity;
import com.agrhub.sensehub.components.entity.TitagSensorEntity;
import com.agrhub.sensehub.components.entity.nRF51822SensorEntity;
import com.agrhub.sensehub.components.util.BleDeviceName;
import com.agrhub.sensehub.components.util.DeviceName;
import com.agrhub.sensehub.components.util.DeviceState;
import com.agrhub.sensehub.components.util.DeviceType;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

/**
 * Created by tanca on 10/20/2017.
 */

public enum BLEManager {
    instance;
    private String mTAG = getClass().getSimpleName();
    private BluetoothAdapter mBluetoothAdapter;
    private boolean mScanning;
    private boolean mPolling;
    private Handler mHandler;
        private BluetoothAdapter.LeScanCallback mBLECallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
            // Add the name and address to an array adapter to show in a Toast
            String derp = device.getName() + " - " + device.getAddress();

                Log.d(mTAG, "Bluetooth device: " + derp);
            Entity bleDevice = DeviceManager.instance.getDevice(device.getAddress());
            if(bleDevice == null){
                bleDevice = initDevice(device.getName());
                if(bleDevice != null){
                    bleDevice.setMacAddress(device.getAddress());
                    DeviceManager.instance.setDevice(bleDevice);
                }
                else{
                    return;
                }
            }
            bleDevice.setDeviceState(DeviceState.DEVICE_CONNECTED);

            if(bleDevice.getDeviceName().getValue() == DeviceName.DB_DEVICE_NAME_AXAET_AIR_SENSOR.getValue()){
                if(scanRecord != null && scanRecord.length >= 32){
                    boolean mHasData = true;
                    for(int i = 25; i < 32; i++){
                        Byte b = new Byte(scanRecord[i]);
                        if(b.intValue() != 0){
                            mHasData = false;
                            break;
                        }
                    }

                    if(mHasData){
                        int mBattery = new Byte(scanRecord[11]).intValue();
                        int mTemperature = new Byte(scanRecord[12]).intValue();
                        int mHumidity = new Byte(scanRecord[13]).intValue();
                        Log.d(mTAG, String.format("pBeacon data=%d-%d-%d", mBattery, mTemperature, mHumidity));
                        ((AxaetSensorEntity)bleDevice).setBattery(mBattery);
                        ((AxaetSensorEntity)bleDevice).setAirTemperature(mTemperature);
                        ((AxaetSensorEntity)bleDevice).setAirHumidity(mHumidity);
                    }
                }
            }
//
        }
    };

    private static final long SCAN_PERIOD = 10000;

    private Context mContext = null;

    public void init(Context ctx){
        mContext = ctx;
        mHandler = new Handler();
        //Initializes a Bluetooth adapter.  For API level 18 and above, get a reference to
        // BluetoothAdapter through BluetoothManager.
        final BluetoothManager bluetoothManager = (BluetoothManager) mContext.getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();

        // Checks if Bluetooth is supported on the device.
        if (mBluetoothAdapter == null) {
            return;
        }

        if (!mBluetoothAdapter.isEnabled()) {
            Log.d(mTAG, "Bluetooth is currently disabled...enabling");
            mBluetoothAdapter.enable();
        }

        if (mBluetoothAdapter.isDiscovering()) {
            mBluetoothAdapter.cancelDiscovery();
        }
        mPolling = false;
        mScanning = false;

        Timer t = new Timer();
        TimerTask mBleTask = new TimerTask() {
            @Override
            public void run()
            {
                if(!mScanning && !mPolling){
                    scanBLEDevice(true);
                }
            }
        };
        t.scheduleAtFixedRate(mBleTask, 5000, 15000);
    }

    public void scanBLEDevice(final boolean enable) {
        Log.d(mTAG, "scanBLEDevice: " + enable);
        mPolling = false;
        mBluetoothAdapter.stopLeScan(mBLECallback);
        if (enable) {
            /*if (mBluetoothAdapter.isDiscovering()) {
                mBluetoothAdapter.cancelDiscovery();
            }*/

            // Stops scanning after a pre-defined scan period.
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mScanning = false;
                    mBluetoothAdapter.stopLeScan(mBLECallback);
                    pollDevices();
                    //mBluetoothAdapter.cancelDiscovery();
                }
            }, SCAN_PERIOD);

            mScanning = true;
            //mBluetoothAdapter.startDiscovery();

            mBluetoothAdapter.startLeScan(mBLECallback);
            /*mReceiver = new BleBroadcastReceiver();
            IntentFilter ifilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
            mContext.registerReceiver(mReceiver, ifilter);*/
        } else {
            mScanning = false;
            //mBluetoothAdapter.stopLeScan(mBLECallback);
            //mBluetoothAdapter.cancelDiscovery();
        }
    }

    public DeviceName getDeviceName(String name){
        if(name == null){
            return null;
        }
        DeviceName mName = DeviceName.DB_DEVICE_NAME_UNKNOWN;
        if(name.contains(BleDeviceName.BLE_DEVICE_NAME_MIFLORA.getValue())){
            mName = DeviceName.DB_DEVICE_NAME_MI_FLORA;
        }
        else if(name.contains(BleDeviceName.BLE_DEVICE_NAME_AXAET.getValue())){
            mName = DeviceName.DB_DEVICE_NAME_AXAET_AIR_SENSOR;
        }
        else if(name.contains(BleDeviceName.BLE_DEVICE_NAME_TITAG.getValue())){
            mName = DeviceName.DB_DEVICE_NAME_TI_TAG_SENSOR;
        }
        else if(name.contains(BleDeviceName.BLE_DEVICE_NAME_nRF51822.getValue())){
            mName = DeviceName.DB_DEVICE_NAME_NRF51822_SENSOR;
        }
        return mName;
    }

    public Entity initDevice(String name){
        Entity mDevice = null;
        if(name == null){
            return mDevice;
        }
        DeviceName mName = getDeviceName(name);
        if(mName.getValue() == DeviceName.DB_DEVICE_NAME_UNKNOWN.getValue()){
            return mDevice;
        }

        if(mName.getValue() == DeviceName.DB_DEVICE_NAME_MI_FLORA.getValue()){
            mDevice = new MiFloraSensorEntity();
        }
        else if(mName.getValue() == DeviceName.DB_DEVICE_NAME_AXAET_AIR_SENSOR.getValue()){
            mDevice =  new AxaetSensorEntity();
        }
        else if(mName.getValue() == DeviceName.DB_DEVICE_NAME_TI_TAG_SENSOR.getValue()){
            mDevice =  new TitagSensorEntity();
        }
        else if(mName.getValue() == DeviceName.DB_DEVICE_NAME_NRF51822_SENSOR.getValue()){
            mDevice =  new nRF51822SensorEntity();
        }
        return mDevice;
    }

    //get data of each BLE sensor
    public void pollDevices(){
        Log.d(mTAG, "pollDevices");
        mPolling = true;
        List<Entity> mBleDevices = new ArrayList<>();
        for(Map.Entry<String, Entity> entity : DeviceManager.instance.getDeviceMap().entrySet()){
            Entity mDev = entity.getValue();
            if(mDev.getDeviceType().getValue() == DeviceType.DB_DEVICE_TYPE_SENSOR.getValue()){
                mBleDevices.add(mDev);
            }
        }

        for(Entity mBleDevice : mBleDevices){
            if(mBleDevice.getDeviceState().getValue() == DeviceState.DEVICE_CONNECTED.getValue()){
                mBleDevice.updateData();
            }
        }
        mPolling = false;
    }

    public BluetoothAdapter getBleAdapter(){
        return mBluetoothAdapter;
    }

    public Context getContext(){
        return mContext;
    }

    /*private class BleBroadcastReceiver extends BroadcastReceiver {

        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction(); //may need to chain this to a recognizing function
            Log.d(mTAG, "BroadcastReceiver");
            if (BluetoothDevice.ACTION_FOUND.equals(action)){
                // Get the BluetoothDevice object from the Intent
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                //ScanRecord record = intent.getParcelableExtra(BluetoothDevice);
                // Add the name and address to an array adapter to show in a Toast
                String derp = device.getName() + " - " + device.getAddress();
                Log.d(mTAG, "Bluetooth device: " + derp);
                Entity bleDevice = DeviceManager.instance.getDevice(device.getAddress());
                if(bleDevice == null){
                    bleDevice = initDevice(device.getName());
                    if(bleDevice != null){
                        bleDevice.setMacAddress(device.getAddress());
                        if(bleDevice.getDeviceName().getValue() == DeviceName.DB_DEVICE_NAME_AXAET_AIR_SENSOR.getValue()){

                        }

                        DeviceManager.instance.setDevice(bleDevice);
                    }
                }
            }
        }
    }*/
}
