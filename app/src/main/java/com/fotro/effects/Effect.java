package com.fotro.effects;

import org.opencv.core.Mat;

public interface Effect {
    String getName();

    boolean apply(Mat srcRgb, Mat dstRgb);
}
