package com.fotro.effects.adjusts;

import com.fotro.effects.EffectException;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfInt;

public class ContrastBrightnessAdjust extends Adjust {
    static final String NAME = "contrast_brightness";

    private static final String CONTRAST = "contrast";
    private static final String BRIGHTNESS = "brightness";

    private static final double CONTRACT_NO_EFFECT = 1;
    private static final int BRIGHTNESS_NO_EFFECT = 0;

    private double mContrast = CONTRACT_NO_EFFECT;
    private int mBrightness = BRIGHTNESS_NO_EFFECT;

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
    public void setContrast(double contrast) throws EffectException {
        if (contrast < 0 || contrast > 3)
            throw new EffectException(CONTRAST + " " + contrast + " should be in [0, 3]");
        mContrast = contrast;
    }

    public double getContrast() {
        return mContrast;
    }

    /**
     * BRIGHTNESS as integer: [-255, 0, 255]
     * value == 0: no change
     * -255 <= value < 0: decease the brightness
     * 0 < value <= 255: increase the brightness
     */
    public void setBrightness(int brightness) throws EffectException {
        if (brightness < -255 || brightness > 255)
            throw new EffectException(BRIGHTNESS + " " + brightness + "should be in [-255, 255]");
        mBrightness = brightness;
    }

    public int getBrightness() {
        return mBrightness;
    }

    @Override
    public boolean apply(Mat srcRgba, Mat dstRgba) {
        if (mContrast == CONTRACT_NO_EFFECT && mBrightness == BRIGHTNESS_NO_EFFECT) {
            return false;
        } else {
            Core.LUT(srcRgba, getLut(mContrast, mBrightness), dstRgba);
            return true;
        }
    }

    private Mat getLut(double contrast, int brightness) {
        Mat lut = new MatOfInt();
        lut.create(256, 1, CvType.CV_8UC4);
        for (int i = 0; i < 256; i++) {
            double value = (i - 127.0) * contrast + 127.0 + brightness;
            lut.put(i, 0, value, value, value, i);
        }
        return lut;
    }
}
