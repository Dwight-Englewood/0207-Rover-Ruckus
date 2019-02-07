package org.firstinspires.ftc.teamcode.Hardware.PID;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;

public class PIDControllerMecanum extends PIDController {
    DcMotor FL, FR, BL, BR;

    public PIDControllerMecanum(double pGain, double iGain, double dGain, DcMotor FL, DcMotor FR, DcMotor BL, DcMotor BR) {
        super(pGain, iGain, dGain);
        this.FL = FL;
        this.FR = FR;
        this.BL = BL;
        this.BR = BR;
    }

    @Override
    public double correction() {
        return Range.clip(super.correction(), -1, 1);
    }

    public double getPower() {
        double currentPos = (Math.abs(FL.getCurrentPosition()) + Math.abs(FR.getCurrentPosition()) +
                Math.abs(BL.getCurrentPosition()) + Math.abs(BR.getCurrentPosition())) / 4.0;
        this.updateError(currentPos);
        return this.correction();
    }

    @Override
    public void setGoal(double goal) { //please only ever pass an int in to this, like really
        super.setGoal(goal);
        FL.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        FR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        BL.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        BR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);


    }

    @Override
    public void reset() {
        super.reset();
        FL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        FR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        BL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        BR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }
}
