package com.agrhub.sensehub.components.util;

import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

/**
 * Created by tanca on 10/28/2017.
 */

public class PacketData {
    private static final String TAG = PacketData.class.getSimpleName();
    public static int MAX_LENGTH = 4096;
    private byte[] mBuffer;
    private int mLength;

    public PacketData(){
        mLength = MAX_LENGTH;
        mBuffer = new byte[mLength];
    }

    public PacketData(byte[] buffer, int length){
        this.mBuffer = buffer;
        this.mLength = length;
    }

    public PacketData(int[] buffer, int length){
        this.mBuffer = ConvertToByteArray(buffer);
        this.mLength = this.mBuffer.length;
    }

    public byte[] getBuffer() {
        return mBuffer;
    }

    public int[] getIntBuffer() {
        return ConvertToIntArray(mBuffer);
    }

    public void setBuffer(byte[] mBuffer) {
        this.mBuffer = mBuffer;
    }

    public void setIntBuffer(int[] mBuffer) {
        this.mBuffer = ConvertToByteArray(mBuffer);
    }

    public int getLength() {
        return mLength;
    }

    public void setLength(int mLength) {
        this.mLength = mLength;
    }

    public static byte[] ConvertToByteArray(int[] input){
        /*ByteBuffer byteBuffer = ByteBuffer.allocate(input.length*4);
        IntBuffer intBuffer = byteBuffer.asIntBuffer();
        intBuffer.put(input);*/
        byte[] output = new byte[input.length];
        for(int i = 0; i < input.length; i++){
            output[i] = (byte)input[i];
        }

        //byte[] output = byteBuffer.array();
        return output;
    }

    public static int[] ConvertToIntArray(byte[] input){
        IntBuffer intBuf =
                ByteBuffer.wrap(input)
                        .order(ByteOrder.BIG_ENDIAN)
                        .asIntBuffer();
        int[] output = new int[intBuf.remaining()];
        intBuf.get(output);
        return output;
    }

    public static void PrintPacket(int[] input){
        StringBuffer buffer = new StringBuffer();
        for(int i : input){
            buffer.append(String.format("%02X\t", i));
        }
        Log.d(TAG, "PrintPacket: " + buffer.toString());
    }

    public static void PrintPacket(byte[] input){
        StringBuffer buffer = new StringBuffer();
        for(byte b : input){
            buffer.append(String.format("%02X\t", b));
        }
        Log.d(TAG, "PrintPacket: " + buffer.toString());
    }
}
