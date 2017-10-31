package com.agrhub.sensehub.components.connector;

import android.content.Context;

import com.agrhub.sensehub.components.util.DeviceName;

/**
 * Created by tanca on 10/28/2017.
 */

public abstract class Connector {
    private DeviceName mDevName;
    private String mIP;
    private String mMac;
    private Context mContext;

    public Connector(){
        mDevName = DeviceName.DB_DEVICE_NAME_UNKNOWN;
        mIP = "0.0.0.0";
        mMac = "00:00:00:00:00:00";
    }

    public DeviceName getDevName() {
        return mDevName;
    }

    public void setDevName(DeviceName mDevName) {
        this.mDevName = mDevName;
    }

    public String getIP() {
        return mIP;
    }

    public void setIP(String mIP) {
        this.mIP = mIP;
    }

    public String getMac() {
        return mMac;
    }

    public void setMac(String mMac) {
        this.mMac = mMac;
    }

    public boolean authorize() {
        return false;
    }

    public Context getContext() {
        return mContext;
    }

    public void setContext(Context mContext) {
        this.mContext = mContext;
    }
}
