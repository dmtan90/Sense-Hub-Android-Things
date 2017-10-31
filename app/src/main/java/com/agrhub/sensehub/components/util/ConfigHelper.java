package com.agrhub.sensehub.components.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by tanca on 10/24/2017.
 */

public class ConfigHelper {
    private static final String TAG = "Helper";

    /*
    * open config file: assets/configs/config_mqtt.properties and add some values:

        api_url=http://url.to.api/v1/
        api_key=123456
    * */
    public static String getConfigValue(Context context, String fileName, String name) {
        try {
            InputStream rawResource = context.getAssets().open(fileName);
            Properties properties = new Properties();
            properties.load(rawResource);
            return properties.getProperty(name);
        } catch (Resources.NotFoundException e) {
            Log.e(TAG, "Unable to find the config file: " + e.getMessage());
        } catch (IOException e) {
            Log.e(TAG, "Failed to open config file.");
        }

        return null;
    }

    /*
    * In your AndroidManifest.xml add something like:
        ...
        <application ...>
            ...

            <meta-data android:name="api_url" android:value="http://url.to.api/v1/"/>
            <meta-data android:name="api_key" android:value="123456"/>
        </application>
    * */
    public static String getMetaData(Context context, String name) {
        try {
            ApplicationInfo ai = context.getPackageManager().getApplicationInfo(context.getPackageName(),
                    PackageManager.GET_META_DATA);
            Bundle bundle = ai.metaData;
            return bundle.getString(name);
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, "Unable to load meta-data: " + e.getMessage());
        }
        return null;
    }
}
