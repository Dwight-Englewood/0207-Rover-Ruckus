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
        top.setPosition(0);
        bot.setPosition(.9);
    }

    @Override
    public void start() {

    }

    @Override
    public void loop() {
        if (gamepad1.a) {
            top.setPosition(.8);
        } else if (gamepad1.b) {
            top.setPosition(0);
        } else if (gamepad1.x) {
            bot.setPosition(.9);
        } else if (gamepad1.y) {
            bot.setPosition(0.6);


        }

    }

    @Override
    public void stop() {

    }
}
