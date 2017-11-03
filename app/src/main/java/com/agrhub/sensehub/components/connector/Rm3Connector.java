package com.agrhub.sensehub.components.connector;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;
import android.util.Log;

import com.agrhub.sensehub.components.util.AirConditionerCmd;
import com.agrhub.sensehub.components.util.AirConditionerMode;
import com.agrhub.sensehub.components.util.AirConditionerPower;
import com.agrhub.sensehub.components.util.AirConditionerState;
import com.agrhub.sensehub.components.util.IrCodeData;
import com.agrhub.sensehub.components.util.PacketData;
import com.agrhub.sensehub.components.util.Rm3IrCodeData;
import com.agrhub.sensehub.components.util.StorageUtils;

import java.util.Arrays;

/**
 * Created by tanca on 10/28/2017.
 */

public class Rm3Connector extends BroadlinkConnector {
    private String TAG = getClass().getSimpleName();
    private final int RM3_CMD_SEND_DATA = 0x6a;
    private final int RM3_CMD_ENT_LEARN = 0x6a;
    private final int RM3_CMD_CHK_DATA = 0x6a;
    private final int KEY_CODE_SIZE = 512;

    private Rm3IrCodeData mIrCode;
    private AirConditionerState mState;

    public Rm3Connector(){
        mIrCode = new Rm3IrCodeData();
        loadKey();
    }

    @Override
    public boolean authorize() {
        super.discovery();
        return super.authorize();
    }

    public boolean setState(AirConditionerState state){
        //-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
        //Update power state: ON, OFF, LEAVE, ...
        //-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
        Log.d(TAG, "setState");
        boolean rs = false;
        AirConditionerMode oldMode = state.getAcMode();

        mState = state;

        StringBuffer mBuffer = new StringBuffer();
        IrCodeData irData = null;

        if(AirConditionerPower.AIR_CONDITIONER_POWER_ON == state.getAcPower()) {
            mBuffer.append(String.format("Sending PowerOn IR code len=%d", mIrCode.getIrPwrOn().getIrCodeLen()));
            irData = mIrCode.getIrPwrOn();
        }
        else if(AirConditionerPower.AIR_CONDITIONER_POWER_OFF == state.getAcPower()) {
            mBuffer.append(String.format("Sending PowerOff IR code len=%d", mIrCode.getIrPwrOff().getIrCodeLen()));
            irData = mIrCode.getIrPwrOff();
        }
        else if(AirConditionerPower.AIR_CONDITIONER_POWER_LEAVE == state.getAcPower()) {
            //Do nothing
        }
        else {
            //Extension
        }
        if(irData != null){
            rs = sendIRCode(irData);
        }
        Log.d(TAG, mBuffer.toString());

        //-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
        //Update working temperature: 12, 14, 16 ...
        //-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
        mBuffer.setLength(0);
        irData = null;
        if(AirConditionerMode.AIR_CONDITIONER_MODE_NORMAL == state.getAcMode()) {
            mState.setAcMode(oldMode);
        }

        if(AirConditionerMode.AIR_CONDITIONER_MODE_TEMP_16 != state.getAcMode()) {
            mBuffer.append(String.format("Sending Mode Temp %s*C IR code len=%d",
                    state.getAcMode().getValueString(), mIrCode.getIrTemp16().getIrCodeLen()));
            irData = mIrCode.getIrTemp16();
        }
        else if(AirConditionerMode.AIR_CONDITIONER_MODE_TEMP_18 != state.getAcMode()) {
            mBuffer.append(String.format("Sending Mode Temp %s*C IR code len=%d",
                    state.getAcMode().getValueString(), mIrCode.getIrTemp18().getIrCodeLen()));
            irData = mIrCode.getIrTemp18();
        }
        else if(AirConditionerMode.AIR_CONDITIONER_MODE_TEMP_20 != state.getAcMode()) {
            mBuffer.append(String.format("Sending Mode Temp %s*C IR code len=%d",
                    state.getAcMode().getValueString(), mIrCode.getIrTemp20().getIrCodeLen()));
            irData = mIrCode.getIrTemp20();
        }
        else if(AirConditionerMode.AIR_CONDITIONER_MODE_TEMP_22 != state.getAcMode()) {
            mBuffer.append(String.format("Sending Mode Temp %s*C IR code len=%d",
                    state.getAcMode().getValueString(), mIrCode.getIrTemp22().getIrCodeLen()));
            irData = mIrCode.getIrTemp22();
        }
        else if(AirConditionerMode.AIR_CONDITIONER_MODE_TEMP_24 != state.getAcMode()) {
            mBuffer.append(String.format("Sending Mode Temp %s*C IR code len=%d",
                    state.getAcMode().getValueString(), mIrCode.getIrTemp24().getIrCodeLen()));
            irData = mIrCode.getIrTemp24();
        }
        else if(AirConditionerMode.AIR_CONDITIONER_MODE_TEMP_26 != state.getAcMode()) {
            mBuffer.append(String.format("Sending Mode Temp %s*C IR code len=%d",
                    state.getAcMode().getValueString(), mIrCode.getIrTemp26().getIrCodeLen()));
            irData = mIrCode.getIrTemp26();
        }
        else if(AirConditionerMode.AIR_CONDITIONER_MODE_TEMP_28 != state.getAcMode()) {
            mBuffer.append(String.format("Sending Mode Temp %s*C IR code len=%d",
                    state.getAcMode().getValueString(), mIrCode.getIrTemp28().getIrCodeLen()));
            irData = mIrCode.getIrTemp28();
        }
        else if(AirConditionerMode.AIR_CONDITIONER_MODE_TEMP_30 != state.getAcMode()) {
            mBuffer.append(String.format("Sending Mode Temp %s*C IR code len=%d",
                    state.getAcMode().getValueString(), mIrCode.getIrTemp30().getIrCodeLen()));
            irData = mIrCode.getIrTemp30();
        }
        else{
            //Do nothing since no power
        }

        if(irData != null){
            rs = sendIRCode(irData);
        }

        Log.d(TAG, mBuffer.toString());

        return rs;
    }

    public AirConditionerState getACState(){
        return mState;
    }

    public boolean learnIRCode(AirConditionerCmd cmd){
        Log.d(TAG, "Learn IR Code, please get your remote ready");
        boolean state = false;

        Log.d(TAG, String.format("learnIRCode: %s", cmd.getStringValue()));
        if(false == EnterLearning())
        {
            return state;
        }
        try{
            Thread.sleep(1000);
        }catch (Exception e){

        }

        int count = 0;
        IrCodeData code = CheckData();
        while(code == null && count < 10) {
            try{
                Thread.sleep(1000);
            }catch (Exception e){

            }
            code = CheckData();
            count++;
        }

        if(code != null) {
            //remove padding
            Log.d(TAG, String.format("learnIRCode: code len=%d", code.getIrCodeLen()));

            for(int i = 0; i < code.getIrCodeLen(); i++) {
                Log.d(TAG,String.format("0x%.02x ", code.getIrCodeData()[i]));
            }

            //store key code
            if(AirConditionerCmd.AIR_CONDITIONER_CMD_PWR_ON.getValue() == cmd.getValue()) {
                Arrays.fill(mIrCode.getIrPwrOn().getIrCodeData(), 0x00);
                mIrCode.setIrPwrOn(code);
            }
            else if(AirConditionerCmd.AIR_CONDITIONER_CMD_PWR_OFF.getValue() == cmd.getValue()) {
                Arrays.fill(mIrCode.getIrPwrOff().getIrCodeData(), 0x00);
                mIrCode.setIrPwrOff(code);
            }
            else if(AirConditionerCmd.AIR_CONDITIONER_CMD_TMP_16.getValue() == cmd.getValue()) {
                Arrays.fill(mIrCode.getIrTemp16().getIrCodeData(), 0x00);
                mIrCode.setIrTemp16(code);
            }
            else if(AirConditionerCmd.AIR_CONDITIONER_CMD_TMP_18.getValue() == cmd.getValue()) {
                Arrays.fill(mIrCode.getIrTemp18().getIrCodeData(), 0x00);
                mIrCode.setIrTemp18(code);
            }
            else if(AirConditionerCmd.AIR_CONDITIONER_CMD_TMP_20.getValue() == cmd.getValue()) {
                Arrays.fill(mIrCode.getIrTemp20().getIrCodeData(), 0x00);
                mIrCode.setIrTemp20(code);
            }
            else if(AirConditionerCmd.AIR_CONDITIONER_CMD_TMP_22.getValue() == cmd.getValue()) {
                Arrays.fill(mIrCode.getIrTemp22().getIrCodeData(), 0x00);
                mIrCode.setIrTemp22(code);
            }
            else if(AirConditionerCmd.AIR_CONDITIONER_CMD_TMP_24.getValue() == cmd.getValue()) {
                Arrays.fill(mIrCode.getIrTemp24().getIrCodeData(), 0x00);
                mIrCode.setIrTemp24(code);
            }
            else if(AirConditionerCmd.AIR_CONDITIONER_CMD_TMP_26.getValue() == cmd.getValue()) {
                Arrays.fill(mIrCode.getIrTemp26().getIrCodeData(), 0x00);
                mIrCode.setIrTemp26(code);
            }
            else if(AirConditionerCmd.AIR_CONDITIONER_CMD_TMP_28.getValue() == cmd.getValue()) {
                Arrays.fill(mIrCode.getIrTemp28().getIrCodeData(), 0x00);
                mIrCode.setIrTemp28(code);
            }
            else if(AirConditionerCmd.AIR_CONDITIONER_CMD_TMP_30.getValue() == cmd.getValue()) {
                Arrays.fill(mIrCode.getIrTemp30().getIrCodeData(), 0x00);
                mIrCode.setIrTemp30(code);
            }

            storeKey();
            state = true;
        }

        return state;
    }

    public void loadKey(){
        mIrCode = StorageUtils.loadRm3IrCodeData(getContext());
    }

    public void storeKey(){
        StorageUtils.storeIrCodeData(getContext(), mIrCode);
    }

    private boolean EnterLearning(){
        Log.d(TAG, "EnterLearning");
        int len = 16;
        int[] packet = new int[len];
        Arrays.fill(packet, 0x00);
        packet[0] = 3;
        PacketData inputData = new PacketData(packet, len);
        PacketData outputData = sendPacket(RM3_CMD_ENT_LEARN, inputData);
        if(outputData == null || outputData.getLength() == 0 || outputData.getLength() <= UDP_PACKAGE_SIZE)
        {
            Log.e(TAG, "Fail to enter learning mode");
            if(true == authorize()) {
                return EnterLearning();
            }
            return false;
        }
        return true;
    }

    private IrCodeData CheckData(){
        //ESP_LOGI(TAG, "check_data");
        int len = 16;
        int[] packet = new int[len];
        Arrays.fill(packet, 0x00);
        packet[0] = 4;

        PacketData inputData = new PacketData(packet, len);
        PacketData outputData = sendPacket(RM3_CMD_CHK_DATA, inputData);
        if(outputData == null || outputData.getLength() == 0 || outputData.getLength() <= UDP_PACKAGE_SIZE) {
            Log.e(TAG, "Fail to send check data command, try again ...");
            return null;
        }

        int err =  outputData.getBuffer()[0x22] | (outputData.getBuffer()[23] << 8);
        if(err != 0) {
            Log.e(TAG, "Fail to receive check data response");
            return null;
        }

        //printf("res_len = %");
        int len_decode = outputData.getLength() - UDP_PACKAGE_SIZE;//0x38

        int[] buff = new int[len_decode];
        for(int i = 0; i < len_decode; i++) {
            buff[i] = outputData.getBuffer()[0x38 + i];
        }
        PacketData decodePacket = new PacketData(buff, len_decode);
        outputData = decrypt(outputData);

        if(outputData == null || outputData.getLength() == 0) {
            Log.e(TAG, "Fail to decrypt check data response");
            return null;
        }

        Log.d(TAG,"===IR Code Return, save if needed====\n");
        //printf("{");

        for(int i = 4; i < len_decode; i++) {
            Log.d(TAG, String.format("0x%.02x ", outputData.getBuffer()[i]));
        }
        //printf("0x%.02x}\n", mInternalBuf[len_decode]);
        Log.d(TAG, "=====================\n");

        if(outputData.getBuffer()[4] != 0x26 && outputData.getBuffer()[5] != 0x00) {
            //invalid IR code
            return null;
        }
        byte[] irBytes = Arrays.copyOf(outputData.getBuffer(), 4);
        IrCodeData code = new IrCodeData(irBytes, outputData.getLength()-4);

        return code;
    };

    private boolean sendIRCode(final IrCodeData irCode){
        if(irCode == null || irCode.getIrCodeLen() == 0)
        {
            Log.e(TAG, "RM3 doesn't have IR code. Please teach for it");
            return false;
        }
        int len = irCode.getIrCodeLen() + 4;
        int[] packet = new int[len];
        Arrays.fill(packet, 0x00);
        packet[0] = 0x02;
        packet[1] = 0x00;
        packet[2] = 0x00;
        packet[3] = 0x00;
        Log.d(TAG, "sendIRCode");
        for(int i = 4;i < len;i++) {
            packet[i] = irCode.getIrCodeData()[i - 4];
        }

        PacketData inputData = new PacketData(packet, len);
        PacketData outputData = sendPacket(RM3_CMD_SEND_DATA, inputData);
        if(outputData == null || outputData.getLength() == 0 || outputData.getLength() <= UDP_PACKAGE_SIZE) {
            Log.e(TAG, "sendIRCode: Failed to send IR code");
            Log.d(TAG, "sendIRCode: retry authorize");
            if(true == authorize()) {
                return sendIRCode(irCode);
            }
            return false;
        }
        Log.d(TAG, "sendIRCode: Succeed to send IR code");
        return true;
    }

    public Rm3IrCodeData getIrCode() {
        return mIrCode;
    }

    public void setIrCode(Rm3IrCodeData mIrCode) {
        this.mIrCode = mIrCode;
    }
}
