package com.filatti.effects.adjusts;

import com.filatti.effects.EffectException;
import com.google.common.base.Preconditions;

import org.opencv.core.Mat;

@SuppressWarnings("JniMissingFunction")
public class SaturationAdjust extends Adjust {
    private final long mNativeObj;

    public SaturationAdjust() {
        mNativeObj = nativeCreateObject();
    }

    @Override
    protected void finalize() throws Throwable {
        nativeDestroyObject(mNativeObj);
        super.finalize();
    }

    public void setSaturation(double saturation) throws EffectException {
        if (!nativeSetSaturation(mNativeObj, saturation))
            throw new EffectException("Saturation isn't within range: " + saturation);
    }

    public double getSaturation() {
        return nativeGetSaturation(mNativeObj);
    }

    @Override
    public Mat apply(Mat src) {
        Preconditions.checkNotNull(src);

        Mat dst = new Mat();
        return (nativeApply(mNativeObj, src.getNativeObjAddr(), dst.getNativeObjAddr()))
                ? dst
                : src;
    }

    private native long nativeCreateObject();

    private native void nativeDestroyObject(long self);

    private native double nativeGetSaturation(long self);

    private native boolean nativeSetSaturation(long self, double saturation);

    private native boolean nativeApply(long self, long nativeSrcMat, long nativeDstMat);
}
