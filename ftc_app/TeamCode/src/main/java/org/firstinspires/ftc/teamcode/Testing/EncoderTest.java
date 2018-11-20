package org.firstinspires.ftc.teamcode.Testing;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Hardware.Bot;


@TeleOp(name = "EncoderTest", group = "Teleop")
public class EncoderTest extends OpMode {
    Bot boot = new Bot(false);

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
        telemetry.addData("bl encoder", boot.driveTrain.bl.getCurrentPosition());
        telemetry.addData("br encoder", boot.driveTrain.br.getCurrentPosition());
        telemetry.addData("fl encoder", boot.driveTrain.fl.getCurrentPosition());
        telemetry.addData("fr encoder", boot.driveTrain.fr.getCurrentPosition());
        telemetry.addData("lift encoder", boot.lift.getTicks());
    }

    @Override
    public void stop() {
        boot.stop();

    }
}
