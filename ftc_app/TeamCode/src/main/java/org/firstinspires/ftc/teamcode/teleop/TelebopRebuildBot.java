package org.firstinspires.ftc.teamcode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.ejml.simple.SimpleMatrix;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.hardware.bots.RebuildBot;


@TeleOp(name = "TeleopRebuildBot", group = "Teleop")
//@Disabled
public class TelebopRebuildBot extends OpMode {

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
        botTheta = -botTheta;
        double lsx = -gamepad1.left_stick_x;
        double lsy = gamepad1.left_stick_y;
        double theta = gamepad1.right_stick_x / 2;
        //SimpleMatrix powVector = boot.driveTrain.drive(lsx, lsy, theta, botTheta);

        boot.driveTrain.tankControl(gamepad1, false, true);


        double liftPow = -gamepad2.right_stick_y;
        if (liftPow > 0.05) {
            boot.dumperPivot.upNotSafe(liftPow);
        } else if (liftPow < -0.05) {
            boot.dumperPivot.downSafe(liftPow);
        } else {
            boot.dumperPivot.idle();
        }

        if (gamepad2.dpad_up) {
            boot.lift.lift();
        } else if (gamepad2.dpad_down) {
            boot.lift.drop();
        } else {
            boot.lift.stop();
        }

        if (gamepad1.a) {
            boot.dumperPivot.pivotScore();
        } else {
            boot.dumperPivot.pivotNotScore();
        }

        boot.intakeSlides.moveVariable(-gamepad1.left_stick_y);

        if (gamepad2.left_trigger > .5) {
            boot.intakeSlides.outtake();
        } else if (gamepad2.right_trigger > .5) {
            boot.intakeSlides.intake();
        } else {
            boot.intakeSlides.intake.setPower(0);
        }

        if (gamepad2.b) {
            boot.intakeSlides.pivotUp();
        } else if (gamepad2.a) {
            boot.intakeSlides.pivotDown();
        }


        telemetry.addData("sad", boot.intakeSlides.magSwitchIntake.getState());

    }

    @Override
    public void stop() {
        boot.stop();

    }
}
