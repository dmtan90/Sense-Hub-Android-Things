package com.agrhub.sensehub.components.devicemanager;

import android.app.ActivityManager;
import android.content.Context;

import com.agrhub.sensehub.components.entity.Entity;
import com.agrhub.sensehub.components.util.DeviceName;
import com.agrhub.sensehub.components.util.DeviceType;
import com.agrhub.sensehub.components.util.NetworkUtils;
import com.agrhub.sensehub.components.util.UpdateState;
import com.agrhub.sensehub.components.wifi.WifiManager;

import java.util.Formatter;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by tanca on 10/18/2017.
 */

public enum DeviceManager {
    instance;
    public String HW_VERSION = "AH_SH_2.0";
    public String SW_VERSION = "0.0.1";
    public long   SW_DATE = 1508299694591L;
    public String COMPANY_NAME = "AGRHUB CO.,LTD.";
    public String PRODUCT_NAME = "SENSE HUB 2 PRO";
    public long   PRODUCT_RELEASE_DATE = 1508299694591L;
    public UpdateState   UPDATE_STATE = UpdateState.SW_UPDATE_MODE_STABLE;

    Map<String, Entity> mDeviceMap = new HashMap<>();

    private Context mContext = null;

    public void setContext(Context ctx){
        mContext = ctx;
    }

    public Entity getDevice(String mac){
        return mDeviceMap.get(mac);
    }

    public void setDevice(Entity device){
        if(getDevice(device.getMacAddress()) == null){
            mDeviceMap.put(device.getMacAddress(), device);
        }
    }

    public String getDevicesJson(){
        StringBuffer mBuffer = new StringBuffer();
        StringBuffer mDevicesBuffer = new StringBuffer();
        mDevicesBuffer.append("[");
        for(Map.Entry<String, Entity> entity : mDeviceMap.entrySet()){
            Entity device = entity.getValue();
            if(mDevicesBuffer.length() > 1){
                mDevicesBuffer.append(",");
            }
            mDevicesBuffer.append(device.toString());
        }
        mDevicesBuffer.append("]");

        mBuffer.append(String.format("{" +
                        "\"gateway_mac_address\":\"%s\"," +
                        "\"gateway_name\":%d," +
                        "\"gateway_type\":%d," +
                        "\"timestamp\":%s," +
                        "\"freemem\":%d," +
                        "\"devices\":%s}",
                NetworkUtils.getMACAddress(mContext),
                DeviceName.DB_DEVICE_NAME_GATEWAY_ANDROID_THINGS.getValue(),
                DeviceType.DB_DEVICE_TYPE_GATEWAY.getValue(),
                System.currentTimeMillis(),
                getFreeMem(),
                mDevicesBuffer.toString()));
        return mBuffer.toString();
    }

    public String getGatewayInfo(){
        StringBuffer mBuffer = new StringBuffer();

        mBuffer.append(String.format("{\"hw_version\":\"%s\",\"sw_version\":\"%s\",\"sw_date\":%d, " +
                "\"ap_ssid\":\"%s\",\"ap_pwd\":\"%s\",\"sta_ssid\":\"%s\",\"sta_pwd\":\"%s\"," +
                "\"company_name\":\"%s\",\"product_name\":\"%s\",\"product_serial\":\"%s\", " +
                "\"product_release_date\":%d,\"update_state\":%d}",
                HW_VERSION,
                SW_VERSION,
                SW_DATE,
                WifiManager.instance.getApSSID(),
                WifiManager.instance.getApPWD(),
                WifiManager.instance.getStaSSID(),
                WifiManager.instance.getStaPWD(),
                COMPANY_NAME,
                PRODUCT_NAME,
                NetworkUtils.getMACAddress(mContext),
                PRODUCT_RELEASE_DATE,
                UPDATE_STATE.getValue()));
        return mBuffer.toString();
    }

    public long getFreeMem(){
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        ActivityManager activityManager = (ActivityManager)mContext.getSystemService(Context.ACTIVITY_SERVICE);
        activityManager.getMemoryInfo(mi);
        long availableMegs = mi.availMem / 1048576L;
        return availableMegs;
    }

    public Map<String, Entity> getDeviceMap(){
        return mDeviceMap;
    }
}
