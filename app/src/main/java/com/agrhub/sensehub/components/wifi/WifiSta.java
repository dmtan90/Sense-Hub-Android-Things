package com.agrhub.sensehub.components.wifi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.util.Log;
import android.net.wifi.WifiManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tanca on 10/17/2017.
 */

public class WifiSta {
    private boolean mIsConnected = false;
    private Context mContext = null;
    private List<ScanResult> lstWifi;

    public WifiSta(Context ctx){
        Log.d(getClass().getSimpleName(), "Create WifiSta");
        mContext = ctx;
        lstWifi = new ArrayList<>();
    }

    public boolean connect(String ssid, String pwd){
        WifiManager wifiManager = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
        WifiConfiguration config = new WifiConfiguration();
        config.SSID = "\"" + ssid + "\"";
        if(pwd.equals("")){
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
        }
        else{
            config.preSharedKey = "\"" + pwd + "\"";
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
        }
        config.status = WifiConfiguration.Status.ENABLED;
        config.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
        if(!wifiManager.isWifiEnabled()){
            wifiManager.setWifiEnabled(true);
        }
        wifiManager.startScan();
        int networkId = wifiManager.addNetwork(config);
        wifiManager.disconnect();
        wifiManager.enableNetwork(networkId, true);
        mIsConnected = wifiManager.reconnect();

        return mIsConnected;
    }

    public List<ScanResult> scanWifi(){
        lstWifi.clear();
        final WifiManager wifi = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
        if (wifi.isWifiEnabled() == false)
        {
            wifi.setWifiEnabled(true);
        }

        mContext.registerReceiver(new BroadcastReceiver()
        {
            @Override
            public void onReceive(Context c, Intent intent)
            {
                lstWifi = wifi.getScanResults();;
            }
        }, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));

        int i = 0;
        while(i < 15){
            i++;
            try {
                Thread.sleep(1000);
            }catch (Exception e){

            }
        }
        return lstWifi;
    }

    public boolean isConnected(){
        return mIsConnected;
    }
}
