package org.firstinspires.ftc.teamcode.Hardware.Scoring;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.Hardware.State;
import org.firstinspires.ftc.teamcode.Hardware.Subsystem;

public class Lift implements Subsystem {
    public Lift() {}

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
    public liftState getState() {
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
        if (motor.getCurrentPosition() >= 19700) {
            motor.setPower(0);
            motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            return true;
        }
        if (motor.getMode() != DcMotor.RunMode.RUN_TO_POSITION) motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        if (motor.getTargetPosition() != 19700) motor.setTargetPosition(19700);
        motor.setPower(1);
        return false;
    }


    //HOPEFULLY REPLACE W/ MAGLIMSWITCH THING;
    public boolean oldYears() {
        if (motor.getCurrentPosition() < 25) {
            motor.setPower(0);
            motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            return true;
        }
        if (motor.getMode() != DcMotor.RunMode.RUN_TO_POSITION) motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        if (motor.getTargetPosition() != 0) motor.setTargetPosition(0);
        motor.setPower(-1);
        return false;
    }

    //HOPEFULLY REPLACE W/ MAGLIMSWITCH THING
    public boolean teleopOldYears() {
        if (motor.getCurrentPosition() < -19700) {
            motor.setPower(0);
            motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            return true;
        }
        if (motor.getMode() != DcMotor.RunMode.RUN_TO_POSITION) motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        if (motor.getTargetPosition() != -19700) motor.setTargetPosition(-19700);
        motor.setPower(-1);
        return false;
    }

    public int getTicks() {
        return motor.getCurrentPosition();
    }

}


