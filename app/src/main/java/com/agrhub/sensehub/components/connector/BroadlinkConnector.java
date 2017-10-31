package com.agrhub.sensehub.components.connector;

import android.util.Log;

import com.agrhub.sensehub.components.util.BroadlinkDeviceType;
import com.agrhub.sensehub.components.util.DeviceName;
import com.agrhub.sensehub.components.util.NetworkUtils;
import com.agrhub.sensehub.components.util.PacketData;
import com.google.api.client.util.Base64;

import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by tanca on 10/28/2017.
 */

public class BroadlinkConnector extends Connector {
    private String TAG = getClass().getSimpleName();
    static final int KEY_SIZE = 16;
    static final int ID_SIZE = 4;
    static final int BROADLINK_CMD_AUTH = 0x65;
    static final int[] DEFAULT_KEY = {0x09, 0x76, 0x28, 0x34, 0x3f, 0xe9, 0x9e, 0x23, 0x76, 0x5c, 0x15, 0x13, 0xac, 0xcf, 0x8b, 0x02};
    static final int[] DEFAULT_IV = {0x56, 0x2e, 0x17, 0x99, 0x6d, 0x09, 0x3d, 0x28, 0xdd, 0xb3, 0xba, 0x69, 0x5a, 0x2e, 0x6f, 0x58};
    static final int UDP_PACKAGE_SIZE = 56;

    private int[] mKey;
    private int[] mIV;
    private int[] mID;
    private int mPort;
    public BroadlinkConnector(){
        super();
        mKey = DEFAULT_KEY;
        mIV = DEFAULT_IV;
        mPort = 80;
    }

    @Override
    public boolean authorize() {
        boolean rs = false;
        Log.d(TAG, "authorize");
        if(getDevName() == DeviceName.DB_DEVICE_NAME_SP3_SMART_PLUG){
            mKey = DEFAULT_KEY;
            mIV = DEFAULT_IV;
        }

        //sync IP address
        String ip = NetworkUtils.getIP(getMac());
        setIP(ip);

        int len = 80;
        int[] payload = new int[len];
        Arrays.fill(payload, 0x00);
        payload[0x04] = 0x31;
        payload[0x05] = 0x31;
        payload[0x06] = 0x31;
        payload[0x07] = 0x31;
        payload[0x08] = 0x31;
        payload[0x09] = 0x31;
        payload[0x0a] = 0x31;
        payload[0x0b] = 0x31;
        payload[0x0c] = 0x31;
        payload[0x0d] = 0x31;
        payload[0x0e] = 0x31;
        payload[0x0f] = 0x31;
        payload[0x10] = 0x31;
        payload[0x11] = 0x31;
        payload[0x12] = 0x31;
        payload[0x1e] = 0x01;
        payload[0x2d] = 0x01;
        payload[0x30] = 'T';
        payload[0x31] = 'e';
        payload[0x32] = 's';
        payload[0x33] = 't';
        payload[0x34] = ' ';
        payload[0x35] = ' ';
        payload[0x36] = '1';

        PacketData inputPacket = new PacketData(payload, len);
        PacketData outputPacket = sendPacket(BROADLINK_CMD_AUTH, inputPacket);

        if(outputPacket == null || outputPacket.getLength() == PacketData.MAX_LENGTH
                || outputPacket.getLength() == 0 || outputPacket.getLength() <= UDP_PACKAGE_SIZE){
            Log.d(TAG, "authorize fail: len=" + outputPacket.getLength());
            return rs;
        }

        int outLen = outputPacket.getLength() - UDP_PACKAGE_SIZE;
        int[] outData = outputPacket.getIntBuffer();
        for(int i = 0; i < outLen; i++){
            payload[i] = outData[UDP_PACKAGE_SIZE + i];
        }
        inputPacket.setLength(outLen);
        inputPacket.setIntBuffer(payload);

        outputPacket = decrypt(inputPacket);
        if(outputPacket != null){
            rs = true;
            for(int i = 0; i < KEY_SIZE; i++){
                mKey[i] = outputPacket.getIntBuffer()[0x04+i];
            }

            for(int i = 0; i < ID_SIZE; i++){
                mID[i] = outputPacket.getIntBuffer()[i];
            }
        }

        return rs;
    }

    public boolean discovery(){
        Log.d(TAG, "discovery");
        boolean rs = false;
        int len = 0x38;
        int[] packet = new int[len];
        Arrays.fill(packet, 0x00);
        packet[0x09] = 0;
        packet[0x0a] = 0;
        packet[0x0b] = 0;
        packet[0x18] = 192;
        packet[0x19] = 168;
        packet[0x1a] = 4;
        packet[0x1b] = 1;
        packet[0x1c] = mPort & 0xff;
        packet[0x1d] = mPort >> 8;
        packet[0x26] = 6;
        int checksum = 0xbeaf;

        for(int i : packet) {
            checksum += i;
        }
        checksum = checksum & 0xffff;
        packet[0x20] = checksum & 0xff;
        packet[0x21] = checksum >> 8;
        PacketData inputPacket = new PacketData(packet, len);
        PacketData outputPacket = NetworkUtils.sendUdpPacket(getIP(), mPort, inputPacket);

        if(outputPacket != null && outputPacket.getLength() < PacketData.MAX_LENGTH){
            byte[] output = outputPacket.getBuffer();
            int deviceType = output[0x34] | output[0x35] << 8;

            BroadlinkDeviceType device = BroadlinkDeviceType.getDevice(deviceType);
            if(device.getValue() == BroadlinkDeviceType.SP3_DEVICE_TYPE.getValue()){
                setDevName(DeviceName.DB_DEVICE_NAME_SP3_SMART_PLUG);
                rs = true;
            }
            else if(device.getValue() == BroadlinkDeviceType.RMMINI_DEVICE_TYPE.getValue()){
                setDevName(DeviceName.DB_DEVICE_NAME_RM3_SMART_REMOTE);
                rs = true;
            }

            if(device.getValue() != BroadlinkDeviceType.UNKNOWN_DEVICE_TYPE.getValue()){
                Log.d(TAG, "discovery - is Broadlink device");
            }
        }
        return rs;
    }

    public PacketData encrypt(PacketData input){
        PacketData output = null;
        try {
            IvParameterSpec iv = new IvParameterSpec(PacketData.ConvertToByteArray(mIV));
            SecretKeySpec skeySpec = new SecretKeySpec(PacketData.ConvertToByteArray(mKey), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

            byte[] encrypted = cipher.doFinal(input.getBuffer());
            System.out.println("encrypted string: "
                    + Base64.encodeBase64String(encrypted));

            output = new PacketData(encrypted, encrypted.length);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return output;
    }

    public PacketData decrypt(PacketData input){
        PacketData output = null;
        try {
            IvParameterSpec iv = new IvParameterSpec(PacketData.ConvertToByteArray(mIV));
            SecretKeySpec skeySpec = new SecretKeySpec(PacketData.ConvertToByteArray(mKey), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);

            byte[] original = cipher.doFinal(Base64.decodeBase64(input.getBuffer()));

            output = new PacketData(original, original.length);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return output;
    };

    public PacketData sendPacket(int cmd, PacketData payload){
        PacketData outputData = null;
        int length = UDP_PACKAGE_SIZE + payload.getLength();
        int[] packet = new int[length];
        //for example: aa:aa:aa:aa:aa:aa = 61613a61613a61613a61613a61613a6161
        byte[] mac = getMac().getBytes();
        do
        {
            Arrays.fill(packet, 0x00);
            packet[0x00] = 0x5a;
            packet[0x01] = 0xa5;
            packet[0x02] = 0xaa;
            packet[0x03] = 0x55;
            packet[0x04] = 0x5a;
            packet[0x05] = 0xa5;
            packet[0x06] = 0xaa;
            packet[0x07] = 0x55;
            packet[0x24] = 0x2a;
            packet[0x25] = 0x27;
            packet[0x26] = cmd;
            packet[0x2a] = mac[0]|mac[1];
            packet[0x2b] = mac[3]|mac[4];
            packet[0x2c] = mac[6]|mac[7];
            packet[0x2d] = mac[9]|mac[10];
            packet[0x2e] = mac[12]|mac[13];
            packet[0x2f] = mac[15]|mac[16];
            packet[0x30] = mID[0];
            packet[0x31] = mID[1];
            packet[0x32] = mID[2];
            packet[0x33] = mID[3];

            int checksum = 0xbeaf;
            for(int i = 0; i < payload.getLength();i++)
            {
                checksum += payload.getBuffer()[i];
                checksum = checksum & 0xffff;
            }

            PacketData encryptData = encrypt(payload);
            if(encryptData == null)
            {
                Log.e(TAG, "sendPacket: encrypt fail");
                break;
            }

            packet[0x34] = checksum & 0xff;
            packet[0x35] = checksum >> 8;

            for(int i = 0; i < encryptData.getLength(); i++)
            {
                packet[UDP_PACKAGE_SIZE + i] = encryptData.getBuffer()[i];
            }
            checksum = 0xbeaf;
            for(int  i = 0 ; i < length; i++)
            {
                checksum += packet[i];
                checksum = checksum & 0xffff;
            }

            packet[0x20] = checksum & 0xff;
            packet[0x21] = checksum >> 8;

            PacketData inputData = new PacketData(packet, length);
            outputData = NetworkUtils.sendUdpPacket(getIP(), mPort, inputData);
        } while(false);

        return outputData;
    }
}
