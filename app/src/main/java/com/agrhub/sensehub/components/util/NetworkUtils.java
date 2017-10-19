package com.agrhub.sensehub.components.util;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.text.format.Formatter;

/**
 * Created by tanca on 10/16/2017.
 */

public class NetworkUtils {

    /**
     * Returns MAC address of the given interface name.
     * @param ctx is android context
     * @return  mac address or empty string
     */
    public static String getMACAddress(Context ctx) {
        if(ctx == null){
            return "";
        }
        WifiManager wm = (WifiManager) ctx.getSystemService(Context.WIFI_SERVICE);
        String mac = wm.getConnectionInfo().getMacAddress();
        return mac;
    }

    /**
     * Get IP address from first non-localhost interface
     * @param ctx is android context
     * @return  address or empty string
     */
    public static String getIPV4Address(Context ctx) {
        if(ctx == null){
            return "127.0.0.1";
        }
        WifiManager wm = (WifiManager) ctx.getSystemService(Context.WIFI_SERVICE);
        String ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
        return ip;
    }
}
