package com.agrhub.sensehub.components.util;

/**
 * Created by tanca on 10/18/2017.
 */

public enum ControllerState {
    CONTROLLER_STATE_ON(0),
    CONTROLLER_STATE_OFF(1),
    CONTROLLER_STATE_UNKNOWN(2);

    private final int value;
    private ControllerState(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
