package com.fotro.effects;

import org.opencv.core.Mat;

public interface Effect {
    String getName();

    void apply(Mat srcRgb, Mat dstRgb);
}
