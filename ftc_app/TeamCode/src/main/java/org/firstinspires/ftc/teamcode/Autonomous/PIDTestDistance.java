package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.Hardware.Bot;

import wen.control.PIDController;


@TeleOp(name = "PIDTest", group = "kms")
public class PIDTestDistance extends OpMode {

    Bot boot = new Bot(true, true);
    double kp = 10;
    double ki = 0;
    double kd = 20; //kinda big kinda ank but they work for turning
    PIDController pidTurn = new PIDController(kp, ki, kd);
    double gyroTarget = 90;
    double gyroRange = .5;

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
        pidTurn.setGoal(boot.driveTrain.);


    }

    @Override
    public void loop() {
        if (gamepad1.b) {
            pidTurn.reset();
        }

        if (gamepad1.a) {
            double gyroActual = boot.sensorSystem.getGyroRotation(AngleUnit.DEGREES);


            double delta = (gyroTarget - gyroActual + 360.0) % 360.0; //the difference between target and actual mod 360
            if (delta > 180.0) {
                delta -= 360.0; //makes delta between -180 and 180
            }
            pidTurn.updateError(gyroActual / 360);
            if (pidTurn.goalReached(.01)) { //checks if delta is out of range
                telemetry.addData("Done", 0);
                boot.driveTrain.turn(0.0);
            } else {
                double pidcorrect = pidTurn.correction();
                telemetry.addData("Correction", pidcorrect);
                boot.driveTrain.drivepow(pidcorrect);
            }
            telemetry.addData("PID Error", pidTurn.error);
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
            this.pidTurn.iGain = gamepad1.right_stick_y * 10;
        }
        if (gamepad1.right_trigger > .5) {
            this.pidTurn.dGain = gamepad1.left_stick_y * 20;
        }
        telemetry.addData("kp", pidTurn.pGain);
        telemetry.addData("ki", pidTurn.iGain);
        telemetry.addData("kd", pidTurn.dGain);

    }


    @Override
    public void stop() {
        boot.stop();
    }
}

