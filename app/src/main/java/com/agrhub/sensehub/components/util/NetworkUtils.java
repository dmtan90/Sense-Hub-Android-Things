package com.agrhub.sensehub.components.util;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.text.format.Formatter;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.text.Format;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

/**
 * Created by tanca on 10/16/2017.
 */

public class NetworkUtils {
    private static String TAG= "NetworkUtils";
    private static int mTimeout = 10000;
    /**
     * Returns MAC address of the given interface name.
     * @param ctx is android context
     * @return  mac address or empty string
     */
    public static String getMACAddress(Context ctx) {
        if(ctx == null){
            return "";
        }
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;

                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return "";
                }

                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    res1.append(Integer.toHexString(b & 0xFF) + ":");
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString().toUpperCase();
            }
        } catch (Exception ex) {
        }
        return "02:00:00:00:00:00";
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

    public static String getApIPV4Address(Context ctx){
        if(ctx == null){
            return "127.0.0.0";
        }
        WifiManager wm = (WifiManager) ctx.getSystemService(Context.WIFI_SERVICE);
        String ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
        String apIP = ip.substring(0, ip.lastIndexOf(".") + 1) + "0";
        Log.d(TAG, "AP: " + apIP);
        return apIP;
    }

    @SuppressWarnings("ConstantConditions")
    public static String getClientIPByMac(String mac)
    {
        String res = "";
        if (mac == null)
            return res;

        String flushCmd = "sh ip -s -s neigh flush all";
        Runtime runtime = Runtime.getRuntime();
        try
        {
            runtime.exec(flushCmd,null,new File("/proc/net"));
        }catch (Exception e){

        }

        BufferedReader br;
        try
        {
            br = new BufferedReader(new FileReader("/proc/net/arp"));
            String line;
            while ((line = br.readLine()) != null)
            {
                String[] sp = line.split(" +");
                if (sp.length >= 4 && mac.equals(sp[3]))
                {
                    String ip = sp[0];
                    if (ip.matches("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}") && sp[2].equals("0x2"))
                    {
                        res = ip;
                        break;
                    }
                }
            }

            br.close();
        }
        catch (Exception e)
        {}

        return res;
    }

    @SuppressWarnings("ConstantConditions")
    public static String getClientMacByIP(String ip)
    {
        String res = "";
        if (ip == null)
            return res;

        String flushCmd = "sh ip -s -s neigh flush all";
        Runtime runtime = Runtime.getRuntime();
        try
        {
            runtime.exec(flushCmd,null,new File("/proc/net"));
        }catch (Exception e){

        }

        BufferedReader br;
        try
        {
            br = new BufferedReader(new FileReader("/proc/net/arp"));
            String line;
            while ((line = br.readLine()) != null)
            {
                String[] sp = line.split(" +");
                if (sp.length >= 4 && ip.equals(sp[0]))
                {
                    //Assistance.Log(sp[0]+sp[2]+sp[3],ALERT_STATES.ALERT_STATE_LOG);
                    String mac = sp[3];
                    if (mac.matches("..:..:..:..:..:..") && sp[2].equals("0x2"))
                    {
                        res = mac;
                        break;
                    }
                }
            }

            br.close();
        }
        catch (Exception e)
        {}

        return res;
    }

    public static PacketData sendUdpPacket(String host, int port, PacketData payload){
        PacketData responseData = null;
        if(payload == null){
            return responseData;
        }
        try {
            InetAddress serverAddress = InetAddress.getByName(host);
            DatagramSocket socket = new DatagramSocket();
            if(!socket.getReuseAddress()){
                socket.setReuseAddress(true);
            }
            if (!socket.getBroadcast()){
                socket.setBroadcast(true);
            }
            StringBuffer buffer = new StringBuffer();
            for(byte b : payload.getBuffer()){
                buffer.append(String.format("%02X\t", b));
            }
            Log.d(TAG, "payload: " + buffer.toString());
            Log.d(TAG, "serverAddress: " + serverAddress.getHostAddress());
            DatagramPacket packet = new DatagramPacket(payload.getBuffer(), payload.getLength(), serverAddress, port);
            socket.send(packet);
            byte[] data = new byte[PacketData.MAX_LENGTH];
            DatagramPacket receivePacket = new DatagramPacket(data, data.length);

            long startTime = System.currentTimeMillis();
            long elapsed;
            while ((elapsed = System.currentTimeMillis() - startTime) < mTimeout) {
                try {
                    socket.send(packet);
                    socket.setSoTimeout(1000);
                    socket.receive(receivePacket);
                    Log.d(TAG, "sendUdpPacket: length=" + receivePacket.getLength());
                    if(packet.getLength() > 0){
                        responseData = new PacketData();
                        responseData.setLength(receivePacket.getLength());
                        responseData.setBuffer(data);
                        PacketData.PrintPacket(responseData.getBuffer());
                    }
                    break;
                } catch (SocketTimeoutException e) {
                    if (elapsed > mTimeout) {
                        break;
                    }

                    continue;
                }
            }

            socket.close();
        } catch (Exception e) {
            Log.e(TAG, "sendUdpPacket: " + e.getMessage());
            e.printStackTrace();
        }
        return responseData;
    }

    /*public static String getIP(String hostMac){
        String ip = null;
        try{
            Enumeration<NetworkInterface> interfaces =
                    NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface i = interfaces.nextElement();
                byte[] mac = i.getHardwareAddress();
                String macAddress = getMacAddressFromBytes(mac);
                if ( macAddress.equals(hostMac)) {
                    Enumeration<InetAddress> inetAddress = i.getInetAddresses();
                    InetAddress in = inetAddress.nextElement();
                    while(in != null){
                        ip = in.getHostName();
                        in = inetAddress.nextElement();
                    }
                    break;
                }

            }
        }catch (Exception e){

        }
        return ip;
    }*/

    public static String getMacAddressFromBytes(byte[] bytes){
        if(bytes == null){
            return null;
        }
        StringBuilder macAddress = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            macAddress.append(String.format("%02X%s", bytes[i], (i < bytes.length - 1) ? ":" : ""));
        }
        return macAddress.toString();
    }

    /*public static String getMacFromIp(String ip){
        String mac = null;
        try{
            String ipAddress = "104.1.168.192";
            String dnsblDomain = "in-addr.arpa";
            Record[] records;
            Lookup lookup = new Lookup(ipAddress + "." + dnsblDomain, Type.PTR);
            SimpleResolver resolver = new SimpleResolver();
            resolver.setAddress(InetAddress.getByName("192.168.1.1"));
            lookup.setResolver(resolver);
            records = lookup.run();

            if(lookup.getResult() == Lookup.SUCCESSFUL) {
                for (int i = 0; i < records.length; i++) {
                    if(records[i] instanceof PTRRecord) {
                        PTRRecord ptr = (PTRRecord) records[i];
                        ptr.rdataToWireCanonical();
                        System.out.println("DNS Record: " + records[0].rdataToString());
                    }
                }
            } else {
                System.out.println("Failed lookup");
            }
        }catch (Exception e){

        }
        return mac;
    }*/
}
