package org.firstinspires.ftc.teamcode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

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
        
        boot.driveTrain.tankControl(gamepad1, false, false);

        this.boot.dumperPivot.variableSafe(-gamepad2.right_stick_y);

        if (gamepad1.dpad_down) {
            boot.lift.lift();
        } else if (gamepad1.dpad_up) {
            boot.lift.drop();
        } else {
            boot.lift.stop();
        }

        if (gamepad1.a) {
            boot.dumperPivot.pivotScore();
        } else {
            boot.dumperPivot.pivotNotScore();
        }

        boot.intakeSlides.variableMove(-gamepad2.left_stick_y);

        if (gamepad2.left_trigger > .5) {
            boot.intakeSlides.outtake();
        } else if (gamepad2.right_trigger > .5) {
            boot.intakeSlides.intake();
        } else {
            boot.intakeSlides.notake();
        }

        if (gamepad2.a) {
            boot.intakeSlides.pivotDown();
        } else if (gamepad2.b) {
            boot.intakeSlides.pivotUp();
        } else if (gamepad2.x) {
            boot.intakeSlides.pivotMiddle();
        }

        telemetry.addData("magswitch", boot.intakeSlides.magSwitchIntake.getState());
    }

    @Override
    public void stop() {
        boot.stop();

    }
}
