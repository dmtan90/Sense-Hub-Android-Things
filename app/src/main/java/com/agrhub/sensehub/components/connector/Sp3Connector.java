package com.agrhub.sensehub.components.connector;

import android.util.Log;

import com.agrhub.sensehub.components.util.PacketData;

import java.util.Arrays;

/**
 * Created by tanca on 10/28/2017.
 */

public class Sp3Connector extends BroadlinkConnector {
    private String TAG = getClass().getSimpleName();
    private final int SP3_CMD_SET_STATE = 0x6a;
    private final int SP3_CMD_GET_STATE = 0x6a;
    private boolean mState;

    public boolean setState(boolean state){
        Log.i(TAG, "setState=" + state);
        int len = 16;
        int[] packet = new int[len];
        Arrays.fill(packet, 0x00);

        packet[0] = 2;
        packet[4] = (state == true ? 1 : 0);

        PacketData inputPacket = new PacketData(packet, len);
        PacketData outData = sendPacket(SP3_CMD_SET_STATE, inputPacket);

        if(outData != null){
            mState = state;
            return true;
        }
        else
        {
            return false;
        }
    }

    public boolean getState(boolean sendPacket){
        Log.i(TAG, "getState");
        if(!sendPacket){
            return mState;
        }

        boolean state = false;
        int len = 16;
        int[] packet = new int[len];
        Arrays.fill(packet, 0x00);

        do
        {
            packet[0] = 1;
            PacketData inputPacket = new PacketData(packet, len);
            PacketData outputPacket = sendPacket(SP3_CMD_GET_STATE, inputPacket);
            if(outputPacket == null || outputPacket.getLength() == 0 || outputPacket.getLength() <= UDP_PACKAGE_SIZE){
                Log.d(TAG, "getState failed");
                //re-authen to get new access token key
                authorize();
                break;
            }

            int err = outputPacket.getBuffer()[0x22] | (outputPacket.getBuffer()[0x23] << 8);
            if(err == 0) {
                //limit decoded data
                len = outputPacket.getLength() - UDP_PACKAGE_SIZE;
                packet = new int[len];

                for(int i = 0; i < len; i++) {
                    packet[i] = outputPacket.getBuffer()[UDP_PACKAGE_SIZE + i];
                }

                inputPacket = new PacketData(packet,len);
                outputPacket = decrypt(inputPacket);
                if(outputPacket == null) {
                    Log.d(TAG, "getState failed");
                    break;
                }

                state = (outputPacket.getBuffer()[0x4] == 1) ? true : false;

                mState = state;
            }
            else {
                Log.d(TAG, "getState failed");
            }

            Log.d(TAG, "getState: " + state);

        } while(false);

        return state;
    }
}
