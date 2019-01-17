package org.firstinspires.ftc.teamcode.Teleop;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Hardware.Bot;


@TeleOp(name = "Telebop2Person", group = "Teleop")
public class Telebop2Person extends OpMode {

    Bot robot = new Bot(false);
    BNO055IMU imu;
    BNO055IMU.Parameters parameters;

    ElapsedTime slowTimer = new ElapsedTime();
    boolean slow = false;
    ElapsedTime reverseTimer = new ElapsedTime();
    boolean reverse = false;

    @Override
    public void init() {
        robot.init(hardwareMap);
    }

    @Override
    public void init_loop() {
            telemetry.addLine("Ready.");
            telemetry.update();
    }

    @Override
    public void start() {
        robot.start();
        slowTimer.reset();
        reverseTimer.reset();
    }

    @Override
    public void loop() {
        if (gamepad1.start && slowTimer.milliseconds() >= 750) {
            slow = !slow;
            slowTimer.reset();
        }

        if (gamepad1.back && reverseTimer.milliseconds() >= 750) {
            reverse = !reverse;
            reverseTimer.reset();
        }

        robot.driveTrain.tankControl(gamepad1, slow, reverse);

        if (gamepad1.dpad_up) robot.lift.drop();
        else if (gamepad1.dpad_down) robot.lift.lift();
        else robot.lift.stop();

        if (gamepad1.b) robot.markerDeploy.drop();
        else robot.markerDeploy.raise();

        if (gamepad2.left_stick_y < -0.5) robot.dumper.up();
        else if (gamepad2.left_stick_y > .5) robot.dumper.down();
        else robot.dumper.stop();

        if (gamepad1.a) robot.dumper.open();
        else robot.dumper.close();

        if (gamepad2.right_stick_y < -.5) robot.intake.intake();
        else if (gamepad2.right_stick_y > .5) robot.intake.outtake();
        else robot.intake.stop();

        telemetry.addData("Lift Ticks", robot.lift.getTicks());
        telemetry.addData("Slow?", slow);
        telemetry.addData("isdown?", robot.dumper.isBottom());
        telemetry.update();
    }

    @Override
    public void stop() {
        robot.stop();
    }
}