package org.firstinspires.ftc.teamcode.Hardware.Scoring;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.Hardware.State;
import org.firstinspires.ftc.teamcode.Hardware.Subsystem;

public class Lift implements Subsystem {
    public Lift() {}
    private final int tickCount = 3500;

    private DcMotor motor;
    private DigitalChannel magSwitch;
    private enum liftState implements State {
        LIFTING("Lifting"),
        DROPPING("Dropping"),
        STOPPED("Stopped");

        private String str;
        liftState(String str) {
            this.str = str;
        }

        @Override
        public String getStateVal() {
            return str;
        }
    }
    private liftState state;

    @Override
    public void init(HardwareMap hwMap) {
        motor = hwMap.get(DcMotor.class, "lift");
        motor.setDirection(DcMotorSimple.Direction.FORWARD);
        motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        state = liftState.STOPPED;

        magSwitch = hwMap.get(DigitalChannel.class, "liftmag");
    }

    @Override
    public void start() {
        motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    @Override
    public void reset() {
        motor.setPower(0);
        state = liftState.STOPPED;
    }

    @Override
    public void stop() {
        motor.setPower(0);
        state = liftState.STOPPED;
    }

    @Override
    public State getState() {
        return state;
    }

    public void lift() {
        motor.setPower(-1);
        state = liftState.LIFTING;
    }

    public void drop() {
        motor.setPower(1);
        state = liftState.DROPPING;
    }

    public boolean newYears() {
        if (motor.getCurrentPosition() >= tickCount) {
            motor.setPower(0);
            motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            return true;
        }
        if (motor.getMode() != DcMotor.RunMode.RUN_TO_POSITION) {
            motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        }
        if (motor.getTargetPosition() != tickCount) {
            motor.setTargetPosition(tickCount);
        }
        motor.setPower(1);
        return false;
    }

    public boolean newYearsMag() {
        if (motor.getMode() != DcMotor.RunMode.RUN_USING_ENCODER) {
            motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }
        if (isAllTheWayUp()) {
            motor.setPower(0);
            return true;
        }
        motor.setPower(1);
        return false;
    }

    //TODO: Check if works
    public boolean oldYears() {
        // if the magnet is near the switch
        if (magSwitch.getState()) {
            //stop running the motor
            motor.setPower(0);
            //reset the runmode
            motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            //return true to flag to system that operation is complete
            return true;
        }
        //otherwise continue pulling lift in
        motor.setPower(-1);
        return false;
    }

    public int getTicks() {
        return motor.getCurrentPosition();
    }

    public boolean isAllTheWayUp() {
        return !this.magSwitch.getState();
    }

}


