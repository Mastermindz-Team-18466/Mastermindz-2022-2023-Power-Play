package org.firstinspires.ftc.teamcode.vision.center.pipeline;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvPipeline;

import java.util.ArrayList;

public class CenterOfConeBluePipeline extends OpenCvPipeline {
    //Outputs
    private Mat cvtColorOutput = new Mat();
    private Mat rgbThresholdOutput = new Mat();

    public float x = 0;
    public float y = 0;

    @Override
    public Mat processFrame(Mat input) {
        // Step CV_cvtColor0:
        Imgproc.cvtColor(input, cvtColorOutput, Imgproc.COLOR_BGR2HSV);

        // Step RGB_Threshold0:
        Mat rgbThresholdInput = cvtColorOutput;
        double[] rgbThresholdRed = {90, 255};
        double[] rgbThresholdGreen = {0, 90};
        double[] rgbThresholdBlue = {0, 85};
        rgbThreshold(input, rgbThresholdRed, rgbThresholdGreen, rgbThresholdBlue, rgbThresholdOutput);

        ArrayList<MatOfPoint> contours = new ArrayList<MatOfPoint>();
        Mat hierarchy = new Mat();
        Mat contourMat = rgbThresholdOutput.clone();
        Imgproc.findContours(contourMat, contours, hierarchy, Imgproc.RETR_EXTERNAL,
                Imgproc.CHAIN_APPROX_SIMPLE);

        if (contours.size() > 1) {
            int largestContourIndex = 0;
            double lastContourArea = 100;
            for (int i = 0; i < contours.size(); i++) {
                double contourArea = Imgproc.contourArea(contours.get(i));
                if (contourArea > lastContourArea) {
                    largestContourIndex = i;
                    lastContourArea = contourArea;
                }
            }

            if (lastContourArea > 100) {
                //get bounding rect
                Rect boundingRect = Imgproc
                        .boundingRect(new MatOfPoint(contours.get(largestContourIndex).toArray()));
                Imgproc.rectangle(rgbThresholdOutput, new Point(boundingRect.x, boundingRect.y),
                        new Point(boundingRect.x + boundingRect.width, boundingRect.y + boundingRect.height),
                        new Scalar(255));

                x = boundingRect.x + boundingRect.width / 2;
                y = boundingRect.y + boundingRect.height / 2;

                Imgproc.rectangle(rgbThresholdOutput, new Point(x, y), new Point(x, y), new Scalar(0));
            } else {
                x = 0;
                y = 0;
            }
        }

        return rgbThresholdOutput;
    }

    private void rgbThreshold(Mat input, double[] red, double[] green, double[] blue,
                              Mat out) {
        Imgproc.cvtColor(input, out, Imgproc.COLOR_BGR2RGB);
        Core.inRange(out, new Scalar(red[0], green[0], blue[0]),
                new Scalar(red[1], green[1], blue[1]), out);
    }
}
