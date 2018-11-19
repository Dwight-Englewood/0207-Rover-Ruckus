package org.firstinspires.ftc.teamcode.Testing.Vision;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Hardware.Bot;
import org.firstinspires.ftc.teamcode.Hardware.Sensors.VumarkWrapper;


@TeleOp(name = "VisionTest", group = "Teleop")
public class VisionTest extends OpMode {

    Bot boot = new Bot(false);

    ElapsedTime slowTimer = new ElapsedTime();
    boolean slow = false;

    @Override
    public void init() {
        boot.init(hardwareMap);
    }

    @Override
    public void init_loop() {
            telemetry.addLine("Ready.");
            telemetry.update();
    }

    @Override
    public void start() {
        boot.start();
        slowTimer.reset();
    }

    @Override
    public void loop() {
        //vmw.updateState();
        telemetry.addData("vumark", boot.vumarkWrapper.getState().getStateVal());
        double[] pos = boot.vumarkWrapper.getPosition();
        telemetry.addData("posX", pos[0]);
        telemetry.addData("posY", pos[1]);
        telemetry.addData("posZ", pos[2]);
        double[] rot = boot.vumarkWrapper.getOrientation();
        telemetry.addData("pitch", rot[0]);
        telemetry.addData("roll", rot[1]);
        telemetry.addData("heading", rot[2]);


    }

    @Override
    public void stop() {
        boot.stop();
    }
}