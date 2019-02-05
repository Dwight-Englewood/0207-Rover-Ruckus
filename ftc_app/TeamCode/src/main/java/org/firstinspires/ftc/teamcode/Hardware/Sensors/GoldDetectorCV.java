package org.firstinspires.ftc.teamcode.Hardware.Sensors;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.corningrobotics.enderbots.endercv.CameraViewDisplay;
import org.firstinspires.ftc.teamcode.Hardware.State;
import org.firstinspires.ftc.teamcode.Hardware.Subsystem;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Rect;
import org.opencv.imgproc.Imgproc;

import java.util.List;

@TeleOp(name = "GoldDetectorTeleop")
public class GoldDetectorCV implements Subsystem {

    public static final int minContourArea = 1000;
    public static final int maxContourArea = 5000;

    private MineralPosition currentState;

    public GoldDetectorWrapper goldDetector;

    public void init(HardwareMap hwMap) {
        goldDetector = new GoldDetectorWrapper();
        // can replace with ActivityViewDisplay.getInstance() for fullscreen
        goldDetector.init(hwMap.appContext, CameraViewDisplay.getInstance());
        // start the vision system
    }

    @Override
    public void start() {
        goldDetector.enable();
    }

    @Override
    public void reset() {

    }

    public void stop() {
        // stop the vision system
        goldDetector.disable();
    }

    private void updateState() {
        List<MatOfPoint> contours = goldDetector.getContours();

        if (contours.size() == 0) {
            this.currentState = MineralPosition.RIGHT;
        } else if (contours.size() == 1) {
            try {
                Rect boundingRect = Imgproc.boundingRect(contours.get(0));
                //telemetry.addData("x", boundingRect.x + boundingRect.width / 2);
                //telemetry.addData("y", boundingRect.y + boundingRect.height / 2);
                if (boundingRect.x + boundingRect.width / 2 > 160) {
                    this.currentState = MineralPosition.CENTER;
                } else {
                    this.currentState = MineralPosition.LEFT;
                }
            } catch (IndexOutOfBoundsException e) {
            }
        } else {
            this.currentState = MineralPosition.NOTVISIBLE;
        }
    }

    @Override
    public State getState() {
        this.updateState();
        return this.currentState;
    }

    public List<MatOfPoint> filterContours(List<MatOfPoint> contours) {
        for (int i = 0; i < contours.size(); i++) {
            Rect boundingRect = Imgproc.boundingRect(contours.get(0));
            if (!(boundingRect.area() > minContourArea) || !(boundingRect.area() < maxContourArea)) {
                contours.remove(i);
                i--;
            }
        }

        return contours;

    }
}