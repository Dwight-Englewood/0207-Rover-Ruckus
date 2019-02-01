package org.firstinspires.ftc.teamcode.Testing;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;


@TeleOp(name = "IntakeTest", group = "Teleop")
public class IntakeTest extends OpMode {

    DcMotor intake = null;
    @Override
    public void init() {
        intake = hardwareMap.get(DcMotor.class, "intake");

    }

    @Override
    public void init_loop() {
    }

    @Override
    public void start() {

    }

    @Override
    public void loop() {
        if (gamepad1.dpad_down) {
            intake.setPower(1);
        } else if (gamepad1.dpad_up) {
            intake.setPower(-1);
        } else {
            intake.setPower(0);
        }

    }

    @Override
    public void stop() {

    }
}
