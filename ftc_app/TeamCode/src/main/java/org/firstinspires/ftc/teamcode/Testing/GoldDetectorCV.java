package org.firstinspires.ftc.teamcode.Testing;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.corningrobotics.enderbots.endercv.CameraViewDisplay;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Rect;
import org.opencv.imgproc.Imgproc;

import java.util.List;
import java.util.Locale;

import static java.lang.Math.sqrt;

@TeleOp(name = "GoldDetectorCV")
public class GoldDetectorCV extends OpMode {

    private GoldDetectorWrapper goldDetector;

    private EditGroup currentEditGroup = EditGroup.NONE;

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
        if (gamepad1.dpad_up) {
            goldDetector.imageView = GoldDetectorWrapper.ImageView.RGB;
        } else if (gamepad1.dpad_right) {
            goldDetector.imageView = GoldDetectorWrapper.ImageView.HSV;
        } else if (gamepad1.dpad_down) {
            goldDetector.imageView = GoldDetectorWrapper.ImageView.BLUR;
        } else if (gamepad1.dpad_left) {
            goldDetector.imageView = GoldDetectorWrapper.ImageView.CONTOUR;
        } else if (gamepad1.a) {
            goldDetector.imageView = GoldDetectorWrapper.ImageView.HULL;
        } else if (gamepad1.b) {
            goldDetector.imageView = GoldDetectorWrapper.ImageView.DEFAULT;
        }

        if (gamepad2.left_bumper) {
            currentEditGroup = EditGroup.NONE;
        } else if (gamepad2.dpad_up) {
            currentEditGroup = EditGroup.HUE;
        } else if (gamepad2.dpad_left) {
            currentEditGroup = EditGroup.SAT;
        } else if (gamepad2.dpad_down && !gamepad2.right_bumper) {
            currentEditGroup = EditGroup.VAL;
        } else if (gamepad2.dpad_right && gamepad2.right_bumper) {
            currentEditGroup = EditGroup.BLUR1;
        } else if (gamepad2.dpad_right && !gamepad2.right_bumper) {
            currentEditGroup = EditGroup.BLUR2;
        } else if (gamepad2.dpad_down && gamepad2.right_bumper) {
            currentEditGroup = EditGroup.HSV;
        }

        telemetry.addData("editGroup", currentEditGroup);

        if (currentEditGroup != EditGroup.HSV && gamepad2.left_trigger < .5) {
            switch (currentEditGroup) {
                case NONE:
                    break;
                case HUE:
                    goldDetector.grip.hsvHueLow = Math.abs(((gamepad2.left_stick_y + 1) / 2) * 255);
                    telemetry.addData("hsvHueLow", goldDetector.grip.hsvHueLow);
                    goldDetector.grip.hsvHueHigh = Math.abs(((gamepad2.right_stick_y + 1) / 2) * 255);
                    telemetry.addData("hsvHueHigh", goldDetector.grip.hsvHueHigh);
                    break;
                case SAT:
                    goldDetector.grip.hsvSatLow = Math.abs(((gamepad2.left_stick_y + 1) / 2) * 255);
                    telemetry.addData("hsvSatLow", goldDetector.grip.hsvSatLow);
                    goldDetector.grip.hsvSatHigh = Math.abs(((gamepad2.right_stick_y + 1) / 2) * 255);
                    telemetry.addData("hsvSatHigh", goldDetector.grip.hsvSatHigh);

                    break;
                case VAL:
                    goldDetector.grip.hsvValLow = Math.abs(((gamepad2.left_stick_y + 1) / 2) * 255);
                    telemetry.addData("hsvValLow", goldDetector.grip.hsvValLow);
                    goldDetector.grip.hsvValHigh = Math.abs(((gamepad2.right_stick_y + 1) / 2) * 255);
                    telemetry.addData("hsvValHigh", goldDetector.grip.hsvValHigh);
                    break;
                case BLUR1:
                    goldDetector.grip.blurRadius1 = Math.abs(gamepad2.right_stick_y * 100);
                    telemetry.addData("blurRadius1", goldDetector.grip.blurRadius1);
                    break;
                case BLUR2:
                    goldDetector.grip.blurRadius2 = Math.abs(gamepad2.right_stick_y * 100);
                    telemetry.addData("blurRadius2", goldDetector.grip.blurRadius2);

            }
        } else if (currentEditGroup == EditGroup.HSV && !gamepad2.right_bumper) {
            double u = gamepad2.left_stick_x;
            double v = -gamepad2.left_stick_y;
            double u2 = u * u;
            double v2 = v * v;
            double twosqrt2 = 2.0 * sqrt(2.0);
            double subtermx = 2.0 + u2 - v2;
            double subtermy = 2.0 - u2 + v2;
            double termx1 = subtermx + u * twosqrt2;
            double termx2 = subtermx - u * twosqrt2;
            double termy1 = subtermy + v * twosqrt2;
            double termy2 = subtermy - v * twosqrt2;
            double x1 = 0.5 * sqrt(termx1) - 0.5 * sqrt(termx2);
            double y1 = 0.5 * sqrt(termy1) - 0.5 * sqrt(termy2);
            goldDetector.grip.hsvHueLow = Math.abs(((x1 + 1) / 2) * 255);
            telemetry.addData("hsvHueLow", goldDetector.grip.hsvHueLow);
            goldDetector.grip.hsvHueHigh = Math.abs(((y1 + 1) / 2) * 255);
            telemetry.addData("hsvHueHigh", goldDetector.grip.hsvHueHigh);
            u = gamepad2.left_stick_x;
            v = -gamepad2.left_stick_y;
            u2 = u * u;
            v2 = v * v;
            twosqrt2 = 2.0 * sqrt(2.0);
            subtermx = 2.0 + u2 - v2;
            subtermy = 2.0 - u2 + v2;
            termx1 = subtermx + u * twosqrt2;
            termx2 = subtermx - u * twosqrt2;
            termy1 = subtermy + v * twosqrt2;
            termy2 = subtermy - v * twosqrt2;
            x1 = 0.5 * sqrt(termx1) - 0.5 * sqrt(termx2);
            y1 = 0.5 * sqrt(termy1) - 0.5 * sqrt(termy2);
            goldDetector.grip.hsvSatLow = Math.abs(((x1 + 1) / 2) * 255);
            telemetry.addData("hsvSatLow", goldDetector.grip.hsvSatLow);
            goldDetector.grip.hsvSatHigh = Math.abs(((y1 + 1) / 2) * 255);
            telemetry.addData("hsvSatHigh", goldDetector.grip.hsvSatHigh);
            goldDetector.grip.hsvValLow = Math.abs(((gamepad2.left_trigger) / 2) * 255);
            telemetry.addData("hsvValLow", goldDetector.grip.hsvValLow);
            goldDetector.grip.hsvValHigh = Math.abs(((gamepad2.right_trigger) / 2) * 255);
            telemetry.addData("hsvValHigh", goldDetector.grip.hsvValHigh);


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
        HUE, SAT, VAL, BLUR1, BLUR2, NONE, HSV;
    }
}