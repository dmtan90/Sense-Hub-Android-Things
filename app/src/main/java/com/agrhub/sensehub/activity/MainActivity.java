package com.agrhub.sensehub.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.agrhub.sensehub.components.ble.BLEManager;
import com.agrhub.sensehub.components.config.Config;
import com.agrhub.sensehub.components.devicemanager.DeviceManager;
import com.agrhub.sensehub.components.webserver.WebService;
import com.agrhub.sensehub.components.wifi.WifiManager;

/**
 * Skeleton of an Android Things activity.
 * <p>
 * Android Things peripheral APIs are accessible through the class
 * PeripheralManagerService. For example, the snippet below will open a GPIO pin and
 * set it to HIGH:
 * <p>
 * <pre>{@code
 * PeripheralManagerService service = new PeripheralManagerService();
 * mLedGpio = service.openGpio("BCM6");
 * mLedGpio.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW);
 * mLedGpio.setValue(true);
 * }</pre>
 * <p>
 * For more complex peripherals, look for an existing user-space driver, or implement one if none
 * is available.
 *
 * @see <a href="https://github.com/androidthings/contrib-drivers#readme">https://github.com/androidthings/contrib-drivers#readme</a>
 */
public class MainActivity extends Activity {
    private WebService mServer = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(getClass().getSimpleName(), "Start MainActivity");
        Config.instance.init(this);
        DeviceManager.instance.setContext(this);
        WifiManager.instance.init(this);
        BLEManager.instance.init(this);
        try {
            mServer = new WebService(this);
        }catch (Exception e){
            Log.e(getClass().getSimpleName(), e.toString());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mServer != null) {
            mServer.stop();
        }
    }
}
