package org.firstinspires.ftc.teamcode.Hardware.Scoring;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Hardware.State;
import org.firstinspires.ftc.teamcode.Hardware.Subsystem;

public class Rake implements Subsystem {
    private Servo bottom;
    private Servo top;


    @Override
    public void init(HardwareMap hwMap) {
        bottom = hwMap.get(Servo.class, "rakebot");
        top = hwMap.get(Servo.class, "raketop");
        this.up();
    }

    public void up() {
        bottom.setPosition(1);
        top.setPosition(0);
    }

    public void downfirst() {
        top.setPosition(.8);
    }

    public void downButNotAsMuch() {
        bottom.setPosition(.6);
        top.setPosition(.5);
    }

    public void down() {
        bottom.setPosition(.6);
        top.setPosition(.8);
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
