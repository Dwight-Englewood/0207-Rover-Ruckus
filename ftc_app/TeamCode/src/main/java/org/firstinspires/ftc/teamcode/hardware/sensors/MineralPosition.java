package org.firstinspires.ftc.teamcode.hardware.sensors;

import org.firstinspires.ftc.teamcode.hardware.State;

public enum MineralPosition implements State {
    LEFT("Left"),
    CENTER("Center"),
    RIGHT("Right"),
    NOTVISIBLE("None");

    private String str;

    MineralPosition(String str) {
        this.str = str;
    }

    @Override
    public String getStateVal() {
        return str;
    }

}
