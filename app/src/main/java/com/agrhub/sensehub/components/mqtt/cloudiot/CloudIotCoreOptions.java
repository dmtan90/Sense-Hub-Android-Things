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
import android.text.TextUtils;

import com.agrhub.sensehub.components.util.ConfigHelper;

import java.util.Locale;

import javax.annotation.Nonnull;

/**
 * Configuration container for the MQTT example
 */
public class CloudIotCoreOptions {
    private static final String TAG = CloudIotCoreOptions.class.getSimpleName();

    private static final String DEFAULT_BRIDGE_HOSTNAME = "mqtt.googleapis.com";
    private static final short DEFAULT_BRIDGE_PORT = 443;

    public static final String UNUSED_ACCOUNT_NAME = "unused";

    /**
     * Notice that for CloudIoT the topic for telemetry events needs to have the format below.
     * As described <a href="https://cloud.google.com/iot/docs/protocol_bridge_guide#telemetry_events">in docs</a>,
     * messages published to a topic with this format are augmented with extra attributes and
     * forwarded to the Pub/Sub topic specified in the registry resource.
     */
    private static final String MQTT_TOPIC_FORMAT = "/devices/%s/events";
    private static final String MQTT_CLIENT_ID_FORMAT =
            "projects/%s/locations/%s/registries/%s/devices/%s";
    private static final String BROKER_URL_FORMAT = "ssl://%s:%d";

    /**
     * GCP cloud project name.
     */
    private String projectId;

    /**
     * Cloud IoT registry id.
     */
    private String registryId;

    /**
     * Cloud IoT device id.
     */
    private String deviceId;

    /**
     * GCP cloud region.
     */
    private String cloudRegion;

    /**
     * MQTT bridge hostname.
     */
    private String bridgeHostname = DEFAULT_BRIDGE_HOSTNAME;

    /**
     * MQTT bridge port.
     */
    private short bridgePort = DEFAULT_BRIDGE_PORT;

    public String getBrokerUrl() {
        return String.format(Locale.getDefault(), BROKER_URL_FORMAT, bridgeHostname, bridgePort);
    }

    public String getClientId() {
        return String.format(Locale.getDefault(), MQTT_CLIENT_ID_FORMAT,
                projectId, cloudRegion, registryId, deviceId);
    }

    public String getTopicName() {
        return String.format(Locale.getDefault(), MQTT_TOPIC_FORMAT, deviceId);
    }

    public String getProjectId() {
        return projectId;
    }

    public String getRegistryId() {
        return registryId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public String getCloudRegion() {
        return cloudRegion;
    }

    public String getBridgeHostname() {
        return bridgeHostname;
    }

    public short getBridgePort() {
        return bridgePort;
    }

    private CloudIotCoreOptions() {
    }

    public boolean isValid() {
        return !TextUtils.isEmpty(projectId) &&
                !TextUtils.isEmpty(registryId) &&
                !TextUtils.isEmpty(deviceId) &&
                !TextUtils.isEmpty(cloudRegion) &&
                !TextUtils.isEmpty(bridgeHostname);
    }

    /**
     * Construct a CloudIotCoreOptions object from SharedPreferences.
     */
    public static CloudIotCoreOptions fromConfigFile(@Nonnull Context context) {
        try {
            String fileName = "configs/config_iot_core.properties";
            CloudIotCoreOptions options = new CloudIotCoreOptions();
            options.projectId = ConfigHelper.getConfigValue(context, fileName,"PROJECT_ID");
            options.registryId = ConfigHelper.getConfigValue(context, fileName,"REGISTRY_ID");
            options.deviceId = ConfigHelper.getConfigValue(context, fileName, "DEVICE_ID");
            options.cloudRegion = ConfigHelper.getConfigValue(context, fileName, "CLOUD_REGION");
            options.bridgeHostname = ConfigHelper.getConfigValue(context, fileName, "MQTT_HOSTNAME");
            if(options.bridgeHostname == null){
                options.bridgeHostname = DEFAULT_BRIDGE_HOSTNAME;
            }
            try{
                options.bridgePort = Short.valueOf(ConfigHelper.getConfigValue(context, fileName, "MQTT_PORT"));
            }catch (Exception e){
                options.bridgePort = DEFAULT_BRIDGE_PORT;
            }
            return options;
        } catch (Exception e) {
            throw new IllegalArgumentException("While processing configuration options", e);
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof CloudIotCoreOptions)) {
            return false;
        }
        CloudIotCoreOptions o = (CloudIotCoreOptions) obj;
        return TextUtils.equals(projectId , o.projectId)
                && TextUtils.equals(registryId, o.registryId)
                && TextUtils.equals(deviceId, o.deviceId)
                && TextUtils.equals(cloudRegion, o.cloudRegion)
                && TextUtils.equals(bridgeHostname, o.bridgeHostname)
                && o.bridgePort == bridgePort;
    }
}
