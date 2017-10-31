package com.agrhub.sensehub.components.mqtt.cloudiot;

import android.content.Context;
import android.util.Log;

import com.agrhub.sensehub.components.mqtt.MessagePayload;

import org.apache.commons.io.IOUtils;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.annotation.Nonnull;

/**
 * Created by tanca on 10/25/2017.
 */

public class MqttPublisher implements CloudPublisher, MqttCallback {
    private static final String TAG = MqttPublisher.class.getSimpleName();

    // Indicate if this message should be a MQTT 'retained' message.
    public static final boolean SHOULD_RETAIN = false;

    // Use mqttQos=1 (at least once delivery), mqttQos=0 (at most once delivery) also supported.
    public static final int MQTT_QOS = 1;
    public String mSubTopic;

    public Context mContext;
    public MqttClient mqttClient;
    public MqttOptions mMqttOptions;
    public AtomicBoolean mReady = new AtomicBoolean(false);

    public MqttPublisher(){

    }

    public MqttPublisher(@Nonnull MqttOptions options, @Nonnull Context ctx){
        mContext = ctx;
        mMqttOptions = options;
        try{
            initializeMqttClient();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void publish(String data) {
        Log.i(TAG, "publish data=" + data);
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
                sendMessage(mMqttOptions.getPublishTopicName(), payload.getBytes());
            }
        } catch (MqttException e) {
            throw new IllegalArgumentException("Could not send message", e);
        }
    }

    @Override
    public boolean isReady() {
        return mReady.get();
    }

    @Override
    public void close() throws MqttException {
        mMqttOptions = null;
        if (mqttClient != null) {
            if (mqttClient.isConnected()) {
                mqttClient.disconnect();
            }
            mqttClient = null;
        }
    }

    private void initializeMqttClient()
            throws MqttException, IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        Log.d(TAG, "initializeMqttClient broker=" + mMqttOptions.getBrokerUrl() +
                " clientID=" + mMqttOptions.getClientId() +
                " username=" + mMqttOptions.getUsername() +
                " password=" + mMqttOptions.getPassword());
        mqttClient = new MqttClient(mMqttOptions.getBrokerUrl(),
                mMqttOptions.getClientId(), new MemoryPersistence());

        MqttConnectOptions options = new MqttConnectOptions();
        // Note that the the Google Cloud IoT only supports MQTT 3.1.1, and Paho requires that we
        // explicitly set this. If you don't set MQTT version, the server will immediately close its
        // connection to your device.
        options.setMqttVersion(MqttConnectOptions.MQTT_VERSION_3_1_1);
        options.setUserName(mMqttOptions.getUsername());
        options.setPassword(mMqttOptions.getPassword().toCharArray());

        options.setAutomaticReconnect(true);

        mqttClient.setCallback(this);
        mqttClient.connect(options);

        if(mqttClient.isConnected()) {
            try{
                Log.i(TAG, "initializeMqttClient subscribe topic=" + mMqttOptions.getSubscribeTopicName());
                mqttClient.subscribe(mMqttOptions.getSubscribeTopicName(), 1);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        else{
            Log.e(TAG, "Can't connect to " + mMqttOptions.getBrokerUrl());
        }
        mReady.set(true);
    }

    public void sendMessage(String mqttTopic, byte[] mqttMessage) throws MqttException {
        mqttClient.publish(mqttTopic, mqttMessage, MQTT_QOS, SHOULD_RETAIN);
    }

    @Override
    public void connectionLost(Throwable cause) {
        Log.e(TAG, "connectionLost");
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        Log.i(TAG, "messageArrived message=" + IOUtils.toString(message.getPayload(), "UTF-8"));
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        Log.i(TAG, "deliveryComplete");
    }
}
