package org.firstinspires.ftc.teamcode.sandbox.vision;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.teamcode.hardware.sensors.vision.tensorflow.TFWrapper1Gold;

import java.util.List;


@TeleOp(name = "Vumark1GoldTest", group = "Teleop")
@Disabled
public class Vision1GoldTest extends OpMode {

    TFWrapper1Gold tfw1g = new TFWrapper1Gold();
    ElapsedTime slowTimer = new ElapsedTime();
    boolean slow = false;

    @Override
    public void init() {
        tfw1g.init(hardwareMap);
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
        tfw1g.start();
        slowTimer.reset();
    }

    @Override
    public void loop() {
        telemetry.addData("mineralSample", tfw1g.getState().getStateVal());
        List<Recognition> dab = tfw1g.badOOP();

        telemetry.addData("number", dab.size());
        for (int i = 0; i < dab.size(); i++) {
            telemetry.addData("recognition", i);
            telemetry.addData("recognitionX", (int) dab.get(i).getLeft());
            telemetry.addData("recognitionY", dab.get(i).getLeft() - (int) dab.get(i).getLeft());
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
        tfw1g.stop();
    }
}