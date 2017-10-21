package com.agrhub.sensehub.components.entity;

import com.agrhub.sensehub.components.util.DeviceName;
import com.agrhub.sensehub.components.util.DeviceState;
import com.agrhub.sensehub.components.util.SensorType;

/**
 * Created by tanca on 10/19/2017.
 */

public class TitagSensorEntity extends AxaetSensorEntity {
    private int mLight;

    public TitagSensorEntity(){
        super();
        setDeviceName(DeviceName.DB_DEVICE_NAME_TI_TAG_SENSOR);
        setDeviceState(DeviceState.DEVICE_DISCONNECTED);
        this.mLight = -1;
    }

    public int getLight() {
        return mLight;
    }

    public void setLight(int mLight) {
        this.mLight = mLight;
    }

    @Override
    public String getData(){
        return String.format("[{\"sensor_type\":%d,\"sensor_value\":%d}," +
                        "{\"sensor_type\":%d,\"sensor_value\":%d}," +
                        "{\"sensor_type\":%d,\"sensor_value\":%d}," +
                        "{\"sensor_type\":%d,\"sensor_value\":%d}]",
                SensorType.SENSOR_TYPE_LIGHT.getValue(), getLight(),
                SensorType.SENSOR_TYPE_AIR_TEMP.getValue(), getAirTemperature(),
                SensorType.SENSOR_TYPE_AIR_HUMIDITY.getValue(), getAirHumidity(),
                SensorType.SENSOR_TYPE_BAT.getValue(), getBattery()
        );
    }
}
