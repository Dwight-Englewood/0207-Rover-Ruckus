package org.firstinspires.ftc.teamcode.Testing;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.corningrobotics.enderbots.endercv.CameraViewDisplay;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Rect;
import org.opencv.imgproc.Imgproc;

import java.util.List;
import java.util.Locale;

@TeleOp(name="Example: Blue Vision Demo")
public class ExampleBlueVisionDemo extends OpMode {
    private ExampleBlueVision blueVision;
    @Override
    public void init() {
        blueVision = new ExampleBlueVision();
        // can replace with ActivityViewDisplay.getInstance() for fullscreen
        blueVision.init(hardwareMap.appContext, CameraViewDisplay.getInstance());
        blueVision.setShowCountours(false);
        // start the vision system
        blueVision.enable();
    }

    @Override
    public void loop() {
        // update the settings of the vision pipeline
        blueVision.setShowCountours(gamepad1.x);

        // get a list of contours from the vision system
        List<MatOfPoint> contours = blueVision.getContours();
        for (int i = 0; i < contours.size(); i++) {
            // get the bounding rectangle of a single contour, we use it to get the x/y center
            // yes there's a mass center using Imgproc.moments but w/e
            Rect boundingRect = Imgproc.boundingRect(contours.get(i));
            telemetry.addData("contour" + Integer.toString(i),
                    String.format(Locale.getDefault(), "(%d, %d)", (boundingRect.x + boundingRect.width) / 2, (boundingRect.y + boundingRect.height) / 2));
        }
    }

    public void stop() {
        // stop the vision system
        blueVision.disable();
    }
}
