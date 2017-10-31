package com.agrhub.sensehub.components.util;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

/**
 * Created by tanca on 10/28/2017.
 */

public class PacketData {
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
        this.mLength = length;
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
        ByteBuffer byteBuffer = ByteBuffer.allocate(input.length * 4);
        IntBuffer intBuffer = byteBuffer.asIntBuffer();
        intBuffer.put(input);

        byte[] output = byteBuffer.array();
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
}
