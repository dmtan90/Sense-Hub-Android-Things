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

package com.agrhub.sensehub.components.mqtt.cloudiot;

import android.content.Context;
import android.util.Log;

import com.agrhub.sensehub.components.mqtt.MessagePayload;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.annotation.Nonnull;

/**
 * Handle publishing sensor data to a Cloud IoT MQTT endpoint.
 *
 */
public class MqttIoTPublisher extends MqttPublisher {

    private static final String TAG = MqttIoTPublisher.class.getSimpleName();
    private String mSubTopic;
    public CloudIotCoreOptions mMqttOptions;

    private MqttAuthentication mqttAuth;

    public MqttIoTPublisher(@Nonnull CloudIotCoreOptions options, @Nonnull Context ctx) {
        mContext = ctx;
        mMqttOptions = options;
        initialize();
    }

    /**
     * Initialize a Cloud IoT Endpoint given a set of configuration options.
     *
     *
     */
    private void initialize() {
        if (!mMqttOptions.isValid()) {
            Log.w(TAG, "Postponing initialization, since CloudIotCoreOptions is incomplete. " +
                "Please configure via intent, for example: \n" +
                "adb shell am startservice -a " +
                "com.example.androidthings.sensorhub.mqtt.CONFIGURE " +
                "-e project_id <PROJECT_ID> -e cloud_region <REGION> " +
                "-e registry_id <REGISTRY_ID> -e device_id <DEVICE_ID> " +
                "com.example.androidthings.sensorhub/.cloud.CloudPublisherService\n");
            return;
        }
        try {
            Log.i(TAG, "Device Configuration:");
            Log.i(TAG, " Project ID: " + mMqttOptions.getProjectId());
            Log.i(TAG, "  Region ID: " + mMqttOptions.getCloudRegion());
            Log.i(TAG, "Registry ID: " + mMqttOptions.getRegistryId());
            Log.i(TAG, "  Device ID: " + mMqttOptions.getDeviceId());
            Log.i(TAG, "MQTT Configuration:");
            Log.i(TAG, "Broker: " + mMqttOptions.getBridgeHostname() + ":" + mMqttOptions.getBridgePort());
            Log.i(TAG, "Publishing to topic: " + mMqttOptions.getTopicName());
            mqttAuth = new MqttAuthentication(mContext);
            mqttAuth.initialize();
            initializeMqttClient();
        } catch (MqttException | IOException | GeneralSecurityException e) {
            throw new IllegalArgumentException("Could not initialize MQTT : " + e.getMessage(), e);
        }
    }

    @Override
    public void publish(String data) {
        try {
            if (isReady()) {
                if (mqttClient != null && !mqttClient.isConnected()) {
                    // if for some reason the mqtt client has disconnected, we should try to connect
                    // it again.
                    try {
                        initializeMqttClient();
                    } catch (MqttException | IOException | GeneralSecurityException e) {
                        throw new IllegalArgumentException("Could not initialize MQTT", e);
                    }
                }
                String payload = MessagePayload.createMessagePayload(data);
                Log.d(TAG, "Publishing: " + payload);
                super.sendMessage(mMqttOptions.getTopicName(), payload.getBytes());
            }
        } catch (MqttException e) {
            throw new IllegalArgumentException("Could not send message", e);
        }
    }

    private void initializeMqttClient()
        throws MqttException, IOException, NoSuchAlgorithmException, InvalidKeySpecException {

        mqttClient = new MqttClient(mMqttOptions.getBrokerUrl(),
                mMqttOptions.getClientId(), new MemoryPersistence());

        MqttConnectOptions options = new MqttConnectOptions();
        // Note that the the Google Cloud IoT only supports MQTT 3.1.1, and Paho requires that we
        // explicitly set this. If you don't set MQTT version, the server will immediately close its
        // connection to your device.
        options.setMqttVersion(MqttConnectOptions.MQTT_VERSION_3_1_1);
        options.setUserName(CloudIotCoreOptions.UNUSED_ACCOUNT_NAME);
        options.setAutomaticReconnect(true);

        // generate the jwt password
        options.setPassword(mqttAuth.createJwt(mMqttOptions.getProjectId()));

        mqttClient.setCallback(this);
        mqttClient.connect(options);

        if(mqttClient.isConnected()) {
            try{
                mSubTopic = "/devices/sense_hub_2.0_android_things/config";// + NetworkUtils.getMACAddress(mContext);
                Log.i(TAG, "initializeMqttClient subscribe topic=" + mSubTopic);
                mqttClient.subscribe(mSubTopic, 1);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        mReady.set(true);
    }
}
