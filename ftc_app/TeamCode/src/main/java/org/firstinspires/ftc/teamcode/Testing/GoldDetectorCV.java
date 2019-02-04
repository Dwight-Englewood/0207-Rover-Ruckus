package org.firstinspires.ftc.teamcode.Testing;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.corningrobotics.enderbots.endercv.CameraViewDisplay;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Rect;
import org.opencv.imgproc.Imgproc;

import java.util.List;
import java.util.Locale;

@TeleOp(name = "GoldDetectorCV")
public class GoldDetectorCV extends OpMode {

    private GoldDetectorWrapper goldDetector;

    private EditGroup currentEditGroup;

    @Override
    public void init() {
        goldDetector = new GoldDetectorWrapper();
        // can replace with ActivityViewDisplay.getInstance() for fullscreen
        goldDetector.init(hardwareMap.appContext, CameraViewDisplay.getInstance());
        // start the vision system
        goldDetector.enable();
    }

    @Override
    public void loop() {
        if (gamepad2.dpad_up) {
            goldDetector.imageView = GoldDetectorWrapper.ImageView.RGB;
        } else if (gamepad1.dpad_right) {
            goldDetector.imageView = GoldDetectorWrapper.ImageView.HSV;
        } else if (gamepad1.dpad_down) {
            goldDetector.imageView = GoldDetectorWrapper.ImageView.BLUR;
        } else if (gamepad1.dpad_left) {
            goldDetector.imageView = GoldDetectorWrapper.ImageView.CONTOUR;
        } else if (gamepad1.a) {
            goldDetector.imageView = GoldDetectorWrapper.ImageView.HULL;
        }

        if (gamepad2.dpad_left && gamepad2.left_bumper) {
            currentEditGroup = EditGroup.NONE;
        } else if (gamepad2.dpad_up) {
            currentEditGroup = EditGroup.HUE;
        } else if (gamepad2.dpad_left) {
            currentEditGroup = EditGroup.SAT;
        } else if (gamepad2.dpad_down) {
            currentEditGroup = EditGroup.VAL;
        } else if (gamepad2.dpad_right && gamepad2.left_bumper) {
            currentEditGroup = EditGroup.BLUR1;
        } else if (gamepad2.dpad_right && !gamepad2.left_bumper) {
            currentEditGroup = EditGroup.BLUR2;
        }
        if (gamepad2.left_trigger < .5) {
            switch (currentEditGroup) {
                case NONE:
                    break;
                case HUE:
                    goldDetector.grip.hsvHueLow = Math.abs(((gamepad2.left_stick_y + 1) / 2) * 255);
                    telemetry.addData("hsvHueLow", goldDetector.grip.hsvHueLow);
                    goldDetector.grip.hsvHueHigh = Math.abs(((gamepad2.right_stick_y + 1) / 2) * 255);
                    telemetry.addData("hsvHueHigh", goldDetector.grip.hsvHueHigh);
                case SAT:
                    goldDetector.grip.hsvSatLow = Math.abs(((gamepad2.left_stick_y + 1) / 2) * 255);
                    telemetry.addData("hsvSatLow", goldDetector.grip.hsvSatLow);
                    goldDetector.grip.hsvSatHigh = Math.abs(((gamepad2.right_stick_y + 1) / 2) * 255);
                    telemetry.addData("hsvSatHigh", goldDetector.grip.hsvSatHigh);
                case VAL:
                    goldDetector.grip.hsvValLow = Math.abs(((gamepad2.left_stick_y + 1) / 2) * 255);
                    telemetry.addData("hsvValLow", goldDetector.grip.hsvValLow);
                    goldDetector.grip.hsvValHigh = Math.abs(((gamepad2.right_stick_y + 1) / 2) * 255);
                    telemetry.addData("hsvValHigh", goldDetector.grip.hsvValHigh);
                case BLUR1:
                    goldDetector.grip.blurRadius1 = Math.abs(gamepad2.right_stick_y * 100);
                    telemetry.addData("blurRadius1", goldDetector.grip.blurRadius1);
                case BLUR2:
                    goldDetector.grip.blurRadius2 = Math.abs(gamepad2.right_stick_y * 100);
                    telemetry.addData("blurRadius2", goldDetector.grip.blurRadius2);

            }
        }

        if (gamepad1.left_trigger > .5) {
            telemetry.addData("hsvhueLow", goldDetector.grip.hsvHueLow);
            telemetry.addData("hsvHueHigh", goldDetector.grip.hsvHueHigh);
            telemetry.addData("hsvSatLow", goldDetector.grip.hsvSatLow);
            telemetry.addData("hsvSatHigh", goldDetector.grip.hsvSatHigh);
            telemetry.addData("hsvValLow", goldDetector.grip.hsvValLow);
            telemetry.addData("hsvValHigh", goldDetector.grip.hsvValHigh);
            telemetry.addData("blurRadius1", goldDetector.grip.blurRadius1);
            telemetry.addData("blurRadius2", goldDetector.grip.blurRadius2);

        }


        // update the settings of the vision pipeline

        // get a list of contours from the vision system
        if (gamepad1.right_trigger > .5) {
            List<MatOfPoint> contours = goldDetector.getContours();
            for (int i = 0; i < contours.size(); i++) {
                // get the bounding rectangle of a single contour, we use it to get the x/y center
                // yes there's a mass center using Imgproc.moments but w/e
                Rect boundingRect = Imgproc.boundingRect(contours.get(i));
                telemetry.addData("contour" + Integer.toString(i),
                        String.format(Locale.getDefault(), "(%d, %d)", (boundingRect.x + boundingRect.width) / 2, (boundingRect.y + boundingRect.height) / 2));
            }
        }
    }

    public void stop() {
        // stop the vision system
        goldDetector.disable();
    }

    public enum EditGroup {
        HUE, SAT, VAL, BLUR1, BLUR2, NONE;
    }
}