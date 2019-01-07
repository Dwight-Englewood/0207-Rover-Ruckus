package org.firstinspires.ftc.teamcode.Hardware.Scoring;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Hardware.State;
import org.firstinspires.ftc.teamcode.Hardware.Subsystem;

public class Rake implements Subsystem {
    private Servo rake;

    @Override
    public void init(HardwareMap hwMap) {
        rake = hwMap.get(Servo.class, "rake");
        this.up();
    }

    public void up() {
        rake.setPosition(0);
    }

    public void down() {
        rake.setPosition(1);
    }

    @Override
    public void start() {

    }

    @Override
    public void reset() {

    }

    @Override
    public void stop() {

    }

    @Override
    public State getState() {
        return null;
    }
}
