package com.agrhub.sensehub.components.wifi;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.os.AsyncTask;
import android.util.Log;

import com.agrhub.sensehub.components.config.Config;
import com.agrhub.sensehub.components.connector.BroadlinkConnector;
import com.agrhub.sensehub.components.devicemanager.DeviceManager;
import com.agrhub.sensehub.components.entity.Entity;
import com.agrhub.sensehub.components.entity.Rm3SmartRemoteEntity;
import com.agrhub.sensehub.components.entity.Sp3SmartPlugEntity;
import com.agrhub.sensehub.components.util.DeviceName;
import com.agrhub.sensehub.components.util.DeviceState;
import com.agrhub.sensehub.components.util.DeviceType;
import com.agrhub.sensehub.components.util.NetworkUtils;
import com.agrhub.sensehub.components.util.WifiStaConnectionState;
import com.stealthcopter.networktools.ARPInfo;
import com.stealthcopter.networktools.Ping;
import com.stealthcopter.networktools.ping.PingResult;
import com.stealthcopter.networktools.ping.PingStats;

import org.apache.commons.lang3.ArrayUtils;

import java.math.BigInteger;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by tanca on 10/17/2017.
 */

public enum WifiManager {
    instance;
    private final String TAG = getClass().getSimpleName();
    private String mApSSID = "";
    private String mApPWD = "";
    private String mStaSSID = "";
    private String mStaPWD = "";
    private WifiAp mAP = null;
    private WifiSta mSTA = null;
    private Context mContext = null;
    private WifiStaConnectionState mStaState;
    private NetworkSniffTask mNetworkSniffTask = null;
    private boolean mScanning = false;
    private boolean mPolling = false;

    private Map<String, String> mDeviceMap = new HashMap<>();

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
        mStaState = WifiStaConnectionState.DISCONNECTED;

        /*if(!createAP()){
            Log.e(getClass().getSimpleName(), "Can't init AP SSID: " + mApSSID + " PWD: " + mApPWD);
        }*/

        if(!mStaSSID.equals("")){
            connectSTA();
            //Log.e(getClass().getSimpleName(), "Can't connect STA SSID: " + mStaSSID + " PWD: " + mStaPWD);
        }

        mScanning = false;
        mPolling = false;

        Timer t = new Timer();
        TimerTask mSniffTask = new TimerTask() {
            @Override
            public void run() {
                if(!mScanning && !mPolling){
                    scanLocalDevice(true);
                }
            }
        };
        t.scheduleAtFixedRate(mSniffTask, 5000, 60000);
    }

    public boolean createAP(){
        return mAP.configApState();//mAP.init(mApSSID, mApPWD);
    }

    public WifiStaConnectionState connectSTA(){
        mStaState = WifiStaConnectionState.CONNECTING;
        WifiSta.WiFiStaConnectCallback callback = new WifiSta.WiFiStaConnectCallback() {
            @Override
            public void onSuccess() {
                mStaState = WifiStaConnectionState.CONNECTED;
                Config.instance.setStaSSID(mStaSSID);
                Config.instance.setStaPwd(mStaPWD);
                Config.instance.store();
            }

            @Override
            public void onFailed() {
                mStaState = WifiStaConnectionState.DISCONNECTED;
            }
        };
        mSTA.connect(mStaSSID, mStaPWD, callback);
        int i = 0;
        while(i < 10 && !mSTA.isConnected()){
            i++;
            try {
                Thread.sleep(1000);
            }catch (Exception e){

            }
        }
        if(mSTA.isConnected()){
            mStaState = WifiStaConnectionState.CONNECTED;
            //new NetworkSniffTask(mContext).execute();
        }
        else{
            mStaState = WifiStaConnectionState.DISCONNECTED;
        }
        return mStaState;
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

    public void scanLocalDevice(final boolean enable) {
        Log.d(TAG, "scanLocalDevice: " + enable);
        mPolling = false;
        if (enable) {
            mScanning = true;
            mNetworkSniffTask = new NetworkSniffTask(mContext);
            mNetworkSniffTask.execute();
        } else {
            mScanning = false;
            mNetworkSniffTask.cancel(true);
        }
    }

    public void pollLocalDevice(){
        Log.d(TAG, "pollLocalDevice");
        mPolling = true;
        //discovery online device
        Iterator it = mDeviceMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, String> item = (Map.Entry) it.next();
            String macAddress = item.getKey();
            String ip = item.getValue();
            //check is Broadlink device
            if(DeviceManager.instance.getDevice(macAddress) == null){
                if(ip.equals("192.168.137.173")){
                    BroadlinkConnector connector = new BroadlinkConnector();
                    connector.setIP(ip);
                    connector.setMac(macAddress);
                    if(connector.discovery()){
                        Log.d(TAG, "is Broadlink device");
                        Entity entity = null;
                        if(connector.getDevName().equals(DeviceName.DB_DEVICE_NAME_SP3_SMART_PLUG)){
                            entity = new Sp3SmartPlugEntity();
                            entity.setMacAddress(macAddress);
                        }
                        else if(connector.getDevName().equals(DeviceName.DB_DEVICE_NAME_RM3_SMART_REMOTE)){
                            entity = new Rm3SmartRemoteEntity();
                            entity.setMacAddress(macAddress);
                        }

                        if(entity != null){
                            DeviceManager.instance.setDevice(entity);
                        }
                    }
                }
            }
        }
        for(Map.Entry<String, String> entry : mDeviceMap.entrySet()){

        }

        List<Entity> mLocalDevices = new ArrayList<>();
        for(Map.Entry<String, Entity> entity : DeviceManager.instance.getDeviceMap().entrySet()){
            Entity mDev = entity.getValue();
            if(mDev.getDeviceType().getValue() == DeviceType.DB_DEVICE_TYPE_CONTROLLER.getValue() ||
                    mDev.getDeviceType().getValue() == DeviceType.DB_DEVICE_TYPE_CONTAINER.getValue()){
                mLocalDevices.add(mDev);
            }
        }

        for(Entity mDevice : mLocalDevices){
            if(mDevice.getDeviceState().getValue() == DeviceState.DEVICE_CONNECTED.getValue()){
                mDevice.updateData();
            }
        }

        mPolling = false;
    }

    public boolean isScanning() {
        return mScanning;
    }

    public void setScanning(boolean mScanning) {
        this.mScanning = mScanning;
    }

    public boolean isPolling() {
        return mPolling;
    }

    public void setPolling(boolean mPolling) {
        this.mPolling = mPolling;
    }

    public String getDevice(String mac) {
        return mDeviceMap.get(mac);
    }

    public void put(String mac, String ip) {
        mDeviceMap.put(mac, ip);
    }

    static class NetworkSniffTask extends AsyncTask<Void, Void, Void> {

        private static final String TAG = NetworkSniffTask.class.getSimpleName();

        private Context mContextRef;

        public NetworkSniffTask(Context context) {
            mContextRef = context;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            Log.d(TAG, "Let's sniff the network");

            try {
                Context context = mContextRef;

                if (context != null) {

                    ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                    android.net.wifi.WifiManager wm = (android.net.wifi.WifiManager) context.getSystemService(Context.WIFI_SERVICE);

                    WifiInfo connectionInfo = wm.getConnectionInfo();
                    byte[] myIPAddress = BigInteger.valueOf(connectionInfo.getIpAddress()).toByteArray();
                    // you must reverse the byte array before conversion. Use Apache's commons library
                    ArrayUtils.reverse(myIPAddress);
                    InetAddress myInetIP = InetAddress.getByAddress(myIPAddress);
                    String ipString = myInetIP.getHostAddress();


                    Log.d(TAG, "activeNetwork: " + String.valueOf(activeNetwork));
                    Log.d(TAG, "ipString: " + String.valueOf(ipString));

                    String prefix = ipString.substring(0, ipString.lastIndexOf(".") + 1);
                    Log.d(TAG, "prefix: " + prefix);

                    Map<String, String> hosts = new HashMap<>();

                    for (int i = 0; i < 255; i++) {
                        String pingIp = prefix + String.valueOf(i);
                        // Asynchronously
                        Ping.onAddress(pingIp).setTimeOutMillis(1000).setTimes(5).doPing(new Ping.PingListener() {
                            @Override
                            public void onResult(PingResult pingResult) {
                                if(pingResult.isReachable()){
                                    String ip = pingResult.getAddress().getHostAddress();
                                    String macAddress = ARPInfo.getMACFromIPAddress(ip);
                                    if(macAddress == null){
                                        return;
                                    }
                                    macAddress = macAddress.toUpperCase();
                                    Log.i(TAG, "Mac:" + macAddress + " Host: " + String.valueOf(pingResult.getAddress().getHostName()) +
                                            "(" + String.valueOf(ip) + ") is reachable!");

                                    WifiManager.instance.put(macAddress, ip);
                                }
                            }

                            @Override
                            public void onFinished(PingStats pingStats) {

                            }
                        });
                        //PingResult pingResult = Ping.onAddress(pingIp).setTimeOutMillis(5000).doPing();
                    }

                    WifiManager.instance.scanLocalDevice(false);
                }
            } catch (Throwable t) {
                Log.e(TAG, "Well that's not good.", t);
            }

            return null;
        }

        @Override
        protected void onCancelled() {
            WifiManager.instance.setPolling(false);
            WifiManager.instance.pollLocalDevice();
        }
    }
}
