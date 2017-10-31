package com.agrhub.sensehub.components.mqtt.cloudiot;

import android.content.Context;
import android.text.TextUtils;

import com.agrhub.sensehub.components.util.ConfigHelper;
import com.agrhub.sensehub.components.util.NetworkUtils;

import java.util.Locale;

import javax.annotation.Nonnull;

/**
 * Created by tanca on 10/26/2017.
 */

public class MqttOptions {
    private static final String TAG = CloudIotCoreOptions.class.getSimpleName();

    private static final String DEFAULT_BRIDGE_HOSTNAME = "mqtt.google.com";
    private static final short DEFAULT_BRIDGE_PORT = 1883;

    public static final String UNUSED_ACCOUNT_NAME = "unused";

    private static final String MQTT_PUBLISH_TOPIC_FORMAT = "gateway/%s";
    private static final String MQTT_SUBSCRIBE_TOPIC_FORMAT = "to-gateway/%s";
    private static final String MQTT_CLIENT_ID_FORMAT = "ID_%s_%d";
    private static final String BROKER_URL_FORMAT = "tcp://%s:%d";

    /**
     * Cloud IoT device id.
     */
    private String deviceId;

    /**
     * MQTT bridge hostname.
     */
    private String bridgeHostname = DEFAULT_BRIDGE_HOSTNAME;

    /**
     * MQTT bridge port.
     */
    private short bridgePort = DEFAULT_BRIDGE_PORT;

    /**
     * MQTT username
     */
    private String username = "";

    /**
     * MQTT password
     */
    private String password = "";

    public String getBrokerUrl() {
        return String.format(Locale.getDefault(), BROKER_URL_FORMAT, bridgeHostname, bridgePort);
    }

    public String getClientId() {
        return String.format(Locale.getDefault(), MQTT_CLIENT_ID_FORMAT,
                deviceId, System.currentTimeMillis());
    }

    public String getPublishTopicName() {
        return String.format(Locale.getDefault(), MQTT_PUBLISH_TOPIC_FORMAT, deviceId);
    }

    public String getSubscribeTopicName() {
        return String.format(Locale.getDefault(), MQTT_SUBSCRIBE_TOPIC_FORMAT, deviceId);
    }

    public String getBridgeHostname() {
        return bridgeHostname;
    }

    public short getBridgePort() {
        return bridgePort;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    private MqttOptions(Context ctx) {
        deviceId = NetworkUtils.getMACAddress(ctx);
    }

    public boolean isValid() {
        return !TextUtils.isEmpty(deviceId) &&
                !TextUtils.isEmpty(bridgeHostname);
    }

    /**
     * Construct a CloudIotCoreOptions object from SharedPreferences.
     */
    public static MqttOptions fromConfigFile(@Nonnull Context context) {
        try {
            String fileName = "configs/config_mqtt.properties";
            MqttOptions options = new MqttOptions(context);
            options.bridgeHostname = ConfigHelper.getConfigValue(context, fileName, "MQTT_HOSTNAME");
            if(options.bridgeHostname == null){
                options.bridgeHostname = DEFAULT_BRIDGE_HOSTNAME;
            }
            try{
                options.bridgePort = Short.valueOf(ConfigHelper.getConfigValue(context, fileName,"MQTT_PORT"));
            }catch (Exception e){
                options.bridgePort = DEFAULT_BRIDGE_PORT;
            }
            options.username = ConfigHelper.getConfigValue(context, fileName,"MQTT_USERNAME");
            if(options.username == null){
                options.username = "";
            }
            options.password = ConfigHelper.getConfigValue(context, fileName,"MQTT_PASSWORD");
            if(options.password == null){
                options.password = "";
            }
            return options;
        } catch (Exception e) {
            throw new IllegalArgumentException("While processing configuration options", e);
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof MqttOptions)) {
            return false;
        }
        MqttOptions o = (MqttOptions) obj;
        return TextUtils.equals(deviceId, o.deviceId)
                && TextUtils.equals(bridgeHostname, o.bridgeHostname)
                && o.bridgePort == bridgePort;
    }
}
