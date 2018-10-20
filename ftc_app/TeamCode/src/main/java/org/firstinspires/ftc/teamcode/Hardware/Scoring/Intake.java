package org.firstinspires.ftc.teamcode.Hardware.Scoring;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.Hardware.State;
import org.firstinspires.ftc.teamcode.Hardware.Subsystem;

public class Intake implements Subsystem {
    public Intake() {}

    private DcMotor motor;
    private enum intakeState {
        INTAKING,
        OUTTAKING,
        STOPPED;
    }
    private intakeState state;

    @Override
    public void init(HardwareMap hwMap) {
        motor = hwMap.get(DcMotor.class, "int");
        motor.setDirection(DcMotorSimple.Direction.FORWARD);
        motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        state = intakeState.STOPPED;
    }

    @Override
    public void start() {

    }

    @Override
    public void reset() {
        motor.setPower(0);
        state = intakeState.STOPPED;
    }

    public void intake() {
        motor.setPower(1);
        state = intakeState.INTAKING;
    }

    public void outtake() {
        motor.setPower(-1);
        state = intakeState.OUTTAKING;
    }

    @Override
    public String toString() {
        switch(state) {
            case INTAKING:
                return "Intaking";

            case OUTTAKING:
                return "Outtaking";

            case STOPPED:
                return "Stopped";

            default:
                return super.toString();
        }
    }
}
