package com.agrhub.sensehub.components.wifi;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.util.Log;

import java.net.InetAddress;
import java.util.List;

/**
 * Created by tanca on 10/17/2017.
 */

public enum WifiManager {
    instance;
    private String mApSSID = "SENSE HUB";
    private String mApPWD = "12345678";
    private String mStaSSID = "";
    private String mStaPWD = "";
    private WifiAp mAP = null;
    private WifiSta mSTA = null;
    private Context mContext = null;

    public void init(Context ctx){
        mContext = ctx;
        if(mAP == null){
            mAP = new WifiAp(mContext);
        }

        if(mSTA == null){
            mSTA = new WifiSta(mContext);
        }

        /*if(!connextAP()){
            Log.e(getClass().getSimpleName(), "Can't init AP SSID: " + mApSSID + " PWD: " + mApPWD);
        }

        if(!mStaSSID.equals("") && !connectSTA()){
            Log.e(getClass().getSimpleName(), "Can't connect STA SSID: " + mStaSSID + " PWD: " + mStaPWD);
        }*/
    }

    public boolean connextAP(){
        return mAP.init(mApSSID, mApPWD);
    }

    public boolean connectSTA(){
        return mSTA.connect(mStaSSID, mStaPWD);
    }

    public void setSTASSID(String ssid){
        mStaSSID = ssid;
    }

    public void setSTAPWD(String pwd){
        mStaPWD = pwd;
    }

    public String getApSSID() {
        return mApSSID;
    }

    public String getApPWD() {
        return mApPWD;
    }

    public String getStaSSID() {
        return mStaSSID;
    }

    public String getStaPWD() {
        return mStaPWD;
    }

    public boolean isConnectedInternet(){
        try {
            InetAddress ipAddr = InetAddress.getByName("google.com"); //You can replace it with your name
            return !ipAddr.equals("");

        } catch (Exception e) {
            return false;
        }
    }

    public List<ScanResult> scanWifi(){
        return mSTA.scanWifi();
    }
}
