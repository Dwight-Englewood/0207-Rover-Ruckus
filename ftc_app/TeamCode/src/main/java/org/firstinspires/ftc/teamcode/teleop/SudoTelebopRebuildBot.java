package org.firstinspires.ftc.teamcode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.ejml.simple.SimpleMatrix;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.hardware.bots.RebuildBot;


@TeleOp(name = "SudoTeleopRebuildBot", group = "Teleop")
//@Disabled
public class SudoTelebopRebuildBot extends OpMode {

    RebuildBot boot = new RebuildBot(false, true);
    /*
    fr = 1
    fl = 2
    bl = 3
    br = 4
     */
    double joyL;
    double joyR;
    long lastTime;

    int timerSwap = 0;

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
        boot.driveTrain.setRunMode(DcMotor.RunMode.RUN_USING_ENCODER);
        lastTime = System.currentTimeMillis();

    }

    @Override
    public void loop() {

        double botTheta = boot.imu.getGyroRotation(AngleUnit.RADIANS);
        //The readings from the gyro are different from the reading needed for the field centric code, so we apply a function to fix it
        botTheta = (botTheta < 0) ? -botTheta : 2 * Math.PI - botTheta;
        botTheta = botTheta;
        double lsx = -gamepad1.left_stick_x;
        double lsy = -gamepad1.left_stick_y;
        double theta = gamepad1.right_stick_x / 2;
        SimpleMatrix powVector = boot.driveTrain.drive(lsx, lsy, theta, botTheta);


        if (gamepad1.dpad_up) {
            boot.dumperPivot.variableSafe(.5);
        } else if (gamepad1.dpad_down) {
            boot.dumperPivot.variableSafe(-.3);
        } else {
            boot.dumperPivot.variableSafe(0);
        }

        if (gamepad1.x) {
            boot.dumperPivot.pivotScore();
        } else {
            boot.dumperPivot.pivotNotScore();
        }

        if (gamepad1.dpad_right) {
            boot.intakeSlides.variableMove(.5);
        } else if (gamepad1.dpad_left) {
            boot.intakeSlides.variableMove(-.5);
        } else {
            boot.intakeSlides.variableMove(0);
        }

        if (gamepad1.left_trigger > .5) {
            boot.intakeSlides.outtake();
        } else if (gamepad1.right_trigger > .5) {
            boot.intakeSlides.intake();
        } else {
            boot.intakeSlides.notake();
        }

        if (gamepad2.a) {
            boot.intakeSlides.pivotDown();
        } else if (gamepad2.b) {
            boot.intakeSlides.pivotUp();
        } else if (gamepad2.y) {
            boot.intakeSlides.pivotMiddle();
        }

    }

    @Override
    public void stop() {
        boot.stop();

    }
}
