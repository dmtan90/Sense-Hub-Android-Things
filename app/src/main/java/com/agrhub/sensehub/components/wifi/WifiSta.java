package com.agrhub.sensehub.components.wifi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.os.Handler;
import android.util.Log;
import android.net.wifi.WifiManager;

import com.agrhub.sensehub.components.util.NetworkUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tanca on 10/17/2017.
 */

public class WifiSta {
    private String mTAG = getClass().getSimpleName();
    private boolean mIsConnected = false;
    private Context mContext = null;
    private List<ScanResult> lstWifi;
    private String mSSID;
    private String mPWD;
    private WiFiStaConnectCallback mCallback = null;


    interface WiFiStaConnectCallback{
        void onSuccess();
        void onFailed();
    }

    public WifiSta(Context ctx){
        Log.d(getClass().getSimpleName(), "Create WifiSta");
        mContext = ctx;
        lstWifi = new ArrayList<>();
        BroadcastReceiver broadcastReceiver = new WifiBroadcastReceiver();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION);
        mContext.registerReceiver(broadcastReceiver, intentFilter);
    }

    public class WifiBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (WifiManager.SUPPLICANT_STATE_CHANGED_ACTION .equals(action)) {
                SupplicantState state = intent.getParcelableExtra(WifiManager.EXTRA_NEW_STATE);
                if (SupplicantState.isValidState(state)
                        && state == SupplicantState.COMPLETED) {

                    mIsConnected = checkConnectedToDesiredWifi();
                    if(mCallback != null){
                        if(mIsConnected){
                            mCallback.onSuccess();
                        }
                        else{
                            mCallback.onFailed();
                        }
                    }

                }
                else if(SupplicantState.isValidState(state)
                        && state == SupplicantState.DISCONNECTED){
                    Log.d(mTAG, "Wifi STA is disconnected");
                }
            }
        }

        /** Detect you are connected to a specific network. */
        private boolean checkConnectedToDesiredWifi() {
            boolean connected = false;

            WifiManager wifiManager =
                    (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);

            WifiInfo wifi = wifiManager.getConnectionInfo();
            if (wifi != null) {
                // get current router Mac address
                String ssid = wifi.getSSID();
                if(mSSID != null){
                    connected = mSSID.equals(ssid);
                }
                Log.d(mTAG, "IP: " + NetworkUtils.getIPV4Address(mContext));
            }

            return connected;
        }
    }

    public void connect(String ssid, String pwd, WiFiStaConnectCallback callback){
        if(callback == null){
            return;
        }
        mCallback = callback;
        mSSID = ssid;
        mPWD = pwd;
        WifiManager wifiManager = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
        WifiConfiguration config = new WifiConfiguration();
        config.SSID = "\"" + mSSID + "\"";
        if(pwd.equals("")){
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
        }
        else{
            config.preSharedKey = "\"" + mPWD + "\"";
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
        wifiManager.reconnect();
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
        wifi.startScan();

        int i = 0;
        while(i < 10){
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
