package org.firstinspires.ftc.teamcode.Testing;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Hardware.Bot;
import org.firstinspires.ftc.teamcode.Hardware.RebuildBot;


@TeleOp(name = "EncoderTestRebuild", group = "Testing")
public class EncoderTestRebuild extends OpMode {
    RebuildBot boot = new RebuildBot(false);

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
    }

    @Override
    public void loop() {

        boot.driveTrain.fl.setPower(gamepad1.left_stick_y);
        boot.driveTrain.fr.setPower(gamepad1.right_stick_y);
        boot.driveTrain.bl.setPower(gamepad1.left_stick_x);
        boot.driveTrain.br.setPower(gamepad1.right_stick_x);

        telemetry.addData("bl encoder", boot.driveTrain.bl.getCurrentPosition());
        telemetry.addData("br encoder", boot.driveTrain.br.getCurrentPosition());
        telemetry.addData("fl encoder", boot.driveTrain.fl.getCurrentPosition());
        telemetry.addData("fr encoder", boot.driveTrain.fr.getCurrentPosition());
    }

    @Override
    public void stop() {
        boot.stop();

    }
}
