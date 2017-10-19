package com.agrhub.sensehub.components.wifi;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.util.Log;

import com.agrhub.sensehub.components.config.Config;

import java.net.InetAddress;
import java.util.List;

/**
 * Created by tanca on 10/17/2017.
 */

public enum WifiManager {
    instance;
    private String mApSSID = "";
    private String mApPWD = "";
    private String mStaSSID = "";
    private String mStaPWD = "";
    private WifiAp mAP = null;
    private WifiSta mSTA = null;
    private Context mContext = null;

    public void init(Context ctx){
        mContext = ctx;
        mApSSID = Config.instance.getApSSID();
        mApPWD = Config.instance.getApPwd();
        mStaSSID = Config.instance.getStaSSID();
        mStaPWD = Config.instance.getStaPwd();

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
        boolean rs = mSTA.connect(mStaSSID, mStaPWD);
        if(rs){
            Config.instance.setStaSSID(mStaSSID);
            Config.instance.setStaPwd(mStaPWD);
            Config.instance.store();
        }
        return rs;
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
