package org.firstinspires.ftc.teamcode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.hardware.bots.Bot;

@Disabled
@TeleOp(name = "TelebopOldbot", group = "Teleop")
public class TelebopOldbot extends OpMode {

    Bot robot = new Bot(false);

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

        if (gamepad1.dpad_left) robot.rake.up();
        else if (gamepad1.dpad_right) robot.rake.down();


        if (gamepad1.b) robot.markerDeploy.drop();
        else robot.markerDeploy.raise();

        if (gamepad1.x) robot.dumper.up();
        else if (gamepad1.y) robot.dumper.down();
        else robot.dumper.stop();

        if (gamepad1.a) robot.dumper.open();
        else robot.dumper.close();

        if (gamepad1.right_bumper) robot.intake.intake();
        else if (gamepad1.left_bumper) robot.intake.outtake();
        else robot.intake.stop();

        telemetry.addData("Lift Ticks", robot.lift.getTicks());
        telemetry.addData("Slow?", slow);
        telemetry.update();
    }

    @Override
    public void stop() {
        robot.stop();
    }
}