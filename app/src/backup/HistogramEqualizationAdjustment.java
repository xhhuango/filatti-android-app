package com.fotro.effects.adjusts;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfInt;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

public class HistogramEqualizationAdjustment extends Adjustment {
    static final String NAME = "histogram_equalization";

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public void apply(Mat srcRgb, Mat dstRgb) {
        Mat ycrcb = new MatOfInt();
        Imgproc.cvtColor(srcRgb, ycrcb, Imgproc.COLOR_RGB2YCrCb);
        List<Mat> channels = new ArrayList<>();
        Core.split(ycrcb, channels);
        Imgproc.equalizeHist(channels.get(0), channels.get(0));
        Core.merge(channels, ycrcb);
        Imgproc.cvtColor(ycrcb, dstRgb, Imgproc.COLOR_YCrCb2RGB);
    }
}
