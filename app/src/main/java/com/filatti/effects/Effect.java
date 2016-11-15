package com.filatti.effects;

import org.opencv.core.Mat;

public interface Effect {
    boolean hasEffect();

    Mat apply(Mat src);
}
