package org.firstinspires.ftc.teamcode.autonomous;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.hardware.bots.Bot;
import org.firstinspires.ftc.teamcode.hardware.bots.RebuildBot;

import wen.control.PIDController;

public class OldAutonMethods {
    public Bot robot;
    public ElapsedTime timer;
    public int command;

    private double kp = 10;
    private double ki = 0;
    private double kd = 20; //kinda big kinda ank but they work for turning
    private PIDController gyroPID = new PIDController(kp, ki, kd);

    public OldAutonMethods() {
        this.robot = new Bot(true, true);
        timer = new ElapsedTime();
        command = 0;
    }

    public void finishDrive() {
        if (Math.abs(robot.driveTrain.fl.getTargetPosition() - robot.driveTrain.fl.getCurrentPosition()) < 20 ||
                Math.abs(robot.driveTrain.br.getTargetPosition() - robot.driveTrain.br.getCurrentPosition()) < 20 ||
                Math.abs(robot.driveTrain.bl.getTargetPosition() - robot.driveTrain.bl.getCurrentPosition()) < 20 ||
                Math.abs(robot.driveTrain.fr.getTargetPosition() - robot.driveTrain.fr.getCurrentPosition()) < 20) {
            robot.driveTrain.stop();
            robot.driveTrain.setRunMode(DcMotor.RunMode.RUN_USING_ENCODER);
            this.timer.reset();
            this.command++;
        } else {
            robot.driveTrain.scalePower();
        }
    }

    public void germanFinishDrive() {
        if (Math.abs(robot.driveTrain.fl.getTargetPosition() - robot.driveTrain.fl.getCurrentPosition()) < 20 ||
                Math.abs(robot.driveTrain.br.getTargetPosition() - robot.driveTrain.br.getCurrentPosition()) < 20 ||
                Math.abs(robot.driveTrain.bl.getTargetPosition() - robot.driveTrain.bl.getCurrentPosition()) < 20 ||
                Math.abs(robot.driveTrain.fr.getTargetPosition() - robot.driveTrain.fr.getCurrentPosition()) < 20) {
            robot.driveTrain.stop();
            robot.driveTrain.setRunMode(DcMotor.RunMode.RUN_USING_ENCODER);
            this.timer.reset();
            this.command++;
        } else {
            robot.driveTrain.germanScalePower();
        }
    }

    public void slightlyLesGgermanFinishDrive() {
        if (Math.abs(robot.driveTrain.fl.getTargetPosition() - robot.driveTrain.fl.getCurrentPosition()) < 20 ||
                Math.abs(robot.driveTrain.br.getTargetPosition() - robot.driveTrain.br.getCurrentPosition()) < 20 ||
                Math.abs(robot.driveTrain.bl.getTargetPosition() - robot.driveTrain.bl.getCurrentPosition()) < 20 ||
                Math.abs(robot.driveTrain.fr.getTargetPosition() - robot.driveTrain.fr.getCurrentPosition()) < 20) {
            robot.driveTrain.stop();
            robot.driveTrain.setRunMode(DcMotor.RunMode.RUN_USING_ENCODER);
            this.timer.reset();
            this.command++;
        } else {
            robot.driveTrain.slightlyLessGermanScalePower();
        }
    }
    public int gyroCorrect(int gyroTarget, int gyroRange, double minSpeed, double addSpeed) {
        int gyroVal = (int) this.robot.sensorSystem.getGyroRotation(AngleUnit.DEGREES);
        this.robot.driveTrain.gyroCorrect(gyroTarget, gyroRange, gyroVal, minSpeed, addSpeed);
        if (this.robot.driveTrain.fl.getPower() == 0) {
            this.timer.reset();
            this.command++;
        }
        return gyroVal;
    }

    /*public void PIDTurn(int gyroTarget, int resolution) {
        double gyroActual = robot.imu.getGyroRotation(AngleUnit.DEGREES);

        double delta = (gyroActual + 360.0) % 360.0; //the difference between target and actual mod 360
        if (delta > 180.0) {
            delta -= 360.0; //makes delta between -180 and 180
        }
        gyroPID.updateError(gyroActual / 360); //TODO: MIGHT NEED TO BE DELTA
        if (gyroPID.goalReached(.01)) { //checks if delta is out of range
            this.command++;
            this.robot.driveTrain.turn(0.0);
        } else {
            double pidcorrect = gyroPID.correction();
            this.robot.driveTrain.turn(pidcorrect);
        }
    }*/

    public void setTarget(int target) {
        this.robot.driveTrain.setTarget(target);
        this.timer.reset();
        this.command++;
    }

    public void setStrafeTarget(int target) {
        this.robot.driveTrain.setStrafeTarget(target);
        this.timer.reset();
        this.command++;
    }
}
