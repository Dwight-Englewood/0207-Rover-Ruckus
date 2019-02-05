package org.firstinspires.ftc.teamcode.Testing;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfInt;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;


public class GoldDetectorPipeline {

    public double hsvHueLow = 78;
    public double hsvHueHigh = 117;
    public double hsvSatLow = 149;
    public double hsvSatHigh = 255;
    public double hsvValLow = 211;
    public double hsvValHigh = 255;
    public double blurRadius1 = 100.0;
    public double blurRadius2 = 0;


    private Mat cvCvtcolorOutput = new Mat();
    private Mat blurHSVOutput = new Mat();
    private Mat hsvThresholdOutput = new Mat();
    private Mat blurThresholdOutput = new Mat();
    private ArrayList<MatOfPoint> findContoursOutput = new ArrayList<MatOfPoint>();
    private ArrayList<MatOfPoint> convexHullsOutput = new ArrayList<MatOfPoint>();

    public Mat getBlurHSVOutput() {
        return blurHSVOutput;
    }

    /**
     * This is the primary method that runs the entire pipeline and updates the outputs.
     */
    public void process(Mat source0) {
        BlurType blurType = BlurType.get("Box Blur");

        Mat cvCvtcolorSrc = source0;
        int cvCvtcolorCode = Imgproc.COLOR_RGB2HSV;
        cvCvtcolor(cvCvtcolorSrc, cvCvtcolorCode, cvCvtcolorOutput);

        Mat blur1Input = cvCvtcolorOutput;
        blur(blur1Input, blurType, blurRadius1, blurHSVOutput);

        Mat hsvThresholdInput = blurHSVOutput;
        double[] hsvThresholdHue = {hsvHueLow, hsvHueHigh};
        double[] hsvThresholdSaturation = {hsvSatLow, hsvSatHigh};
        double[] hsvThresholdValue = {hsvValLow, hsvValHigh};
        hsvThreshold(hsvThresholdInput, hsvThresholdHue, hsvThresholdSaturation, hsvThresholdValue, hsvThresholdOutput);

        Mat blurInput = hsvThresholdOutput;
        blur(blurInput, blurType, blurRadius2, blurThresholdOutput);

        Mat findContoursInput = blurThresholdOutput;
        boolean findContoursExternalOnly = false;
        findContours(findContoursInput, findContoursExternalOnly, findContoursOutput);

        ArrayList<MatOfPoint> convexHullsContours = findContoursOutput;
        convexHulls(convexHullsContours, convexHullsOutput);

    }

    public Mat cvCvtcolorOutput() {
        return cvCvtcolorOutput;
    }


    public Mat hsvThresholdOutput() {
        return hsvThresholdOutput;
    }

    public Mat blurOutput() {
        return blurThresholdOutput;
    }


    public ArrayList<MatOfPoint> findContoursOutput() {
        return findContoursOutput;
    }


    public ArrayList<MatOfPoint> convexHullsOutput() {
        return convexHullsOutput;
    }


    private void cvCvtcolor(Mat src, int code, Mat dst) {
        Imgproc.cvtColor(src, dst, code);
    }


    private void hsvThreshold(Mat input, double[] hue, double[] sat, double[] val,
                              Mat out) {
        Imgproc.cvtColor(input, out, Imgproc.COLOR_BGR2HSV);
        Core.inRange(out, new Scalar(hue[0], sat[0], val[0]),
                new Scalar(hue[1], sat[1], val[1]), out);
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


    private void convexHulls(List<MatOfPoint> inputContours,
                             ArrayList<MatOfPoint> outputContours) {
        final MatOfInt hull = new MatOfInt();
        outputContours.clear();
        for (int i = 0; i < inputContours.size(); i++) {
            final MatOfPoint contour = inputContours.get(i);
            final MatOfPoint mopHull = new MatOfPoint();
            Imgproc.convexHull(contour, hull);
            mopHull.create((int) hull.size().height, 1, CvType.CV_32SC2);
            for (int j = 0; j < hull.size().height; j++) {
                int index = (int) hull.get(j, 0)[0];
                double[] point = new double[]{contour.get(index, 0)[0], contour.get(index, 0)[1]};
                mopHull.put(j, 0, point);
            }
            outputContours.add(mopHull);
        }
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

