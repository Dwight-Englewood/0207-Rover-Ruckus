package org.firstinspires.ftc.teamcode.Testing.Sensors;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.teamcode.Hardware.Bot;


@TeleOp(name = "IMUTest", group = "Teleop")
public class IMUTest extends OpMode {

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
        int gyroVal = (int)robot.sensors.getGyroRotation(AngleUnit.DEGREES);
        robot.driveTrain.gyroCorrect(0,1, gyroVal, .05, .4);

        telemetry.addData("heading", gyroVal);
        telemetry.update();
    }

    @Override
    public void stop() {
        robot.stop();
    }
}