package com.agrhub.sensehub.components.entity;

import com.agrhub.sensehub.components.util.ControllerData;
import com.agrhub.sensehub.components.util.ControllerState;
import com.agrhub.sensehub.components.util.ControllerType;
import com.agrhub.sensehub.components.util.DeviceName;
import com.agrhub.sensehub.components.util.DeviceState;
import com.agrhub.sensehub.components.util.EcFormulaData;
import com.agrhub.sensehub.components.util.LiquidLevelData;
import com.agrhub.sensehub.components.util.LiquidThresholdData;
import com.agrhub.sensehub.components.util.SensorType;

/**
 * Created by tanca on 10/19/2017.
 */

public class FiotTankEntity extends Entity{
    private float mPH;
    private LiquidThresholdData mPHThreshold;
    private float mEC;
    private LiquidThresholdData mECThreshold;
    private EcFormulaData mECFormula;
    private int mCO2;
    private int mWaterLevel;
    private LiquidThresholdData mWaterLevelThreshold;
    private ControllerData mValveInput;
    private ControllerData mValveOutput;
    private ControllerData mWashingMode;
    private ControllerData mCalibrateMode;
    private ControllerData mWaterPump;
    private ControllerData mOxygenPump;
    private int mWaterPumpFreq;
    private int mOxygenPumpFreq;
    private int mTankHeight;
    private int mSensorHeight;
    private int mWaterVolume;
    private int mLiquidVolume;
    private LiquidLevelData mLiquidLevel;
    private Boolean mWaterLeak;
    private Boolean mError;

    public FiotTankEntity(){
        super();
        setDeviceName(DeviceName.DB_DEVICE_NAME_FIOT_SMART_TANK);
        setDeviceState(DeviceState.DEVICE_DISCONNECTED);

        this.mPH = -1;
        this.mPHThreshold.mMin = 0;
        this.mPHThreshold.mMax = 0;

        this.mEC = -1;
        this.mECThreshold.mMin = 0;
        this.mECThreshold.mMax = 0;

        this.mECFormula.mA = 0;
        this.mECFormula.mB = 0;
        this.mECFormula.mC = 0;

        this.mCO2 = -1;
        this.mWaterLevel = -1;
        this.mWaterLevelThreshold.mMin = 0;
        this.mWaterLevelThreshold.mMax = 0;

        this.mValveInput.mControllerType = ControllerType.DEVICE_CMD_VALVE_INPUT;
        this.mValveInput.mControllerState = ControllerState.CONTROLLER_STATE_OFF;

        this.mValveOutput.mControllerType = ControllerType.DEVICE_CMD_VALVE_OUTPUT;
        this.mValveOutput.mControllerState = ControllerState.CONTROLLER_STATE_OFF;

        this.mWashingMode.mControllerType = ControllerType.DEVICE_CMD_HYDRO_TANK_WASHING;
        this.mWashingMode.mControllerState = ControllerState.CONTROLLER_STATE_OFF;

        this.mCalibrateMode.mControllerType = ControllerType.DEVICE_CMD_HYDRO_TANK_CALIBRATE;
        this.mCalibrateMode.mControllerState = ControllerState.CONTROLLER_STATE_OFF;

        this.mWaterPump.mControllerType = ControllerType.DEVICE_CMD_PUMP;
        this.mWaterPump.mControllerState = ControllerState.CONTROLLER_STATE_OFF;

        this.mOxygenPump.mControllerType = ControllerType.DEVICE_CMD_OXYGEN_PUMP;
        this.mOxygenPump.mControllerState = ControllerState.CONTROLLER_STATE_OFF;

        this.mWaterPumpFreq = 3600;
        this.mOxygenPumpFreq = 3600;

        this.mTankHeight = 20;
        this.mSensorHeight = 10;

        this.mWaterVolume = 20;
        this.mLiquidVolume = 1000;

        this.mLiquidLevel.mA = 100;
        this.mLiquidLevel.mB = 100;
        this.mLiquidLevel.mC = 100;
        this.mLiquidLevel.mD = 100;

        this.mWaterLeak = false;
        this.mError = false;
    }

    public float getPH() {
        return mPH;
    }

    public void setPH(float mPH) {
        this.mPH = mPH;
    }

    public LiquidThresholdData getPHThreshold() {
        return mPHThreshold;
    }

    public void setPHThreshold(LiquidThresholdData mPHThreshold) {
        this.mPHThreshold = mPHThreshold;
    }

    public float getEC() {
        return mEC;
    }

    public void setEC(float mEC) {
        this.mEC = mEC;
    }

    public LiquidThresholdData getECThreshold() {
        return mECThreshold;
    }

    public void setECThreshold(LiquidThresholdData mECThreshold) {
        this.mECThreshold = mECThreshold;
    }

    public EcFormulaData getECFormula() {
        return mECFormula;
    }

    public void setECFormula(EcFormulaData mECFormula) {
        this.mECFormula = mECFormula;
    }

    public int getCO2() {
        return mCO2;
    }

    public void setCO2(int mCO2) {
        this.mCO2 = mCO2;
    }

    public int getWaterLevel() {
        return mWaterLevel;
    }

    public void setWaterLevel(int mWaterLevel) {
        this.mWaterLevel = mWaterLevel;
    }

    public LiquidThresholdData getWaterLevelThreshold() {
        return mWaterLevelThreshold;
    }

    public void setWaterLevelThreshold(LiquidThresholdData mWaterLevelThreshold) {
        this.mWaterLevelThreshold = mWaterLevelThreshold;
    }

    public ControllerData getValveInput() {
        return mValveInput;
    }

    public void setValveInput(ControllerData mValveInput) {
        this.mValveInput = mValveInput;
    }

    public ControllerData getValveOutput() {
        return mValveOutput;
    }

    public void setValveOutput(ControllerData mValveOutput) {
        this.mValveOutput = mValveOutput;
    }

    public ControllerData getWashingMode() {
        return mWashingMode;
    }

    public void setWashingMode(ControllerData mWashingMode) {
        this.mWashingMode = mWashingMode;
    }

    public ControllerData getCalibrateMode() {
        return mCalibrateMode;
    }

    public void setCalibrateMode(ControllerData mCalibrateMode) {
        this.mCalibrateMode = mCalibrateMode;
    }

    public ControllerData getWaterPump() {
        return mWaterPump;
    }

    public void setWaterPump(ControllerData mWaterPump) {
        this.mWaterPump = mWaterPump;
    }

    public ControllerData getOxygenPump() {
        return mOxygenPump;
    }

    public void setOxygenPump(ControllerData mOxygenPump) {
        this.mOxygenPump = mOxygenPump;
    }

    public int getWaterPumpFreq() {
        return mWaterPumpFreq;
    }

    public void setWaterPumpFreq(int mWaterPumpFreq) {
        this.mWaterPumpFreq = mWaterPumpFreq;
    }

    public int getOxygenPumpFreq() {
        return mOxygenPumpFreq;
    }

    public void setOxygenPumpFreq(int mOxygenPumpFreq) {
        this.mOxygenPumpFreq = mOxygenPumpFreq;
    }

    public int getTankHeight() {
        return mTankHeight;
    }

    public void setTankHeight(int mTankHeight) {
        this.mTankHeight = mTankHeight;
    }

    public int getSensorHeight() {
        return mSensorHeight;
    }

    public void setSensorHeight(int mSensorHeight) {
        this.mSensorHeight = mSensorHeight;
    }

    public int getWaterVolume() {
        return mWaterVolume;
    }

    public void setWaterVolume(int mWaterVolume) {
        this.mWaterVolume = mWaterVolume;
    }

    public int getLiquidVolume() {
        return mLiquidVolume;
    }

    public void setLiquidVolume(int mLiquidVolume) {
        this.mLiquidVolume = mLiquidVolume;
    }

    public LiquidLevelData getLiquidLevel() {
        return mLiquidLevel;
    }

    public void setLiquidLevel(LiquidLevelData mLiquidLevel) {
        this.mLiquidLevel = mLiquidLevel;
    }

    public Boolean getWaterLeak() {
        return mWaterLeak;
    }

    public void setWaterLeak(Boolean mWaterLeak) {
        this.mWaterLeak = mWaterLeak;
    }

    public Boolean getError() {
        return mError;
    }

    public void setError(Boolean mError) {
        this.mError = mError;
    }

    @Override
    public String getData() {
        return String.format("[" +
                "{\"sensor_type\":%d,\"sensor_value\":%.2f,\"sensor_threshold\":{\"sensor_value_min\":%.2f,\"sensor_value_max\":%.2f},\"sensor_liquid_level\":{\"d\":%d}}," +
                "{\"sensor_type\":%d,\"sensor_value\":%.2f,\"sensor_threshold\":{\"sensor_value_min\":%.2f,\"sensor_value_max\":%.2f},\"sensor_formula\":{\"a\":%d,\"b\":%d,\"c\":%d},\"sensor_liquid_level\":{\"a\":%d,\"b\":%d,\"c\":%d}}," +
                "{\"sensor_type\":%d,\"sensor_value\":%d}," +
                "{\"sensor_type\":%d,\"sensor_value\":%d,\"sensor_threshold\":{\"sensor_value_min\":%.0f,\"sensor_value_max\":%.0f}}," +
                "{\"sensor_type\":%d,\"sensor_value\":%d}," +
                "{\"sensor_type\":%d,\"sensor_value\":%d}," +
                "{\"controller_type\":%d,\"controller_is_on\":%s}," +
                "{\"controller_type\":%d,\"controller_is_on\":%s}," +
                "{\"controller_type\":%d,\"controller_is_on\":%s}," +
                "{\"controller_type\":%d,\"controller_is_on\":%s}," +
                "{\"controller_type\":%d,\"controller_is_on\":%s}," +
                "{\"controller_type\":%d,\"controller_is_on\":%s}]",
                SensorType.SENSOR_TYPE_WATER_PH.getValue(),
                getPH(), getPHThreshold().mMin, getPHThreshold().mMax, getLiquidLevel().mD,
                getEC(), getECThreshold().mMin, getECThreshold().mMax, getECFormula().mA, getECFormula().mB, getECFormula().mC,
                getECFormula().mA, getECFormula().mB, getECFormula().mC,
                SensorType.SENSOR_TYPE_CO2.getValue(), getCO2(),
                SensorType.SENSOR_TYPE_WATER_LEVEL.getValue(), getWaterLevel(), getWaterLevelThreshold().mMin, getWaterLevelThreshold().mMax,
                SensorType.SENSOR_TYPE_WATER_DETECT.getValue(), getWaterLeak(),
                SensorType.SENSOR_TYPE_ERROR_DETECT.getValue(), getError(),
                getValveInput().mControllerType.getValue(), getValveInput().mControllerState.getValueString(),
                getValveOutput().mControllerType.getValue(), getValveOutput().mControllerState.getValueString(),
                getWashingMode().mControllerType.getValue(), getWashingMode().mControllerState.getValueString(),
                getCalibrateMode().mControllerType.getValue(), getCalibrateMode().mControllerState.getValueString(),
                getWaterPump().mControllerType.getValue(), getWaterPump().mControllerState.getValueString(),
                getOxygenPump().mControllerType.getValue(), getOxygenPump().mControllerState.getValueString()
                );
    }
}
