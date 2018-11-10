package org.firstinspires.ftc.teamcode.Testing.Vision;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Hardware.Bot;
import org.firstinspires.ftc.teamcode.Hardware.Sensors.VumarkWrapper;


@TeleOp(name = "VisionTest", group = "Teleop")
public class VisionTest extends OpMode {

    VumarkWrapper vmw = new VumarkWrapper();
    BNO055IMU imu;
    BNO055IMU.Parameters parameters;

    ElapsedTime slowTimer = new ElapsedTime();
    boolean slow = false;

    @Override
    public void init() {
        vmw.init(hardwareMap);
    }

    @Override
    public void init_loop() {
            telemetry.addLine("Ready.");
            telemetry.update();
    }

    @Override
    public void start() {
        vmw.start();
        slowTimer.reset();
    }

    @Override
    public void loop() {
        vmw.updateState();
        telemetry.addData("dab", vmw.getState().vumarkName.toString());
        telemetry.addData("dab", vmw.getState().visible);

    }

    @Override
    public void stop() {
        vmw.stop();
    }
}