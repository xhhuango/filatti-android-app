package com.filatti.effects.adjusts;

import org.opencv.core.Mat;

/**
 * Created by wayne on 7/18/16.
 */
public class SaturationNativeAdjust extends Adjust {
    public native long setSaturation();

    @Override
    public String getName() {
        return null;
    }

    @Override
    public Mat apply(Mat src) {
        return null;
    }
}
