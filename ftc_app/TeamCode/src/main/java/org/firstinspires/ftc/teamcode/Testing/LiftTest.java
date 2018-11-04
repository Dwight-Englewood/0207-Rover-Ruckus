package org.firstinspires.ftc.teamcode.Testing;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Hardware.Bot;


@TeleOp(name = "LiftTest", group = "Teleop")
public class LiftTest extends OpMode {

    Bot robot = new Bot(false);

    @Override
    public void init() {
        robot.init(hardwareMap);
    }

    @Override
    public void init_loop() {
    }

    @Override
    public void start() {
        robot.start();
    }

    @Override
    public void loop() {
        telemetry.addData("newyears?", robot.lift.newYears());
    }

    @Override
    public void stop() {
        robot.stop();
    }
}