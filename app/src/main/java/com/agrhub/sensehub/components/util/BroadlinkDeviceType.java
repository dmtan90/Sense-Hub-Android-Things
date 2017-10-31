package com.agrhub.sensehub.components.util;

/**
 * Created by tanca on 10/28/2017.
 */

public enum BroadlinkDeviceType {
    UNKNOWN_DEVICE_TYPE(-1),
    SP1_DEVICE_TYPE(0),
    SP2_DEVICE_TYPE(0x2711),
    HONEYWELL_SP2_2719_DEVICE_TYPE(0x2719),
    HONEYWELL_SP2_7919_DEVICE_TYPE(0x7919),
    HONEYWELL_SP2_791A_DEVICE_TYPE(0x791a),
    SPMINI_DEVICE_TYPE(0x2720),
    SPMINI2_DEVICE_TYPE(0x2728),
    SP3_DEVICE_TYPE(0x753e),
    OEM_SPMINI_2733_DEVICE_TYPE(0x2733),
    OEM_SPMINI_273E_DEVICE_TYPE(0x273e),
    OEM_SPMINI2_7530_DEVICE_TYPE(0x7530),
    OEM_SPMINI2_7918_DEVICE_TYPE(0x7918),
    SPMINI_PLUS_DEVICE_TYPE(0x2736),
    RM2_DEVICE_TYPE(0x2712),
    RMMINI_DEVICE_TYPE(0x2737),
    RMPRO_PHICOMM_DEVICE_TYPE(0x273d),
    RM2_HOME_PLUS_PHICOMM_DEVICE_TYPE(0x2783),
    RM2_HOME_PLUS_GDT_DEVICE_TYPE(0x277c),
    RM2_PRO_PLUS_DEVICE_TYPE(0x272a),
    RM2_PRO_PLUS2_DEVICE_TYPE(0x2787),
    RM2_PRO_PLUS_BL_DEVICE_TYPE(0x278b),
    RMMINI_SHATE_BL_DEVICE_TYPE(0x278f),
    A1_DEVICE_TYPE(0x2714),
    MP1_DEVICE_TYPE(0x4EB5);

    private int mValue;
    private BroadlinkDeviceType(int value){
        mValue = value;
    }

    public int getValue(){
        return mValue;
    }

    public static BroadlinkDeviceType getDevice(int value){
        BroadlinkDeviceType dev = UNKNOWN_DEVICE_TYPE;
        for(BroadlinkDeviceType el : BroadlinkDeviceType.values()){
            if(value == el.getValue()){
                dev = el;
                break;
            }
        }

        return dev;
    };
}
