package com.agrhub.sensehub.components.Connector;


import android.util.Log;

import com.agrhub.sensehub.components.util.DeviceName;
import com.agrhub.sensehub.components.util.ErrorType;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.util.Arrays;

/**
 * Created by KIMDUONG on 11/8/2017.
 */

public class BroadLinkConnector {
     static int ID_SIZE = 4;
     static int IP_SiZE = 4;
     static int MAC_SIZE = 6;
     static int KEY_SIZE = 16;
     static int IV_SIZE  = 16;
     static int BUF_SIZE = 1024;
     static int UDP_PACKGE_SIZE = 56;
  //
     protected int[] mId = new int[ID_SIZE];
     protected int[] mIp = new int[IP_SiZE];
     protected int[] mMac = new int[MAC_SIZE];
     protected int[] mKey = new int [KEY_SIZE];
     protected int[] mIV;
     protected int mPort;
     protected int[] mInternalBuf=new int [BUF_SIZE];
     protected int mInternalLenght;
     protected String mHost;
     protected DeviceName mDevName;




    static final int BROADLINK_CMD_AUTH = 0x65;
    static final int SP3_DEVICE_TYPE = 0x753e;
    static final int RM3_DEVICE_TYPE = 0x2737;

    private String mTAG = "BROADLINK";
    private final char default_Key[] ={0x09, 0x76, 0x28, 0x34, 0x3f, 0xe9, 0x9e, 0x23, 0x76, 0x5c, 0x15, 0x13, 0xac, 0xcf, 0x8b, 0x02};
    private final char default_IV[] = {0x56, 0x2e, 0x17, 0x99, 0x6d, 0x09, 0x3d, 0x28, 0xdd, 0xb3, 0xba, 0x69, 0x5a, 0x2e, 0x6f, 0x58};

//    public  BroadLinkConnector(String ip, String mac){
//
//        Log.i(mTAG,String.format("BroadLink_Connector: %s  %s",ip,mac));
//        //fill ip
//        String[] partsId = ip.split("\\.");
//        for (int i = 0;partsId.length < i; i++){
//            mIp[i] = Integer.parseInt(partsId[i]);
//        }
//        //fill mac
//        String[] partsMac = mac.split(":");
//        for(int i = 0; partsMac.length < i ;i++){
//            mMac[i] = Integer.valueOf(partsMac[i]);
//        }
//        for(int i= 0; i < KEY_SIZE; i++){
//            mKey[i] = default_Key[i];
//            mIV[i] = default_IV[i];
//        }
//        mPort = 80;
//        mDevName = DeviceName.DB_DEVICE_NAME_UNKNOWN;
//    }
//    public  boolean Authorize(){
//        return true;
//
//    }
//    public boolean Discover(){
//        Log.i(mTAG,"Discover...");
//        boolean isBoardLinkDev = false;
//        int len = 0x30;
//        int[] packet = new int[len];
//
//
//        int port = 80;
//
//        packet[0x09] = 0;
//        packet[0x0a] = 0;
//        packet[0x0b] = 0;
//        packet[0x18] = 192;
//        packet[0x19] = 168;
//        packet[0x1a] = 4;
//        packet[0x1b] = 1;
//        packet[0x1c] = port & 0xff;
//        packet[0x1d] = port >> 8;
//        packet[0x26] = 6;
//        int checksum = 0xbeaf;
//        for (int i=0;i<packet.length;i++){
//            checksum += packet[i];
//        }
//        checksum = checksum & 0xffff;
//        packet[0x20] = checksum & 0xff;
//        packet[0x21] = checksum >> 8;
//        int response_len = 0;
//
//
//        return  isBoardLinkDev;
//
//
//
//
//
//    }
//    public void setIP(String ip){
//
//        String[] temp = ip.split("\\.");
//        for (int i = 0;temp.length < i; i++){
//            mIp[i] = Integer.parseInt(temp[i]);
//        }
//        mHost = String.format("%d.%d.%d.%d",mIp[0], mIp[1], mIp[2], mIp[3]);
//
//    }
//    public String getDeviceID(){
//        Log.i(mTAG,"getDeviceID ");
//        String id = String.format("%c%c%c%c", mId[0], mId[1], mId[2], mId[3]);
//        return id;
//
//
//    }
//    public DeviceName getDeviceName(){
//
//        return mDevName;
//    }
//    ErrorType sendPacket(int cmd, int input[] , int input_len, int size , int response_len){
//        //check IP before sending
//        char[] mac = new char[20];
//
//
//        String ip = new String();
//        if(ip == null){
//            return ErrorType.TYPE_FAIL;
//
//        }
//        else {
//            setIP(ip);
//            ip = null;
//        }
//        if( -1  == mHost.indexOf("192.168.4."))
//        {
//            Log.d(mTAG,String.format("sendPacket - Invalid IP %s", mHost));
//            return ErrorType.TYPE_FAIL;
//
//        }
////            int length = UDP_PACKGE_SIZE + input_len;
////            int []packet = new int[length];
////            ErrorType res = ErrorType.TYPE_OKE;
////            do{
////                packet[0x00] = 0x5a;
////                packet[0x01] = 0xa5;
////                packet[0x02] = 0xaa;
////                packet[0x03] = 0x55;
////                packet[0x04] = 0x5a;
////                packet[0x05] = 0xa5;
////                packet[0x06] = 0xaa;
////                packet[0x07] = 0x55;
////                packet[0x24] = 0x2a;
////                packet[0x25] = 0x27;
////                packet[0x26] = cmd;
////                packet[0x2a] = mMac[0];
////                packet[0x2b] = mMac[1];
////                packet[0x2c] = mMac[2];
////                packet[0x2d] = mMac[3];
////                packet[0x2e] = mMac[4];
////                packet[0x2f] = mMac[5];
////                packet[0x30] = mId[0];
////                packet[0x31] = mId[1];
////                packet[0x32] = mId[2];
////                packet[0x33] = mId[3];
////                int checksum = 0xbeaf;
////                for(int i = 0; i < input_len;i++)
////                {
////                    checksum += input[i];
////                    checksum = checksum & 0xffff;
////                }
////                res = encrypt(input, input_len);
////                if( res == ErrorType.TYPE_FAIL){
////                    Log.d(mTAG,"sendPacket: encrypt fail");
////                    break;
////                }
////                packet[0x34] = checksum & 0xff;
////                packet[0x35] = checksum >> 8;
//////                for(int i = 0; i < input_len; i++)
//////                {
//////                    packet[UDP_PACKAGE_SIZE + i] = mInternalBuf[i];
//////                }
////                checksum = 0xbeaf;
////                for(int  i = 0 ; i < length; i++)
////                {
////                    checksum += packet[i];
////                    checksum = checksum & 0xffff;
////                }
////                checksum = 0xbeaf;
////                for(int  i = 0 ; i < length; i++)
////                {
////                    checksum += packet[i];
////                    checksum = checksum & 0xffff;
////                }
////
////                packet[0x20] = checksum & 0xff;
////                packet[0x21] = checksum >> 8;
////
////                DatagramPacket packetSend;
////                DatagramSocket socket;
////            }while (true);
////            return ErrorType.TYPE_OKE;
////
////
////
////
////
//
//    }
//
//    public ErrorType encrypt(int[] payload, int len){
//
//
//        return ErrorType.TYPE_OKE;
//
//
//    }



}

