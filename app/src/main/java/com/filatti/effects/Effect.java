package com.filatti.effects;

import org.opencv.core.Mat;

public interface Effect {
    String getName();

    Mat apply(Mat src);
}
