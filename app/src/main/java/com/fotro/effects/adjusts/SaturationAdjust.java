package com.fotro.effects.adjusts;

import com.fotro.effects.EffectException;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfInt;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

public class SaturationAdjust extends Adjust {
    static final String NAME = "saturation";

    private static final String SATURATION = "saturation";

    private static final double SATURATION_NO_EFFECT = 0;

    private double mSaturation = SATURATION_NO_EFFECT;

    @Override
    public String getName() {
        return NAME;
    }

    /**
     * SATURATION as double: [-1, 0, 1]
     * value == 0: no change
     * -1 <= value < 0: decrease the saturation
     * 0 < value <= 1: increase the saturation
     */
    public void setSaturation(double saturation) throws EffectException {
        if (saturation < -1 || saturation > 1)
            throw new EffectException(SATURATION + " " + saturation + " should be in [-1, 1]");
        mSaturation = saturation;
    }

    public double getSaturation() {
        return mSaturation;
    }

    @Override
    public Mat apply(Mat src) {
        if (mSaturation == SATURATION_NO_EFFECT) {
            return src;
        } else {
            Mat hsv = new Mat();
            Imgproc.cvtColor(src, hsv, Imgproc.COLOR_RGB2HSV);

            List<Mat> channels = new ArrayList<>();
            Core.split(hsv, channels);
            Core.LUT(channels.get(1), getLut(mSaturation), channels.get(1));
            Core.merge(channels, hsv);

            Mat dst = new MatOfInt();
            Imgproc.cvtColor(hsv, dst, Imgproc.COLOR_HSV2RGB);
            return dst;
        }
    }

    private Mat getLut(double saturation) {
        Mat lut = new Mat();
        lut.create(256, 1, CvType.CV_8UC1);
        for (int i = 0; i < 256; i++) {
            lut.put(i, 0, i + (i * saturation));
        }
        return lut;
    }
}
