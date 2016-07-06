package com.fotro.effects.adjusts;

import org.opencv.core.Mat;

public class NoneAdjust extends Adjust {
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
