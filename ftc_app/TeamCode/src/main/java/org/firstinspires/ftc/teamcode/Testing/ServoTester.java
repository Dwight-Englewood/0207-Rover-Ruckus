package org.firstinspires.ftc.teamcode.Testing;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.CRServoImplEx;


@TeleOp(name = "ServoTester", group = "Teleop")
public class ServoTester extends OpMode {

    CRServo servo;
    @Override
    public void init() {
        servo = hardwareMap.get(CRServo.class, "servo");
    }

    @Override
    public void init_loop() {
    }

    @Override
    public void start() {
        if (gamepad1.y) {
            servo.setPower(1);
            telemetry.addData("rip up", 0);
        } else if (gamepad1.a) {
            servo.setPower(-1);


            telemetry.addData("rip down", 0);
        } else {
            servo.setPower(0);

            telemetry.addData("no rip", 0);
        }
        telemetry.addData(servo.getConnectionInfo(), 0);

    }

    @Override
    public void loop() {

    }

    @Override
    public void stop() {

    }
}
