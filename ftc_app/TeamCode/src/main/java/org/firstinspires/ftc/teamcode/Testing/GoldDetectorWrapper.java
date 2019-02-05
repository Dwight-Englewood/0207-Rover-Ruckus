package org.firstinspires.ftc.teamcode.Testing;

import org.corningrobotics.enderbots.endercv.OpenCVPipeline;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;


public class GoldDetectorWrapper extends OpenCVPipeline {
    public ImageView imageView = ImageView.THRESH;
    public GoldDetectorPipeline grip = new GoldDetectorPipeline();
    // To keep it such that we don't have to instantiate a new Mat every call to processFrame,
    // we declare the Mats up here and reuse them. This is easier on the garbage collector.
    private Mat hsv = new Mat();
    private Mat thresholded = new Mat();
    // this is just here so we can expose it later thru getContours.
    private List<MatOfPoint> contours = new ArrayList<>();

    public synchronized List<MatOfPoint> getContours() {
        return contours;
    }

    // This is called every camera frame.
    @Override
    public Mat processFrame(Mat rgba, Mat gray) {
        grip.process(rgba);
        contours = grip.findContoursOutput();
        switch (imageView) {
            case RESIZE:
                return grip.resizeImageOutput();
            case HSV:
                return grip.cvCvtcolorOutput();
            case BLUR:
                return grip.blurOutput();
            case THRESH:
                return grip.hsvThresholdOutput();
            case CONTOUR:
                Mat contourImageOutput = grip.resizeImageOutput().clone();
                Imgproc.drawContours(contourImageOutput, grip.findContoursOutput(), -1, new Scalar(255, 255, 255), 8);
                return contourImageOutput;
            default:
                return rgba;
        }

    }

    public enum ImageView {
        RESIZE, HSV, BLUR, THRESH, CONTOUR, DEFAULT;
    }
}