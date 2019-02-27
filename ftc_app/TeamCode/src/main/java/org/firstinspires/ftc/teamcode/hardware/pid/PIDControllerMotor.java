package org.firstinspires.ftc.teamcode.hardware.pid;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;
import wen.control.PIDController;

public class PIDControllerMotor extends PIDController {

    DcMotor motor;

    public PIDControllerMotor(double pGain, double iGain, double dGain, DcMotor motor) {
        super(pGain, iGain, dGain);
        this.motor = motor;
    }

    public void executePID() {
        if (motor.getMode() != DcMotor.RunMode.RUN_USING_ENCODER) {
            motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }
        this.motor.setPower(this.getPower());
    }

    @Override
    public double correction() {
        return Range.clip(super.correction(), -1, 1);
    }

    private double getPower() {
        this.updateError(motor.getCurrentPosition());
        return this.correction();
    }

    @Override
    public void setGoal(double goal) { //please only ever pass an int in to this, like really
        super.setGoal(goal);
        this.reset();
    }

    @Override
    public void reset() {
        //super.reset();
        motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }


}
