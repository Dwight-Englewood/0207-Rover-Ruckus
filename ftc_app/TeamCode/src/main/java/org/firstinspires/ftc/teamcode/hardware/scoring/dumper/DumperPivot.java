package org.firstinspires.ftc.teamcode.hardware.scoring.dumper;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.hardware.State;
import org.firstinspires.ftc.teamcode.hardware.Subsystem;

public class DumperPivot implements Subsystem {
    private final double pivotScorePos = 1;
    private final double pivotNotScorePos = -1;
    private Servo dumperPivot;
    private DcMotor spool;
    private DumperState state;
    //private DigitalChannel magSwitch;
    //public Rev2mDistanceSensor distanceSensor;

    public DumperPivot() {
    }

    @Override
    public void init(HardwareMap hwMap) {
        dumperPivot = hwMap.get(Servo.class, "dumperPivot");
        //backWall = hwMap.get(Servo.class, "wall");
        spool = hwMap.get(DcMotor.class, "spool");
        spool.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        spool.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        //magSwitch = hwMap.get(DigitalChannel.class, "dumpmag");
        //distanceSensor = hwMap.get(Rev2mDistanceSensor.class, "dumpdist");
        this.pivotNotScore();
    }

    @Override
    public void start() {

    }

    @Override
    public void reset() {

    }

    @Override
    public void stop() {
        spool.setPower(0);
    }

    @Override
    public State getState() {
        return this.state;
    }

    public void pivotScore() {
        this.dumperPivot.setPosition(pivotScorePos);

    }

    public void pivotNotScore() {
        this.dumperPivot.setPosition(pivotNotScorePos);
    }

    public void up() {
        spool.setPower(-.8);
    }

    public void down() {

        spool.setPower(.8);

    }

    public void idle() {
        spool.setPower(0);
    }


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
}
