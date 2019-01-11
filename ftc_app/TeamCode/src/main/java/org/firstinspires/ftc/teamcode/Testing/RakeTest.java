package org.firstinspires.ftc.teamcode.Testing;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;


@TeleOp(name = "RakeTest", group = "Teleop")
public class RakeTest extends OpMode {
    Servo top;
    Servo bot;
    @Override
    public void init() {
        this.top = hardwareMap.get(Servo.class, "raketop");
        this.bot = hardwareMap.get(Servo.class, "rakebot");
    }

    @Override
    public void init_loop() {
        top.setPosition(.5);
        bot.setPosition(.5);
    }

    @Override
    public void start() {

    }

    @Override
    public void loop() {
        top.setPosition(this.gamepad1.left_stick_y);
        bot.setPosition(this.gamepad1.right_stick_y);

    }

    @Override
    public void stop() {

    }
}
