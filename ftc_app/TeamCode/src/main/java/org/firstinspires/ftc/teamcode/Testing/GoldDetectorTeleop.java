package org.firstinspires.ftc.teamcode.Testing;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Hardware.Sensors.GoldDetectorWrapper;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Rect;
import org.opencv.imgproc.Imgproc;

import java.util.List;
import java.util.Locale;

@TeleOp(name = "GoldDetectorTeleop")
public class GoldDetectorTeleop extends OpMode {

    public static final int minContourArea = 1000;
    public static final int maxContourArea = 5000;

    private GoldDetectorWrapper goldDetector = new GoldDetectorWrapper();

    @Override
    public void init() {
        goldDetector.init(hardwareMap);
    }

    @Override
    public void start() {
        goldDetector.start();
    }

    @Override
    public void loop() {
        if (gamepad1.dpad_up) {
            goldDetector.imageView = GoldDetectorWrapper.ImageView.RESIZE;
        } else if (gamepad1.dpad_right) {
            goldDetector.imageView = GoldDetectorWrapper.ImageView.HSV;
        } else if (gamepad1.dpad_down) {
            goldDetector.imageView = GoldDetectorWrapper.ImageView.BLUR;
        } else if (gamepad1.dpad_left) {
            goldDetector.imageView = GoldDetectorWrapper.ImageView.THRESH;
        } else if (gamepad1.a) {
            goldDetector.imageView = GoldDetectorWrapper.ImageView.CONTOUR;
        } else if (gamepad1.b) {
            goldDetector.imageView = GoldDetectorWrapper.ImageView.DEFAULT;
        }

        telemetry.addData("Current View", goldDetector.imageView);

        // update the settings of the vision pipeline
        List<MatOfPoint> contours = goldDetector.getContours();


        // get a list of contours from the vision system
        if (gamepad1.right_trigger > .5) {
            for (int i = 0; i < contours.size(); i++) {
                // get the bounding rectangle of a single contour, we use it to get the x/y center
                // yes there's a mass center using Imgproc.moments but w/e
                Rect boundingRect = Imgproc.boundingRect(contours.get(i));
                telemetry.addData("contour" + Integer.toString(i), boundingRect.area());
                telemetry.addData("contour" + Integer.toString(i),
                        String.format(Locale.getDefault(), "(%d, %d)", (boundingRect.x + boundingRect.width) / 2, (boundingRect.y + boundingRect.height) / 2));
            }
        } else {
            telemetry.addData("State", goldDetector.getState());
        }
    }

    public void stop() {
        // stop the vision system
        goldDetector.stop();
    }
    
}