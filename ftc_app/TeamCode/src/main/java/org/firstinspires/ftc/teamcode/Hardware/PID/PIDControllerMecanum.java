package org.firstinspires.ftc.teamcode.Hardware.PID;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.Hardware.Drivetrain.MecanumDriveTrain;

import wen.control.PIDController;

public class PIDControllerMecanum extends PIDController {
    private MecanumDriveTrain dt;

    public PIDControllerMecanum(double pGain, double iGain, double dGain, MecanumDriveTrain dt) {
        super(pGain, iGain, dGain);
        this.dt = dt;
    }

    public void executePIDDrive() {
        double pow = this.getPower();
        if (dt.fl.getMode() != DcMotor.RunMode.RUN_USING_ENCODER) {
            dt.setRunMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }
        dt.drivepow(pow);
    }

    public void executePIDStrafe() {
        double pow = this.getPower();
        if (dt.fl.getMode() != DcMotor.RunMode.RUN_USING_ENCODER) {
            dt.setRunMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }
        dt.strafepow(pow);
    }

    @Override
    public double correction() {
        return Range.clip(super.correction(), -1, 1);
    }

    private double getPower() {
        double currentPos = (Math.abs(dt.fl.getCurrentPosition()) + Math.abs(dt.fr.getCurrentPosition()) +
                Math.abs(dt.bl.getCurrentPosition()) + Math.abs(dt.br.getCurrentPosition())) / 4.0;
        double sign = Math.signum(dt.fr.getCurrentPosition());
        this.updateError(currentPos*sign);
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
        dt.setRunMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }
}
