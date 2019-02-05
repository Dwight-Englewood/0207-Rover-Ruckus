package org.firstinspires.ftc.teamcode.Testing;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

public class GoldDetectorPipeline {

    public static final double RESIZE_IMAGE_WIDTH = 320.0;
    public static final double RESIZE_IMAGE_HEIGHT = 240.0;
    public static final double BLUR_RADIUS = 8.108108108108109;
    public static final int hsvHueLow = 30;
    public static final int hsvHueHigh = 60;
    public static final int hsvSatLow = 150;
    public static final int hsvSatHigh = 255;
    public static final int hsvValLow = 100;
    public static final int hsvValHigh = 255;
    //Outputs
    private Mat resizeImageOutput = new Mat();
    private Mat cvCvtcolorOutput = new Mat();
    private Mat blurOutput = new Mat();
    private Mat hsvThresholdOutput = new Mat();
    private ArrayList<MatOfPoint> findContoursOutput = new ArrayList<MatOfPoint>();

    public void process(Mat source0) {
        // Step Resize_Image0:
        Mat resizeImageInput = source0;
        double resizeImageWidth = RESIZE_IMAGE_WIDTH;
        double resizeImageHeight = RESIZE_IMAGE_HEIGHT;
        int resizeImageInterpolation = Imgproc.INTER_CUBIC;
        resizeImage(resizeImageInput, resizeImageWidth, resizeImageHeight, resizeImageInterpolation, resizeImageOutput);

        // Step CV_cvtColor0:
        Mat cvCvtcolorSrc = resizeImageOutput;
        int cvCvtcolorCode = Imgproc.COLOR_RGB2HSV;
        cvCvtcolor(cvCvtcolorSrc, cvCvtcolorCode, cvCvtcolorOutput);

        // Step Blur0:
        Mat blurInput = cvCvtcolorOutput;
        BlurType blurType = BlurType.get("Median Filter");
        double blurRadius = BLUR_RADIUS;
        blur(blurInput, blurType, blurRadius, blurOutput);

        // Step HSV_Threshold0:
        Mat hsvThresholdInput = blurOutput;
        double[] hsvThresholdHue = {hsvHueLow, hsvHueHigh};
        double[] hsvThresholdSaturation = {hsvSatLow, hsvSatHigh};
        double[] hsvThresholdValue = {hsvValLow, hsvValHigh};
        hsvThreshold(hsvThresholdInput, hsvThresholdHue, hsvThresholdSaturation, hsvThresholdValue, hsvThresholdOutput);

        // Step Find_Contours0:
        Mat findContoursInput = hsvThresholdOutput;
        boolean findContoursExternalOnly = false;
        findContours(findContoursInput, findContoursExternalOnly, findContoursOutput);

    }

    public Mat resizeImageOutput() {
        return resizeImageOutput;
    }

    public Mat cvCvtcolorOutput() {
        return cvCvtcolorOutput;
    }

    public Mat blurOutput() {
        return blurOutput;
    }

    public Mat hsvThresholdOutput() {
        return hsvThresholdOutput;
    }

    public ArrayList<MatOfPoint> findContoursOutput() {
        return findContoursOutput;
    }

    private void resizeImage(Mat input, double width, double height,
                             int interpolation, Mat output) {
        Imgproc.resize(input, output, new Size(width, height), 0.0, 0.0, interpolation);
    }

    private void cvCvtcolor(Mat src, int code, Mat dst) {
        Imgproc.cvtColor(src, dst, code);
    }

    private void blur(Mat input, BlurType type, double doubleRadius,
                      Mat output) {
        int radius = (int) (doubleRadius + 0.5);
        int kernelSize;
        switch (type) {
            case BOX:
                kernelSize = 2 * radius + 1;
                Imgproc.blur(input, output, new Size(kernelSize, kernelSize));
                break;
            case GAUSSIAN:
                kernelSize = 6 * radius + 1;
                Imgproc.GaussianBlur(input, output, new Size(kernelSize, kernelSize), radius);
                break;
            case MEDIAN:
                kernelSize = 2 * radius + 1;
                Imgproc.medianBlur(input, output, kernelSize);
                break;
            case BILATERAL:
                Imgproc.bilateralFilter(input, output, -1, radius, radius);
                break;
        }
    }

    private void hsvThreshold(Mat input, double[] hue, double[] sat, double[] val,
                              Mat out) {
        Imgproc.cvtColor(input, out, Imgproc.COLOR_BGR2HSV);
        Core.inRange(out, new Scalar(hue[0], sat[0], val[0]),
                new Scalar(hue[1], sat[1], val[1]), out);
    }

    private void findContours(Mat input, boolean externalOnly,
                              List<MatOfPoint> contours) {
        Mat hierarchy = new Mat();
        contours.clear();
        int mode;
        if (externalOnly) {
            mode = Imgproc.RETR_EXTERNAL;
        } else {
            mode = Imgproc.RETR_LIST;
        }
        int method = Imgproc.CHAIN_APPROX_SIMPLE;
        Imgproc.findContours(input, contours, hierarchy, mode, method);
    }

    enum BlurType {
        BOX("Box Blur"), GAUSSIAN("Gaussian Blur"), MEDIAN("Median Filter"),
        BILATERAL("Bilateral Filter");

        private final String label;

        BlurType(String label) {
            this.label = label;
        }

        public static BlurType get(String type) {
            if (BILATERAL.label.equals(type)) {
                return BILATERAL;
            } else if (GAUSSIAN.label.equals(type)) {
                return GAUSSIAN;
            } else if (MEDIAN.label.equals(type)) {
                return MEDIAN;
            } else {
                return BOX;
            }
        }

        @Override
        public String toString() {
            return this.label;
        }
    }


}
