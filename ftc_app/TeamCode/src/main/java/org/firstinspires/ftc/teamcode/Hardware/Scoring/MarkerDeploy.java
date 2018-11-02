package org.firstinspires.ftc.teamcode.Hardware.Scoring;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Hardware.State;
import org.firstinspires.ftc.teamcode.Hardware.Subsystem;

public class MarkerDeploy implements Subsystem {
    public MarkerDeploy() {

    }

    private Servo servo;

    private enum markerState implements State {
        HOLDING("Holding"),
        DROPPING("Dropping");


        private String str;
        markerState(String str) {
            this.str = str;
        }

        @Override
        public String getStateVal() {
            return str;
        }
    }
    private markerState state;

    @Override
    public void init(HardwareMap hwMap) {
        servo = hwMap.get(Servo.class, "mark");
        this.raise();
    }

    @Override
    public void start() {
        this.raise();
    }

    @Override
    public void reset() {
        this.raise();
    }

    @Override
    public void stop() {}

    public void drop() {
        servo.setPosition(0);
    }

    public void raise() {
        servo.setPosition(1);
    }

    @Override
    public State getState() {
        return state;
    }
}
