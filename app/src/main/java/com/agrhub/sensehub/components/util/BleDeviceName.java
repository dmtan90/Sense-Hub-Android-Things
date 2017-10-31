package com.agrhub.sensehub.components.util;

/**
 * Created by tanca on 10/21/2017.
 */

public enum BleDeviceName {
    BLE_DEVICE_NAME_MIFLORA("Flower care"),
    BLE_DEVICE_NAME_MIFLORA2("Flower mate"),
    BLE_DEVICE_NAME_AXAET("pBeacon"),
    BLE_DEVICE_NAME_TITAG("CC2650 SensorTag"),
    BLE_DEVICE_NAME_nRF51822("SENSOR");

    private final String mDeviceName;
    private BleDeviceName(String name){
        this.mDeviceName = name;
    }

    public String getValue(){
        return this.mDeviceName;
    }
}
