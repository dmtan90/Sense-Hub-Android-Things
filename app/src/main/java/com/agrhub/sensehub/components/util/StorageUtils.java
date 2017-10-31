package com.agrhub.sensehub.components.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;

/**
 * Created by tanca on 10/30/2017.
 */

public class StorageUtils {
    public static Rm3IrCodeData loadRm3IrCodeData(Context context){
        Rm3IrCodeData mIrCode = new Rm3IrCodeData();
        Activity activity = (Activity) context;
        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        String mCmdPwrOn = sharedPref.getString("AIR_CONDITIONER_CMD_PWR_ON", "");
        String mCmdPwrOff = sharedPref.getString("AIR_CONDITIONER_CMD_PWR_OFF", "");
        String mCmdTemp16 = sharedPref.getString("AIR_CONDITIONER_CMD_TMP_16", "");
        String mCmdTemp18 = sharedPref.getString("AIR_CONDITIONER_CMD_TMP_18", "");
        String mCmdTemp20 = sharedPref.getString("AIR_CONDITIONER_CMD_TMP_20", "");
        String mCmdTemp22 = sharedPref.getString("AIR_CONDITIONER_CMD_TMP_22", "");
        String mCmdTemp24 = sharedPref.getString("AIR_CONDITIONER_CMD_TMP_24", "");
        String mCmdTemp26 = sharedPref.getString("AIR_CONDITIONER_CMD_TMP_26", "");
        String mCmdTemp28 = sharedPref.getString("AIR_CONDITIONER_CMD_TMP_28", "");
        String mCmdTemp30 = sharedPref.getString("AIR_CONDITIONER_CMD_TMP_30", "");

        if(!mCmdPwrOn.equals("")){
            byte[] array = Base64.decode(mCmdPwrOn, Base64.NO_WRAP);
            IrCodeData code = new IrCodeData(array, array.length);
            mIrCode.setIrPwrOn(code);
        }

        if(!mCmdPwrOff.equals("")){
            byte[] array = Base64.decode(mCmdPwrOff, Base64.NO_WRAP);
            IrCodeData code = new IrCodeData(array, array.length);
            mIrCode.setIrPwrOff(code);
        }

        if(!mCmdTemp16.equals("")){
            byte[] array = Base64.decode(mCmdTemp16, Base64.NO_WRAP);
            IrCodeData code = new IrCodeData(array, array.length);
            mIrCode.setIrTemp16(code);
        }

        if(!mCmdTemp18.equals("")){
            byte[] array = Base64.decode(mCmdTemp18, Base64.NO_WRAP);
            IrCodeData code = new IrCodeData(array, array.length);
            mIrCode.setIrTemp18(code);
        }

        if(!mCmdTemp20.equals("")){
            byte[] array = Base64.decode(mCmdTemp20, Base64.NO_WRAP);
            IrCodeData code = new IrCodeData(array, array.length);
            mIrCode.setIrTemp20(code);
        }

        if(!mCmdTemp22.equals("")){
            byte[] array = Base64.decode(mCmdTemp22, Base64.NO_WRAP);
            IrCodeData code = new IrCodeData(array, array.length);
            mIrCode.setIrTemp22(code);
        }

        if(!mCmdTemp24.equals("")){
            byte[] array = Base64.decode(mCmdTemp24, Base64.NO_WRAP);
            IrCodeData code = new IrCodeData(array, array.length);
            mIrCode.setIrTemp24(code);
        }

        if(!mCmdTemp26.equals("")){
            byte[] array = Base64.decode(mCmdTemp26, Base64.NO_WRAP);
            IrCodeData code = new IrCodeData(array, array.length);
            mIrCode.setIrTemp26(code);
        }

        if(!mCmdTemp28.equals("")){
            byte[] array = Base64.decode(mCmdTemp28, Base64.NO_WRAP);
            IrCodeData code = new IrCodeData(array, array.length);
            mIrCode.setIrTemp28(code);
        }

        if(!mCmdTemp30.equals("")){
            byte[] array = Base64.decode(mCmdTemp30, Base64.NO_WRAP);
            IrCodeData code = new IrCodeData(array, array.length);
            mIrCode.setIrTemp30(code);
        }
        return mIrCode;
    }

    public static boolean storeIrCodeData(Context context, Rm3IrCodeData mIrCode){
        Activity activity = (Activity) context;
        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        String mCmdPwrOn = Base64.encodeToString(PacketData.ConvertToByteArray(mIrCode.getIrPwrOn().getIrCodeData()),
                Base64.NO_WRAP);
        String mCmdPwrOff = Base64.encodeToString(PacketData.ConvertToByteArray(mIrCode.getIrPwrOff().getIrCodeData()),
                Base64.NO_WRAP);
        String mCmdTemp16 = Base64.encodeToString(PacketData.ConvertToByteArray(mIrCode.getIrTemp16().getIrCodeData()),
                Base64.NO_WRAP);
        String mCmdTemp18 = Base64.encodeToString(PacketData.ConvertToByteArray(mIrCode.getIrTemp18().getIrCodeData()),
                Base64.NO_WRAP);
        String mCmdTemp20 = Base64.encodeToString(PacketData.ConvertToByteArray(mIrCode.getIrTemp20().getIrCodeData()),
                Base64.NO_WRAP);
        String mCmdTemp22 = Base64.encodeToString(PacketData.ConvertToByteArray(mIrCode.getIrTemp22().getIrCodeData()),
                Base64.NO_WRAP);
        String mCmdTemp24 = Base64.encodeToString(PacketData.ConvertToByteArray(mIrCode.getIrTemp24().getIrCodeData()),
                Base64.NO_WRAP);
        String mCmdTemp26 = Base64.encodeToString(PacketData.ConvertToByteArray(mIrCode.getIrTemp26().getIrCodeData()),
                Base64.NO_WRAP);
        String mCmdTemp28 = Base64.encodeToString(PacketData.ConvertToByteArray(mIrCode.getIrTemp28().getIrCodeData()),
                Base64.NO_WRAP);
        String mCmdTemp30 = Base64.encodeToString(PacketData.ConvertToByteArray(mIrCode.getIrTemp30().getIrCodeData()),
                Base64.NO_WRAP);

        editor.putString("AIR_CONDITIONER_CMD_PWR_ON", mCmdPwrOn);
        editor.putString("AIR_CONDITIONER_CMD_PWR_OFF", mCmdPwrOff);
        editor.putString("AIR_CONDITIONER_CMD_TMP_16", mCmdTemp16);
        editor.putString("AIR_CONDITIONER_CMD_TMP_18", mCmdTemp18);
        editor.putString("AIR_CONDITIONER_CMD_TMP_20", mCmdTemp20);
        editor.putString("AIR_CONDITIONER_CMD_TMP_22", mCmdTemp22);
        editor.putString("AIR_CONDITIONER_CMD_TMP_24", mCmdTemp24);
        editor.putString("AIR_CONDITIONER_CMD_TMP_26", mCmdTemp26);
        editor.putString("AIR_CONDITIONER_CMD_TMP_28", mCmdTemp28);
        editor.putString("AIR_CONDITIONER_CMD_TMP_30", mCmdTemp30);

        return editor.commit();
    }
}
