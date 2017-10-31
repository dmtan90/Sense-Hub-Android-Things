package com.agrhub.sensehub.components.wifi;

import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.os.Handler;
import android.util.Log;

import java.lang.reflect.Method;

import cc.mvdan.accesspoint.WifiApControl;

/**
 * Created by tanca on 10/17/2017.
 */

public class WifiAp {
    private boolean mIsEnabled = false;
    private Context mContext = null;
    public WifiAp(Context ctx){
        Log.d(getClass().getSimpleName(), "Create WifiAp");
        mContext = ctx;
    }

    public boolean init(String ssid, String pwd){
        Log.d(getClass().getSimpleName(), "init SSID: " + ssid + " PWD: " + pwd);
        if(ssid == null || ssid == "" || pwd == null){
            return false;
        }
        /*WifiConfiguration wifiConfiguration = new WifiConfiguration();
        wifiConfiguration.SSID = ssid;
        wifiConfiguration.preSharedKey = pwd;
        wifiConfiguration.hiddenSSID = false;
        wifiConfiguration.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
        wifiConfiguration.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
        wifiConfiguration.allowedKeyManagement.set(4);
        wifiConfiguration.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
        wifiConfiguration.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);

        WifiApControl apControl = WifiApControl.getInstance(mContext);
        apControl.setEnabled(wifiConfiguration, true);

        mIsEnabled = apControl.isEnabled();
        if(mIsEnabled){
            Log.d(getClass().getSimpleName(), "AP is enabled IP: " + apControl.getInet4Address());
        }*/

        WifiConfiguration wifiConfiguration = new WifiConfiguration();
        wifiConfiguration.SSID = "\"" + ssid + "\"";
        wifiConfiguration.preSharedKey = "\"" + pwd + "\"";
        wifiConfiguration.hiddenSSID = false;
        wifiConfiguration.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
        wifiConfiguration.status = WifiConfiguration.Status.ENABLED;
        wifiConfiguration.allowedProtocols.set(WifiConfiguration.Protocol.WPA);

        android.net.wifi.WifiManager wifiManager = (android.net.wifi.WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);

        Method[] wmMethods = wifiManager.getClass().getDeclaredMethods(); //Get all declared methods in WifiManager class

        for (Method method: wmMethods){
            if (method.getName().equals("setWifiApEnabled")) {
                try{
                    method.invoke(wifiManager, wifiConfiguration, true);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }

        if(!wifiManager.isWifiEnabled()){
            wifiManager.setWifiEnabled(true);
        }

        /*wifiManager.startLocalOnlyHotspot(new android.net.wifi.WifiManager.LocalOnlyHotspotCallback(){

            @Override
            public void onStarted(android.net.wifi.WifiManager.LocalOnlyHotspotReservation reservation) {
                super.onStarted(reservation);
                Log.d(getClass().getSimpleName(), "Wifi Hotspot is on now");
                mIsEnabled = true;
            }

            @Override
            public void onStopped() {
                super.onStopped();
                Log.d(getClass().getSimpleName(), "onStopped: ");
            }

            @Override
            public void onFailed(int reason) {
                super.onFailed(reason);
                Log.d(getClass().getSimpleName(), "onFailed: ");
                mIsEnabled = false;
            }
        },new Handler());
        /*int mCounter = 0;
        while(mCounter < 10 && !mIsEnabled){
            mCounter++;
            try{
                Thread.sleep(1000);
            }catch (Exception e){

            }
        }*/

        return mIsEnabled;
    }

    //check whether wifi hotspot on or off
    public boolean isApOn() {
        android.net.wifi.WifiManager wifiManager =
                (android.net.wifi.WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
        try {
            Method method = wifiManager.getClass().getDeclaredMethod("isWifiApEnabled");
            method.setAccessible(true);
            return (Boolean) method.invoke(wifiManager);
        }
        catch (Throwable ignored) {}
        return false;
    }

    // toggle wifi hotspot on or off
    public boolean configApState() {
        android.net.wifi.WifiManager wifiManager =
                (android.net.wifi.WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
        WifiConfiguration wificonfiguration = null;
        try {
            // if WiFi is on, turn it off
            if(isApOn()) {
                wifiManager.setWifiEnabled(false);
            }
            Method method = wifiManager.getClass().getMethod("setWifiApEnabled", WifiConfiguration.class, boolean.class);
            method.invoke(wifiManager, wificonfiguration, !isApOn());
            return true;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
