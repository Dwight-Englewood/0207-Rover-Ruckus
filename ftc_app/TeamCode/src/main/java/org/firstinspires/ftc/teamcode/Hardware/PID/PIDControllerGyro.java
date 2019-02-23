package org.firstinspires.ftc.teamcode.Hardware.PID;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.teamcode.Hardware.Drivetrain.MecanumDriveTrain;

import wen.control.PIDController;

public class PIDControllerGyro extends PIDController {
    BNO055IMU imu;
    MecanumDriveTrain dt;

    public PIDControllerGyro(double pGain, double iGain, double dGain, BNO055IMU imu, MecanumDriveTrain dt) {
        super(pGain, iGain, dGain);
        this.imu = imu;
        this.dt = dt;
    }

    public void executeTurn(){
        double pow = this.getPower();if (dt.fl.getMode() != DcMotor.RunMode.RUN_USING_ENCODER) {
            dt.setRunMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }
        this.dt.turn(pow);
    }

    public double correction() {
        return Range.clip(super.correction(), -1, 1);
    }

    private double getPower() {
        this.updateError(imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES).firstAngle);
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
