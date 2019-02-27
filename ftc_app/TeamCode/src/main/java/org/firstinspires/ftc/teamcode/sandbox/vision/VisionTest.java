package org.firstinspires.ftc.teamcode.sandbox.vision;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.teamcode.hardware.bots.Bot;

import java.util.List;


@TeleOp(name = "VumarkTest", group = "Teleop")
@Disabled
public class VisionTest extends OpMode {

    Bot boot = new Bot(false, false);

    ElapsedTime slowTimer = new ElapsedTime();
    boolean slow = false;

    @Override
    public void init() {
        boot.tensorFlow.init(hardwareMap);
        //boot.vumarkWrapper.init(hardwareMap);
    }

    @Override
    public void init_loop() {
            telemetry.addLine("Ready.");
            telemetry.update();
    }

    @Override
    public void start() {
        //boot.vumarkWrapper.start();
        boot.tensorFlow.start();
        slowTimer.reset();
    }

    @Override
    public void loop() {
        telemetry.addData("mineralSample", boot.tensorFlow.getState().getStateVal());
        List<Recognition> dab = boot.tensorFlow.badOOP();

        telemetry.addData("number", dab);
        for (int i = 0; i < dab.size(); i++) {
            telemetry.addData("recognition", i);
            telemetry.addData("recognitionX", (int) dab.get(i).getLeft());
            telemetry.addData("recognitionY", dab.get(i).getLeft()-(int)dab.get(i).getLeft());
            telemetry.addData("area", dab.get(i).getWidth() * dab.get(i).getHeight());
            telemetry.addData("recognition", dab.get(i).getLabel());

        }
        /*telemetry.addData("vumark", boot.vumarkWrapper.getState().getStateVal());
        double[] pos = boot.vumarkWrapper.getPosition();
        telemetry.addData("posX", pos[0]);
        telemetry.addData("posY", pos[1]);
        telemetry.addData("posZ", pos[2]);
        double[] rot = boot.vumarkWrapper.getOrientation();
        telemetry.addData("pitch", rot[0]);
        telemetry.addData("roll", rot[1]);
        telemetry.addData("heading", rot[2]);*/


    }

    @Override
    public void stop() {
        boot.tensorFlow.stop();
    }
}