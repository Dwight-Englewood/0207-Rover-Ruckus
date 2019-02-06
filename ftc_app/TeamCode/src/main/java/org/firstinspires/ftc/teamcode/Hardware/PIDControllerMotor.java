package org.firstinspires.ftc.teamcode.Hardware;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.PIDCoefficients;
import com.qualcomm.robotcore.util.Range;

import java.math.RoundingMode;
import java.text.DecimalFormat;

public class PIDControllerMotor extends PIDController {

    DcMotor motor;

    public PIDControllerMotor(double pGain, double iGain, double dGain, DcMotor motor) {
        super(pGain, iGain, dGain);
        this.motor = motor;
    }

    @Override
    public double correction() {
        return Range.clip(super.correction(), -1, 1);
    }

    public double getPower() {
        this.updateError(motor.getCurrentPosition());
        return this.correction();
    }

    @Override
    public void setGoal(double goal) { //please only ever pass an int in to this, like really
        super.setGoal(goal);
        motor.setTargetPosition((int) goal);
        motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }

    @Override
    public void reset() {
        super.reset();
        motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }


}
