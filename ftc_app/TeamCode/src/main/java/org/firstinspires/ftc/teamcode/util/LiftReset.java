package org.firstinspires.ftc.teamcode.util;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.hardware.bots.Bot;


@TeleOp(name = "LiftReset", group = "Teleop")
@Disabled
public class LiftReset extends OpMode {

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
        telemetry.addData("oldYears?", robot.lift.oldYears());
        telemetry.addData("ticks ", robot.lift.getTicks());
    }

    @Override
    public void stop() {
        robot.stop();
    }
}