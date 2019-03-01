package org.firstinspires.ftc.teamcode.hardware.scoring.dumper;

import com.qualcomm.hardware.rev.Rev2mDistanceSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.hardware.State;
import org.firstinspires.ftc.teamcode.hardware.Subsystem;

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
        spool.setPower(-.8);
        backWall.setPosition(1);
    }

    public void upWithFailsafe() {
        //If the dumper is at its peak height, prevent despooling
        if (isTop()) {
            this.stop();
        } else { //Else raise the dumper
            spool.setPower(-.8);
        }
        //Set the backwall to close
        backWall.setPosition(1);
    }

    public void down() {
        //If the dumper is bottomed out, prevent despooling
        if (isBottom()) {
            this.stop();
            //set the backwall to open
            backWall.setPosition(0);

        } else { //Else lower the dumper
            spool.setPower(.8);
        }
    }

    public boolean isBottom() {
        //Return true if the magnet is near the sensor
        return !magSwitch.getState();
    }
    public boolean isTop() {
        //Return true if the distance is greater than 50cm
        return distanceSensor.getDistance(DistanceUnit.CM) > 50;
    }
}
