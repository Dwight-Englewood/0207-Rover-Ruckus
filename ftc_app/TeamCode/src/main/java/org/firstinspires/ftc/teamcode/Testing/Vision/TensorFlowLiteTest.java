package org.firstinspires.ftc.teamcode.Testing.Vision;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.teamcode.Hardware.Bot;

import java.util.List;


@TeleOp(name = "TFLTest", group = "Teleop")
public class TensorFlowLiteTest extends OpMode {

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
        boot.tensorFlow.start();
        slowTimer.reset();
    }

    @Override
    public void loop() {
        //vmw.updateState();
        telemetry.addData("mineralSample", boot.tensorFlow.getState().getStateVal());
        List<Recognition> dab = boot.tensorFlow.badOOP();
        if (dab == null) {

        } else {
            for (int i = 0; i < dab.size(); i++) {
                Recognition e = dab.get(i);
                telemetry.addData("angle", e.estimateAngleToObject(AngleUnit.DEGREES));
                telemetry.addData(Integer.toString(i) + ": " + e.getLabel(), e.getConfidence());
            }
        }


    }

    @Override
    public void stop() {
        boot.tensorFlow.stop();
    }
}