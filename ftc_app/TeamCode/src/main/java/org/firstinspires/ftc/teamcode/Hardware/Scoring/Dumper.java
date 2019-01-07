package org.firstinspires.ftc.teamcode.Hardware.Scoring;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Hardware.State;
import org.firstinspires.ftc.teamcode.Hardware.Subsystem;

public class Dumper implements Subsystem {
    public Dumper() {}

    private Servo wall;
    private DcMotor spool;
    private DigitalChannel magSwitch;
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
        spool = hwMap.get(DcMotor.class, "spool");
        spool.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        magSwitch = hwMap.get(DigitalChannel.class, "dumpmag");
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

    public void up() {spool.setPower(-.8);}

    public void down() {
        if (isBottom()) this.stop();
        else spool.setPower(.8);
    }

    public boolean isBottom() {return !magSwitch.getState();}
}
