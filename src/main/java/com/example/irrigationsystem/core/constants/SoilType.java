package com.example.irrigationsystem.core.constants;

public enum SoilType {
    CLAY(1), SANDY(2), SILTY(3), PEATY(4), CHALKY(5), LOAMY(6);

    private final int type;
    SoilType(int type) {
        this.type = type;
    }
}
