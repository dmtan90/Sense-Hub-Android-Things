/*
 * Copyright 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.agrhub.sensehub.components.mqtt;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import com.agrhub.sensehub.components.devicemanager.DeviceManager;
import com.agrhub.sensehub.components.mqtt.cloudiot.CloudIotCoreOptions;
import com.agrhub.sensehub.components.mqtt.cloudiot.CloudPublisher;
import com.agrhub.sensehub.components.mqtt.cloudiot.MqttIoTPublisher;
import com.agrhub.sensehub.components.mqtt.cloudiot.MqttOptions;
import com.agrhub.sensehub.components.mqtt.cloudiot.MqttPublisher;

import java.util.Locale;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.Nullable;

/**
 * Handle asynchronous cloud sensor logging requests via a Binder interface. Sensor events are
 * periodically published to the cloud via a {@link CloudPublisher}.
 * <p>
 */
public class CloudPublisherService extends Service {
    private static final String TAG = " CloudPublisherService";

    // Will store at most this amount of most recent sensor change events, per sensor type
    private static final int BUFFER_SIZE_FOR_ONCHANGE_SENSORS = 10;

    private static final long PUBLISH_INTERVAL_MS = TimeUnit.SECONDS.toMillis(60);

    // After this amount of tentatives, the publish interval will change from PUBLISH_INTERVAL_MS
    // to BACKOFF_INTERVAL_MS until a successful connection has been established.
    private static final long ERRORS_TO_INITIATE_BACKOFF = 20;
    private static final long BACKOFF_INTERVAL_MS = TimeUnit.MINUTES.toMillis(1);

    private Looper mServiceLooper;
    private Handler mServiceHandler;
    private CloudPublisher mPublisher;

    private AtomicInteger mUnsuccessfulTentatives = new AtomicInteger(0);

    private final ConcurrentHashMap<Long, String> mMostRecentData = new ConcurrentHashMap<>();
    /*private final ConcurrentHashMap<String, PriorityBlockingQueue<SensorData>> mOnChangeData =
            new ConcurrentHashMap<>();*/

    private final Runnable mSensorConsumerRunnable = new Runnable() {
        @Override
        public void run() {
            long delayForNextTentative = PUBLISH_INTERVAL_MS;
            try {
                initializeIfNeeded();
                processCollectedSensorData();
                mUnsuccessfulTentatives.set(0);
            } catch (Throwable t) {
                if (mUnsuccessfulTentatives.get() >= ERRORS_TO_INITIATE_BACKOFF) {
                    delayForNextTentative = BACKOFF_INTERVAL_MS;
                } else {
                    mUnsuccessfulTentatives.incrementAndGet();
                }
                Log.e(TAG, String.format(Locale.getDefault(),
                        "Cannot publish. %d unsuccessful tentatives, will try again in %d ms",
                        mUnsuccessfulTentatives.get(), delayForNextTentative), t);
            } finally {
                mServiceHandler.postDelayed(this, delayForNextTentative);
            }
        }
    };

    private void processCollectedSensorData() {
        if (mPublisher == null || !mPublisher.isReady()) {
            return;
        }
        String data = DeviceManager.instance.getDevicesJson();
        mMostRecentData.put(System.currentTimeMillis(), data);

        Log.i(TAG, "publishing " + data);
        mPublisher.publish(data);
    }

    // Support for service binding
    private final IBinder mBinder = new CloudPublisherService.LocalBinder();

    /**
     * Class used for the client Binder.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with IPC.
     */
    public class LocalBinder extends Binder {
        public CloudPublisherService getService() {
            // Return this instance of LocalService so clients can call public methods
            return CloudPublisherService.this;
        }
    }

    private CloudIotCoreOptions readIoTCoreOptions() {
        CloudIotCoreOptions options = CloudIotCoreOptions.fromConfigFile(this);
        return options;
    }

    private MqttOptions readMqttOptions() {
        MqttOptions options = MqttOptions.fromConfigFile(this);
        return options;
    }

    @Override
    public void onStart(Intent intent, int startId) {
        Log.d(TAG, "onStart");
        super.onStart(intent, startId);
    }

    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate");
        super.onCreate();
        initializeIfNeeded();
        HandlerThread thread = new HandlerThread("CloudPublisherService");
        thread.start();
        mServiceLooper = thread.getLooper();
        mServiceHandler = new Handler(mServiceLooper);
        mServiceHandler.postDelayed(mSensorConsumerRunnable, PUBLISH_INTERVAL_MS);
    }

    private void initializeIfNeeded() {
        if (mPublisher == null) {
            try {
                //For Google IoT Core
                //mPublisher = new MqttIoTPublisher(readIoTCoreOptions(), this);

                //For normal MQTT server
                mPublisher = new MqttPublisher(readMqttOptions(), this);
            } catch (Throwable t) {
                Log.e(TAG, "Could not create MqttIoTPublisher. Will try again later", t);
            }
        }
    }

    @Override
    @Nullable
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onDestroy() {
        mServiceLooper.quit();
        mServiceLooper = null;
    }

}
