package org.firstinspires.ftc.teamcode.sandbox.control;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.hardware.bots.Bot;
import org.firstinspires.ftc.teamcode.hardware.bots.RebuildBot;

import wen.control.PIDController;


@TeleOp(name = "PIDTestDistance ", group = "kms")
public class PIDTestDistance extends OpMode {

    RebuildBot boot = new RebuildBot(true, true);
    double kp = 2;
    double ki = 0.0005;
    double kd = 5;
    PIDController pid = new PIDController(kp, ki, kd);
    double resolution = 10;
    int targetEncoderTicks = 3000;

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
        pid.setGoal(targetEncoderTicks);


    }

    @Override
    public void loop() {
        if (gamepad1.b) {
            pid.reset();
            boot.driveTrain.setRunMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            pid.setGoal(targetEncoderTicks);
        }
        if (gamepad1.left_bumper) {
            boot.driveTrain.setRunMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }

        if (gamepad1.a) {
            double currentSum = boot.driveTrain.br.getCurrentPosition() +  boot.driveTrain.bl.getCurrentPosition() +  boot.driveTrain.fl.getCurrentPosition() +  boot.driveTrain.fr.getCurrentPosition();
            currentSum = currentSum / 4;
            pid.updateError(currentSum);
            if (pid.goalReached(resolution)) { //checks if delta is out of range
                telemetry.addData("Done", 0);
                boot.driveTrain.drivepow(0);
            } else {
                double pidcorrect = pid.correction();
                telemetry.addData("Correction", pidcorrect);
                boot.driveTrain.drivepow(pidcorrect / targetEncoderTicks);
            }
            telemetry.addData("PID Error", pid.error);
            telemetry.addData("Current Dist", currentSum);
            telemetry.addData("Target Dist", targetEncoderTicks);
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
        if (gamepad2.left_trigger > .5) {
            this.pid.iGain = gamepad2.right_stick_y / 100;
        }
        if (gamepad2.right_trigger > .5) {
            this.pid.dGain = gamepad2.left_stick_y * 20;
        }
        if (gamepad2.left_bumper) {
            this.pid.pGain = gamepad2.left_stick_y * 10;
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

