package org.firstinspires.ftc.teamcode.hardware.scoring;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.hardware.State;
import org.firstinspires.ftc.teamcode.hardware.Subsystem;

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
        bottom.setPosition(.8);
        top.setPosition(0.4);
    }

    public void downfirst() {
        top.setPosition(1);
    }

    public void downButNotAsMuch() {
        bottom.setPosition(.6);
        top.setPosition(1);
    }

    public void down() {
        bottom.setPosition(.40);
        top.setPosition(1);
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
