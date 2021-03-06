package org.firstinspires.ftc.teamcode.sandbox.control;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.hardware.bots.Bot;
import org.firstinspires.ftc.teamcode.hardware.bots.RebuildBot;

import wen.control.PIDController;


@TeleOp(name = "PIDTestGyro", group = "kms")
@Disabled
public class PIDTestGyro extends OpMode {

    RebuildBot boot = new RebuildBot(true, true);
    double kp = 10;
    double ki = 0;
    double kd = 20; //kinda big kinda ank but they work for turning
    PIDController pid = new PIDController(kp, ki, kd);
    double resolution = .5;

    @Override
    public void init() {
        boot.init(hardwareMap);

    }

    @Override
    public void init_loop() {

    }

    @Override
    public void start() {
        boot.start();
        pid.setGoal(0);


    }

    @Override
    public void loop() {
        if (gamepad1.b) {
            pid.reset();
        }

        if (gamepad1.a) {
            double gyroActual = boot.imu.getGyroRotation(AngleUnit.DEGREES);


            double delta = (gyroActual + 360.0) % 360.0; //the difference between target and actual mod 360
            if (delta > 180.0) {
                delta -= 360.0; //makes delta between -180 and 180
            }
            pid.updateError((gyroActual-70) / 360);
            if (pid.goalReached(.01)) { //checks if delta is out of range
                telemetry.addData("Done", 0);
                boot.driveTrain.turn(0.0);
            } else {
                double pidcorrect = pid.correction();
                telemetry.addData("Correction", pidcorrect);
                boot.driveTrain.turn(pidcorrect);
            }
            telemetry.addData("PID Error", pid.error);
            telemetry.addData("Current Gyro", gyroActual);
        } else {
            if (gamepad1.dpad_up) {
                boot.driveTrain.bl.setPower(1);
                boot.driveTrain.fl.setPower(1);
                boot.driveTrain.fr.setPower(1);
                boot.driveTrain.br.setPower(1);
            } else if (gamepad1.dpad_down) {
                boot.driveTrain.bl.setPower(-1);
                boot.driveTrain.fl.setPower(-1);
                boot.driveTrain.fr.setPower(-1);
                boot.driveTrain.br.setPower(-1);
            } else if (gamepad1.dpad_left) {
                boot.driveTrain.bl.setPower(-1);
                boot.driveTrain.fl.setPower(1);
                boot.driveTrain.fr.setPower(-1);
                boot.driveTrain.br.setPower(1);
            } else if (gamepad1.dpad_right) {
                boot.driveTrain.bl.setPower(1);
                boot.driveTrain.fl.setPower(-1);
                boot.driveTrain.fr.setPower(1);
                boot.driveTrain.br.setPower(-1);
            } else if (gamepad1.x) {
                boot.driveTrain.turn(-1);
            } else if (gamepad1.y) {
                boot.driveTrain.turn(1);
            } else {
                boot.driveTrain.bl.setPower(0);
                boot.driveTrain.fl.setPower(0);
                boot.driveTrain.fr.setPower(0);
                boot.driveTrain.br.setPower(0);
            }
        }
        if (gamepad1.left_trigger > .5) {
            this.pid.iGain = gamepad1.right_stick_y * 10;
        }
        if (gamepad1.right_trigger > .5) {
            this.pid.dGain = gamepad1.left_stick_y * 20;
        }
        telemetry.addData("kp", pid.pGain);
        telemetry.addData("ki", pid.iGain);
        telemetry.addData("kd", pid.dGain);

    }


    @Override
    public void stop() {
        boot.stop();
    }
}

