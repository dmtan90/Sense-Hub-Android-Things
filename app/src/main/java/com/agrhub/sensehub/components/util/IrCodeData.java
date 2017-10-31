package com.agrhub.sensehub.components.util;

/**
 * Created by tanca on 10/29/2017.
 */

public class IrCodeData {
    private final int MAX_KEY_CODE_SIZE = 512;
    private int[] mIrCodeData = new int[MAX_KEY_CODE_SIZE];
    int mIrCodeLen;

    public IrCodeData(int[] data, int length){
        this.mIrCodeData = data;
        this.mIrCodeLen = length;
    }

    public IrCodeData(byte[] data, int length){
        this.mIrCodeData = PacketData.ConvertToIntArray(data);
        this.mIrCodeLen = length;
    }

    public int[] getIrCodeData() {
        return mIrCodeData;
    }

    public void setIrCodeData(int[] mIrCodeData) {
        this.mIrCodeData = mIrCodeData;
    }

    public int getIrCodeLen() {
        return mIrCodeLen;
    }

    public void setIrCodeLen(int mIrCodeLen) {
        this.mIrCodeLen = mIrCodeLen;
    }
}
