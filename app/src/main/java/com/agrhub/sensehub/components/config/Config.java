package com.agrhub.sensehub.components.config;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.agrhub.sensehub.components.util.UpdateState;

/**
 * Created by tanca on 10/19/2017.
 */

public enum Config {
    instance;
    private Context mContext = null;
    private String mStaSSID = "";
    private String mStaPwd = "";
    private String mApSSID = "";
    private String mApPwd = "";
    private UpdateState mUpdateState = UpdateState.SW_UPDATE_MODE_STABLE;

    public void init(Context ctx){
        mContext = ctx;
        load();
    }

    private void load(){
        Activity activity = (Activity) mContext;
        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        mApSSID = sharedPref.getString("ap_ssid", "SENSE HUB");
        mApPwd = sharedPref.getString("ap_pwd", "12345678");
        mStaSSID = sharedPref.getString("sta_ssid", "");
        mStaPwd = sharedPref.getString("sta_pwd", "");
        int updateState = sharedPref.getInt("update_state", 0);

        if(updateState == 0){
            mUpdateState = UpdateState.SW_UPDATE_MODE_DEVELOPING;
        }
        else if(updateState == 1){
            mUpdateState = UpdateState.SW_UPDATE_MODE_ALPHA;
        }
        else if(updateState == 2){
            mUpdateState = UpdateState.SW_UPDATE_MODE_BETA;
        }
        else{
            mUpdateState = UpdateState.SW_UPDATE_MODE_STABLE;
        }
    }

    public boolean store(){
        Boolean rs = false;
        Activity activity = (Activity) mContext;
        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("ap_ssid", mApSSID);
        editor.putString("ap_pwd", mApPwd);
        editor.putString("sta_ssid", mStaSSID);
        editor.putString("sta_pwd", mStaPwd);
        editor.putInt("update_state", mUpdateState.getValue());
        rs = editor.commit();
        return rs;
    }

    public String getStaSSID() {
        return mStaSSID;
    }

    public void setStaSSID(String mStaSSID) {
        this.mStaSSID = mStaSSID;
    }

    public String getStaPwd() {
        return mStaPwd;
    }

    public void setStaPwd(String mStaPwd) {
        this.mStaPwd = mStaPwd;
    }

    public String getApSSID() {
        return mApSSID;
    }

    public void setApSSID(String mApSSID) {
        this.mApSSID = mApSSID;
    }

    public String getApPwd() {
        return mApPwd;
    }

    public void setApPwd(String mApPwd) {
        this.mApPwd = mApPwd;
    }

    public UpdateState getUpdateState() {
        return mUpdateState;
    }

    public void setUpdateState(UpdateState mUpdateState) {
        this.mUpdateState = mUpdateState;
    }
}
