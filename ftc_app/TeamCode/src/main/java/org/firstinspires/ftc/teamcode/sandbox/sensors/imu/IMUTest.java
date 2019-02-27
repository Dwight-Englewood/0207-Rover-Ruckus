package org.firstinspires.ftc.teamcode.sandbox.sensors.imu;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.hardware.bots.Bot;


@TeleOp(name = "IMUTest", group = "Teleop")
@Disabled
public class IMUTest extends OpMode {

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
        int gyroVal = (int)robot.sensorSystem.getGyroRotation(AngleUnit.DEGREES);
        robot.driveTrain.gyroCorrect(0,1, gyroVal, .05, .4);

        telemetry.addData("heading", gyroVal);
        telemetry.update();
    }

    @Override
    public void stop() {
        robot.stop();
    }
}