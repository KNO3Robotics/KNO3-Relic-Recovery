package net.kno3.season.relicrecovery.nerva.program.auto;

import android.util.Log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import org.opencv.core.*;
import org.opencv.core.Core.*;
import org.opencv.features2d.FeatureDetector;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.*;
import org.opencv.objdetect.*;

/**
 * JewelDetector class.
 *
 * <p>An OpenCV pipeline generated by GRIP.
 *
 * @author GRIP
 */
public class JewelDetector {

    //Outputs
    private Mat blurOutput = new Mat();
    private Mat hsvThreshold0Output = new Mat();
    private ArrayList<MatOfPoint> findContours0Output = new ArrayList<MatOfPoint>();
    private ArrayList<MatOfPoint> filterContours0Output = new ArrayList<MatOfPoint>();
    private Mat hsvThreshold1Output = new Mat();
    private ArrayList<MatOfPoint> findContours1Output = new ArrayList<MatOfPoint>();
    private ArrayList<MatOfPoint> filterContours1Output = new ArrayList<MatOfPoint>();

    static {
        System.loadLibrary("opencv_java3");
    }

    /**
     * This is the primary method that runs the entire pipeline and updates the outputs.
     */
    public void process(Mat source0) {

        //Log.i("vif", "j detec 1");
        // Step Blur0:
        Mat blurInput = source0;
        BlurType blurType = BlurType.get("Box Blur");
        double blurRadius = 31.53153153153153;
        blur(blurInput, blurType, blurRadius, blurOutput);

        //Log.i("vif", "j detec 2");
        // Step HSV_Threshold0:
        Mat hsvThreshold0Input = blurOutput;
        double[] hsvThreshold0Hue = {58.27338129496402, 109.71137521222413};
        double[] hsvThreshold0Saturation = {107.77877697841726, 255.0};
        double[] hsvThreshold0Value = {43.57014388489208, 255.0};
        hsvThreshold(hsvThreshold0Input, hsvThreshold0Hue, hsvThreshold0Saturation, hsvThreshold0Value, hsvThreshold0Output);

        //Log.i("vif", "j detec 3");
        // Step Find_Contours0:
        Mat findContours0Input = hsvThreshold0Output;
        boolean findContours0ExternalOnly = true;
        findContours(findContours0Input, findContours0ExternalOnly, findContours0Output);

        //Log.i("vif", "j detec 4");
        // Step Filter_Contours0:
        ArrayList<MatOfPoint> filterContours0Contours = findContours0Output;
        double filterContours0MinArea = 10000.0;
        double filterContours0MinPerimeter = 0.0;
        double filterContours0MinWidth = 0.0;
        double filterContours0MaxWidth = 1000.0;
        double filterContours0MinHeight = 0.0;
        double filterContours0MaxHeight = 1000.0;
        double[] filterContours0Solidity = {0, 100};
        double filterContours0MaxVertices = 1000000.0;
        double filterContours0MinVertices = 0.0;
        double filterContours0MinRatio = 0.0;
        double filterContours0MaxRatio = 1000.0;
        filterContours(filterContours0Contours, filterContours0MinArea, filterContours0MinPerimeter, filterContours0MinWidth, filterContours0MaxWidth, filterContours0MinHeight, filterContours0MaxHeight, filterContours0Solidity, filterContours0MaxVertices, filterContours0MinVertices, filterContours0MinRatio, filterContours0MaxRatio, filterContours0Output);

        //Log.i("vif", "j detec 5");
        // Step HSV_Threshold1:
        Mat hsvThreshold1Input = blurOutput;
        double[] hsvThreshold1Hue = {140.8273381294964, 180.0};
        double[] hsvThreshold1Saturation = {105.48561151079136, 255.0};
        double[] hsvThreshold1Value = {43.57014388489208, 255.0};
        hsvThreshold(hsvThreshold1Input, hsvThreshold1Hue, hsvThreshold1Saturation, hsvThreshold1Value, hsvThreshold1Output);

        //Log.i("vif", "j detec 6");
        // Step Find_Contours1:
        Mat findContours1Input = hsvThreshold1Output;
        boolean findContours1ExternalOnly = true;
        findContours(findContours1Input, findContours1ExternalOnly, findContours1Output);

        //Log.i("vif", "j detec 7");
        // Step Filter_Contours1:
        ArrayList<MatOfPoint> filterContours1Contours = findContours1Output;
        double filterContours1MinArea = 10000.0;
        double filterContours1MinPerimeter = 0.0;
        double filterContours1MinWidth = 0.0;
        double filterContours1MaxWidth = 1000.0;
        double filterContours1MinHeight = 0.0;
        double filterContours1MaxHeight = 1000.0;
        double[] filterContours1Solidity = {0, 100};
        double filterContours1MaxVertices = 1000000.0;
        double filterContours1MinVertices = 0.0;
        double filterContours1MinRatio = 0.0;
        double filterContours1MaxRatio = 1000.0;
        filterContours(filterContours1Contours, filterContours1MinArea, filterContours1MinPerimeter, filterContours1MinWidth, filterContours1MaxWidth, filterContours1MinHeight, filterContours1MaxHeight, filterContours1Solidity, filterContours1MaxVertices, filterContours1MinVertices, filterContours1MinRatio, filterContours1MaxRatio, filterContours1Output);

        //Log.i("vif", "j detec 8");
    }

    /**
     * This method is a generated getter for the output of a Blur.
     * @return Mat output from Blur.
     */
    public Mat blurOutput() {
        return blurOutput;
    }

    /**
     * This method is a generated getter for the output of a HSV_Threshold.
     * @return Mat output from HSV_Threshold.
     */
    public Mat hsvThreshold0Output() {
        return hsvThreshold0Output;
    }

    /**
     * This method is a generated getter for the output of a Find_Contours.
     * @return ArrayList<MatOfPoint> output from Find_Contours.
     */
    public ArrayList<MatOfPoint> findContours0Output() {
        return findContours0Output;
    }

    /**
     * This method is a generated getter for the output of a Filter_Contours.
     * @return ArrayList<MatOfPoint> output from Filter_Contours.
     */
    public ArrayList<MatOfPoint> filterContours0Output() {
        return filterContours0Output;
    }

    /**
     * This method is a generated getter for the output of a HSV_Threshold.
     * @return Mat output from HSV_Threshold.
     */
    public Mat hsvThreshold1Output() {
        return hsvThreshold1Output;
    }

    /**
     * This method is a generated getter for the output of a Find_Contours.
     * @return ArrayList<MatOfPoint> output from Find_Contours.
     */
    public ArrayList<MatOfPoint> findContours1Output() {
        return findContours1Output;
    }

    /**
     * This method is a generated getter for the output of a Filter_Contours.
     * @return ArrayList<MatOfPoint> output from Filter_Contours.
     */
    public ArrayList<MatOfPoint> filterContours1Output() {
        return filterContours1Output;
    }


    /**
     * An indication of which type of filter to use for a blur.
     * Choices are BOX, GAUSSIAN, MEDIAN, and BILATERAL
     */
    enum BlurType{
        BOX("Box Blur"), GAUSSIAN("Gaussian Blur"), MEDIAN("Median Filter"),
        BILATERAL("Bilateral Filter");

        private final String label;

        BlurType(String label) {
            this.label = label;
        }

        public static BlurType get(String type) {
            if (BILATERAL.label.equals(type)) {
                return BILATERAL;
            }
            else if (GAUSSIAN.label.equals(type)) {
                return GAUSSIAN;
            }
            else if (MEDIAN.label.equals(type)) {
                return MEDIAN;
            }
            else {
                return BOX;
            }
        }

        @Override
        public String toString() {
            return this.label;
        }
    }

    /**
     * Softens an image using one of several filters.
     * @param input The image on which to perform the blur.
     * @param type The blurType to perform.
     * @param doubleRadius The radius for the blur.
     * @param output The image in which to store the output.
     */
    private void blur(Mat input, BlurType type, double doubleRadius,
                      Mat output) {
        int radius = (int)(doubleRadius + 0.5);
        int kernelSize;
        switch(type){
            case BOX:
                kernelSize = 2 * radius + 1;
                Imgproc.blur(input, output, new Size(kernelSize, kernelSize));
                break;
            case GAUSSIAN:
                kernelSize = 6 * radius + 1;
                Imgproc.GaussianBlur(input,output, new Size(kernelSize, kernelSize), radius);
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

    /**
     * Segment an image based on hue, saturation, and value ranges.
     *
     * @param input The image on which to perform the HSL threshold.
     * @param hue The min and max hue
     * @param sat The min and max saturation
     * @param val The min and max value
     * @param output The image in which to store the output.
     */
    private void hsvThreshold(Mat input, double[] hue, double[] sat, double[] val,
                              Mat out) {
        Imgproc.cvtColor(input, out, Imgproc.COLOR_BGR2HSV);
        Core.inRange(out, new Scalar(hue[0], sat[0], val[0]),
                new Scalar(hue[1], sat[1], val[1]), out);
    }

    /**
     * Sets the values of pixels in a binary image to their distance to the nearest black pixel.
     * @param input The image on which to perform the Distance Transform.
     * @param type The Transform.
     * @param maskSize the size of the mask.
     * @param output The image in which to store the output.
     */
    private void findContours(Mat input, boolean externalOnly,
                              List<MatOfPoint> contours) {
        Mat hierarchy = new Mat();
        contours.clear();
        int mode;
        if (externalOnly) {
            mode = Imgproc.RETR_EXTERNAL;
        }
        else {
            mode = Imgproc.RETR_LIST;
        }
        int method = Imgproc.CHAIN_APPROX_SIMPLE;
        Imgproc.findContours(input, contours, hierarchy, mode, method);
    }


    /**
     * Filters out contours that do not meet certain criteria.
     * @param inputContours is the input list of contours
     * @param output is the the output list of contours
     * @param minArea is the minimum area of a contour that will be kept
     * @param minPerimeter is the minimum perimeter of a contour that will be kept
     * @param minWidth minimum width of a contour
     * @param maxWidth maximum width
     * @param minHeight minimum height
     * @param maxHeight maximimum height
     * @param Solidity the minimum and maximum solidity of a contour
     * @param minVertexCount minimum vertex Count of the contours
     * @param maxVertexCount maximum vertex Count
     * @param minRatio minimum ratio of width to height
     * @param maxRatio maximum ratio of width to height
     */
    private void filterContours(List<MatOfPoint> inputContours, double minArea,
                                double minPerimeter, double minWidth, double maxWidth, double minHeight, double
                                        maxHeight, double[] solidity, double maxVertexCount, double minVertexCount, double
                                        minRatio, double maxRatio, List<MatOfPoint> output) {
        final MatOfInt hull = new MatOfInt();
        output.clear();
        //operation
        for (int i = 0; i < inputContours.size(); i++) {
            final MatOfPoint contour = inputContours.get(i);
            final Rect bb = Imgproc.boundingRect(contour);
            if (bb.width < minWidth || bb.width > maxWidth) continue;
            if (bb.height < minHeight || bb.height > maxHeight) continue;
            final double area = Imgproc.contourArea(contour);
            if (area < minArea) continue;
            if (Imgproc.arcLength(new MatOfPoint2f(contour.toArray()), true) < minPerimeter) continue;
            Imgproc.convexHull(contour, hull);
            MatOfPoint mopHull = new MatOfPoint();
            mopHull.create((int) hull.size().height, 1, CvType.CV_32SC2);
            for (int j = 0; j < hull.size().height; j++) {
                int index = (int)hull.get(j, 0)[0];
                double[] point = new double[] { contour.get(index, 0)[0], contour.get(index, 0)[1]};
                mopHull.put(j, 0, point);
            }
            final double solid = 100 * area / Imgproc.contourArea(mopHull);
            if (solid < solidity[0] || solid > solidity[1]) continue;
            if (contour.rows() < minVertexCount || contour.rows() > maxVertexCount)	continue;
            final double ratio = bb.width / (double)bb.height;
            if (ratio < minRatio || ratio > maxRatio) continue;
            output.add(contour);
        }
    }



    public int redJewel() {
        return filterContours1Output().size();
    }


    public int blueJewel() {
        return filterContours0Output().size();
    }
}