package org.firstinspires.ftc.teamcode.Hardware.Scoring;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.Hardware.State;
import org.firstinspires.ftc.teamcode.Hardware.Subsystem;

public class Lift implements Subsystem {
    public Lift() {}

    private DcMotor motor;
    private enum liftState implements State {
        LIFTING("Lifting"),
        DROPPING("Dropping"),
        STOPPED("Stopped");

        private String str;
        liftState(String str) {
            this.str = str;
        }
        public String getStateVal() {
            return str;
        }
    }
    private liftState state;

    @Override
    public void init(HardwareMap hwMap) {
        motor = hwMap.get(DcMotor.class, "lift");
        motor.setDirection(DcMotorSimple.Direction.FORWARD);
        motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        state = liftState.STOPPED;
    }

    @Override
    public void start() {

    }

    @Override
    public void reset() {
        motor.setPower(0);
        state = liftState.STOPPED;
    }

    public void lift() {
        motor.setPower(1);
        state = liftState.LIFTING;
    }

    public void drop() {
        motor.setPower(-1);
        state = liftState.DROPPING;
    }

    @Override
    public liftState getState() {
        return state;
    }
}


