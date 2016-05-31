package com.fotro.imgproc.adjustments;

import org.opencv.core.Mat;

public class NoneAdjustment extends Adjustment {
    static final String NAME = "none";

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public void apply(Mat srcRgb, Mat dstRgb) {
        srcRgb.assignTo(dstRgb);
    }
}
