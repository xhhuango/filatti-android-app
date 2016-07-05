package com.fotro.effects.adjustments.core;

import com.fotro.effects.adjustments.Adjustment;
import com.fotro.effects.adjustments.IllegalAdjustmentParameter;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfInt;

public class ContrastBrightnessAdjustment extends Adjustment {
    static final String NAME = "contrast_brightness";

    private static final String CONTRAST = "contrast";
    private static final String BRIGHTNESS = "brightness";

    private double mContrast = 1;
    private int mBrightness = 0;

    @Override
    public String getName() {
        return NAME;
    }

    /**
     * CONTRAST as double: [0, 1, 3]
     * value == 1: no change
     * 0 <= value < 1: decrease the contrast
     * 1 < value <= 3: increase
     */
    public void setContrast(double contrast) throws IllegalAdjustmentParameter {
        if (contrast < 0 || contrast > 3)
            throw new IllegalAdjustmentParameter(getName(), CONTRAST, "[0, 3]", contrast);
        mContrast = contrast;
    }

    /**
     * BRIGHTNESS as integer: [-255, 0, 255]
     * value == 0: no change
     * -255 <= value < 0: decease the brightness
     * 0 < value <= 255: increase the brightness
     */
    public void setBrightness(int brightness) throws IllegalAdjustmentParameter {
        if (brightness < -255 || brightness > 255)
            throw new IllegalAdjustmentParameter(getName(), BRIGHTNESS, "[-255, 255]", brightness);
        mBrightness = brightness;
    }

    @Override
    public void apply(Mat srcRgb, Mat dstRgb) {
        if (mContrast == 1 && mBrightness == 0)
            srcRgb.convertTo(dstRgb, -1);
        else
            Core.LUT(srcRgb, getLut(mContrast, mBrightness), dstRgb);
    }

    private Mat getLut(double contrast, int brightness) {
        Mat lut = new MatOfInt();
        lut.create(256, 1, CvType.CV_8UC3);
        for (int i = 0; i < 256; i++) {
            double value = (i - 127.0) * contrast + 127.0 + brightness;
            lut.put(i, 0, value, value, value);
        }
        return lut;
    }
}
