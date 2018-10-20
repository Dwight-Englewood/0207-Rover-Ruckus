package org.firstinspires.ftc.teamcode.Teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Hardware.Bot;


@TeleOp(name = "Telebop", group = "Teleop")
public class Telebop extends OpMode {

    Bot robot = new Bot();
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

    }

    @Override
    public void stop() {
        robot.stop();
    }
}