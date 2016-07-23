package com.filatti.effects;

import org.opencv.core.Mat;

public interface Effect {
    Mat apply(Mat src);
}
