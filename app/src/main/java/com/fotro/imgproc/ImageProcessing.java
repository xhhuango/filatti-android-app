package com.fotro.imgproc;

import org.opencv.core.Mat;

public interface ImageProcessing {
    String getName();

    boolean check();

    void init();

    void apply(Mat srcRgb, Mat dstRgb);
}
