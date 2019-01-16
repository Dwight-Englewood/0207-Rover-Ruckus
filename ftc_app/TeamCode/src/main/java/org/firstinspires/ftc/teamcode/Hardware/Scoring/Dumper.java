package org.firstinspires.ftc.teamcode.Hardware.Scoring;

import com.qualcomm.hardware.rev.Rev2mDistanceSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.Hardware.State;
import org.firstinspires.ftc.teamcode.Hardware.Subsystem;

public class Dumper implements Subsystem {
    public Dumper() {}

    private Servo wall;
    private Servo backWall;
    private DcMotor spool;
    private DigitalChannel magSwitch;
    public Rev2mDistanceSensor distanceSensor;
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
        backWall = hwMap.get(Servo.class, "wall");
        spool = hwMap.get(DcMotor.class, "spool");
        spool.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        spool.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        magSwitch = hwMap.get(DigitalChannel.class, "dumpmag");
        distanceSensor = hwMap.get(Rev2mDistanceSensor.class, "dumpdist");
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
        spool.setPower(0);
    }

    @Override
    public State getState() {
        return this.state;
    }

    public void close() {
        wall.setPosition(.2);
    }

    public void open() {
        wall.setPosition(.8);
    }

    public void up() {
        if (isTop()) {
            this.stop();
        } else {
            spool.setPower(-.8);
        }
        backWall.setPosition(1);
    }

    public void down() {
        if (isBottom()) {
            this.stop();
            backWall.setPosition(0);
        } else {
            spool.setPower(.8);
        }
    }

    public boolean isBottom() {return !magSwitch.getState();}
    public boolean isTop() { return distanceSensor.getDistance(DistanceUnit.CM) > 50; }
}
