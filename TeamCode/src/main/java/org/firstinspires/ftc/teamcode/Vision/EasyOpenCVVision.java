package org.firstinspires.ftc.teamcode.Vision;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvPipeline;


public class EasyOpenCVVision extends OpenCvPipeline {
    // This enum contains the possible number of rings
    public enum ShipPosition {
        LEFT,
        CENTER,
        NONE
    }
    //bpvtytybz
    // Color constants
    static final Scalar RED = new Scalar(255, 0, 0);
    static final Scalar GREEN = new Scalar(0, 255, 0);

    // Constants for determining the existence of shipping element based on the RED content
    final int ONE_SHIP_THRESHOLD = 150;
    final int NONE_SHIP_THRESHOLD = 50;

    // Upper-left point of the rectangle where shipping element will be defined
    static final Point REGION1_TOPLEFT_ANCHOR_POINT = new Point(50, 375);
    // The width of the rectangle where the sh. el. will be defined
    static final int REGION_WIDTH = 190;
    // The height of the rectangle where the sh. el. will be defined
    static final int REGION_HEIGHT = 150;

    // Creating the upper-left point of the rectangle where the rings will be defined
    Point region1_pointA = new Point(
            REGION1_TOPLEFT_ANCHOR_POINT.x,
            REGION1_TOPLEFT_ANCHOR_POINT.y);
    // Creating the lower-right point of the rectangle where the rings will be defined
    Point region1_pointB = new Point(
            REGION1_TOPLEFT_ANCHOR_POINT.x + REGION_WIDTH,
            REGION1_TOPLEFT_ANCHOR_POINT.y + REGION_HEIGHT);

    // Working variables
    Mat region1_Cb;
    Mat YCrCb = new Mat();
    Mat Cb = new Mat();
    Mat Cr = new Mat();
    int avg1R; // The amount of red in the specified rectangle

    // Working variables
    Mat region2_Cb;
    //Mat Cb2 = new Mat();
    int avg2R; // The amount of red in the specified rectangle

    // Working variables
    Mat region3_Cb;
    //Mat Cb2 = new Mat();
    int avg3R; // The amount of red in the specified rectangle

    Mat region1_Cr;
    int avg1B; // The amount of blue in the specified rectangle

    // Working variables
    Mat region2_Cr;
    //Mat Cb2 = new Mat();
    int avg2B; // The amount of blue in the specified rectangle

    // Working variables
    Mat region3_Cr;
    //Mat Cb2 = new Mat();
    int avg3B; // The amount of blue in the specified rectangle

    // Upper-left point of the rectangle where shipping elemnt will be defined
    static final Point REGION2_TOPLEFT_ANCHOR_POINT = new Point(530, 275);
    // The width of the rectangle where the sh. el. will be defined
    static final int REGION2_WIDTH = 175;
    // The height of the rectangle where the sh. el. will be defined
    static final int REGION2_HEIGHT = 150;

    // Creating the upper-left point of the rectangle where the rings will be defined
    Point region2_pointA = new Point(
            REGION2_TOPLEFT_ANCHOR_POINT.x,
            REGION2_TOPLEFT_ANCHOR_POINT.y);
    // Creating the lower-right point of the rectangle where the rings will be defined
    Point region2_pointB = new Point(
            REGION2_TOPLEFT_ANCHOR_POINT.x + REGION2_WIDTH,
            REGION2_TOPLEFT_ANCHOR_POINT.y + REGION2_HEIGHT);

    // Upper-left point of the rectangle where shipping elemnt will be defined
    static final Point REGION3_TOPLEFT_ANCHOR_POINT = new Point(925, 375);
    // The width of the rectangle where the sh. el. will be defined
    static final int REGION3_WIDTH = 190;
    // The height of the rectangle where the sh. el. will be defined
    static final int REGION3_HEIGHT = 150;

    // Creating the upper-left point of the rectangle where the rings will be defined
    Point region3_pointA = new Point(
            REGION3_TOPLEFT_ANCHOR_POINT.x,
            REGION3_TOPLEFT_ANCHOR_POINT.y);
    // Creating the lower-right point of the rectangle where the rings will be defined
    Point region3_pointB = new Point(
            REGION3_TOPLEFT_ANCHOR_POINT.x + REGION3_WIDTH,
            REGION3_TOPLEFT_ANCHOR_POINT.y + REGION3_HEIGHT);

    // Variable where the number of rings will be stored at the moment
    public volatile ShipPosition position = ShipPosition.NONE;

    // This function takes the RGB frame, converts to YCrCb, and extracts the Cb channel to the 'Cb' variable
    void inputToCb(Mat input) {
        Imgproc.cvtColor(input, YCrCb, Imgproc.COLOR_RGB2YCrCb);
        Core.extractChannel(YCrCb, Cb, 1);
    }

    void inputToCr(Mat input) {
        Imgproc.cvtColor(input, YCrCb, Imgproc.COLOR_RGB2YCrCb);
        Core.extractChannel(YCrCb, Cr, 2);
    }

    @Override
    public void init(Mat firstFrame) {
        inputToCb(firstFrame);
        region1_Cb = Cb.submat(new Rect(region1_pointA, region1_pointB));
        region2_Cb = Cb.submat(new Rect(region2_pointA, region2_pointB));
        region3_Cb = Cb.submat(new Rect(region3_pointA, region3_pointB));

        inputToCr(firstFrame);
        region1_Cr = Cr.submat(new Rect(region1_pointA, region1_pointB));
        region2_Cr = Cr.submat(new Rect(region2_pointA, region2_pointB));
        region3_Cr = Cr.submat(new Rect(region3_pointA, region3_pointB));
    }

    @Override
    public Mat processFrame(Mat input) {
        inputToCb(input);

        avg1R = (int) Core.mean(region1_Cb).val[0];
        dataFromOpenCV.AVG1R = avg1R;
        avg2R = (int) Core.mean(region2_Cb).val[0];
        dataFromOpenCV.AVG2R = avg2R;
        avg3R = (int) Core.mean(region3_Cb).val[0];
        dataFromOpenCV.AVG3R = avg3R;

        inputToCr(input);
        avg1B = (int) Core.mean(region1_Cr).val[0];
        dataFromOpenCV.AVG1B = avg1B;
        avg2B = (int) Core.mean(region2_Cr).val[0];
        dataFromOpenCV.AVG2B = avg2B;
        avg3B = (int) Core.mean(region3_Cr).val[0];
        dataFromOpenCV.AVG3B = avg3B;

        Imgproc.rectangle(
                input, // Buffer to draw on
                region1_pointA, // First point which defines the rectangle
                region1_pointB, // Second point which defines the rectangle
                GREEN, // The color the rectangle is drawn in
                3); // Thickness of the rectangle lines

        Imgproc.rectangle(
                input, // Buffer to draw on
                region2_pointA, // First point which defines the rectangle
                region2_pointB, // Second point which defines the rectangle
                GREEN, // The color the rectangle is drawn in
                3); // Thickness of the rectangle lines

        Imgproc.rectangle(
                input, // Buffer to draw on
                region3_pointA, // First point which defines the rectangle
                region3_pointB, // Second point which defines the rectangle
                GREEN, // The color the rectangle is drawn in
                3); // Thickness of the rectangle lines

        position = ShipPosition.NONE; // Record our analysis

        if (avg1R>avg2R&avg1R>avg3R || avg1B>avg2B&avg1B>avg3B) {
            position = ShipPosition.LEFT;
        }
        if (avg2R>avg1R&avg2R>avg3R || avg2B>avg1B&avg2B>avg3B) {
            position = ShipPosition.CENTER;
        }
        if (avg3R>avg1R&avg3R>avg2R || avg3B>avg1B&avg3B>avg2B) {
            position = ShipPosition.NONE;
        }

        return input;
    }

    public int getAnalysis() {
        return avg1R;
    }
    public int getAnalysis2() {
        return avg2R;
    }

}