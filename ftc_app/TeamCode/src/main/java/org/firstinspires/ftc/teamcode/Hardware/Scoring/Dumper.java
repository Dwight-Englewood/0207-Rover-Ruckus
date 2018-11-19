package org.firstinspires.ftc.teamcode.Hardware.Scoring;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Hardware.State;
import org.firstinspires.ftc.teamcode.Hardware.Subsystem;

public class Dumper implements Subsystem {
    public Dumper() {}

    private Servo wall;
    private enum DumperState implements State {
        DUMPING("Dumping"),
        HOLDING("Holding");


        private String str;
        DumperState(String str) {
            this.str = str;
        }

        @Override
        public String getStateVal() {
            return str;
        }
    }
    private DumperState state;

    @Override
    public void init(HardwareMap hwMap) {
        wall = hwMap.get(Servo.class, "dump");
        this.close();
    }

    @Override
    public void start() {

    }

    @Override
    public void reset() {
        this.close();
    }

    @Override
    public void stop() {

    }

    @Override
    public State getState() {
        return this.state;
    }

    public void close() {
        wall.setPosition(1);
    }

    public void open() {
        wall.setPosition(0);
    }
}
