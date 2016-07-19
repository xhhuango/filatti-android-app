package com.filatti.effects.adjusts;

import com.filatti.effects.EffectException;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

public class SharpenAdjust extends Adjust {
    static final String NAME = "Sharpen";

    private static final String SHARPEN = "sharpen";

    private static final double SHARPEN_NO_EFFECT = 0;

    private double mSharpen = SHARPEN_NO_EFFECT;

    private Mat mBlurred;

    @Override
    public String getName() {
        return NAME;
    }

    /**
     * SHARPEN as double: [0, 1]
     * value == 0: no change
     * 0 < value <= 1: sharpen
     */
    public void setSharpe(double sharpen) throws EffectException {
        if (sharpen < 0 || sharpen > 1)
            throw new EffectException(NAME + "." + SHARPEN + " " + sharpen
                                              + " should be in [0, 1]");
        mSharpen = sharpen;
        if (mSharpen == SHARPEN_NO_EFFECT && mBlurred != null) {
            mBlurred.release();
            mBlurred = null;
        }
    }

    public double getSharpen() {
        return mSharpen;
    }

    @Override
    public Mat apply(Mat src) {
        if (mSharpen == SHARPEN_NO_EFFECT) {
            return src;
        } else {
            if (mBlurred == null) {
                mBlurred = new Mat();
                Imgproc.GaussianBlur(src, mBlurred, new Size(0, 0), 3);
            }
            Mat dst = new Mat();
            Core.addWeighted(src, 1.0 + mSharpen, mBlurred, -mSharpen, 0, dst);
            return dst;
        }
    }
}
