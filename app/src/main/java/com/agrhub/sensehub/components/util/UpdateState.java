package com.agrhub.sensehub.components.util;

/**
 * Created by tanca on 10/18/2017.
 */

public enum UpdateState {
    SW_UPDATE_MODE_DEVELOPING(0),
    SW_UPDATE_MODE_ALPHA(1),
    SW_UPDATE_MODE_BETA(2),
    SW_UPDATE_MODE_STABLE(3);

    private final int value;
    private UpdateState(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
