package com.filatti.effects.adjusts;

import com.filatti.effects.EffectException;
import com.google.common.base.Preconditions;

import org.opencv.core.Mat;

@SuppressWarnings("JniMissingFunction")
public class HsvAdjust extends Adjust {
    private final long mNativeObj;

    public HsvAdjust() {
        mNativeObj = nativeCreateObject();
    }

    @Override
    protected void finalize() throws Throwable {
        nativeDestroyObject(mNativeObj);
        super.finalize();
    }

    public void setBrightness(double brightness) throws EffectException {
        if (!nativeSetBrightness(mNativeObj, brightness))
            throw new EffectException("Brightness isn't within range: " + brightness);
    }

    public double getBrightness() {
        return nativeGetBrightness(mNativeObj);
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
        return nativeApply(mNativeObj, src.getNativeObjAddr(), dst.getNativeObjAddr())
                ? dst
                : src;
    }

    private native long nativeCreateObject();

    private native void nativeDestroyObject(long self);

    private native double nativeGetBrightness(long self);

    private native boolean nativeSetBrightness(long self, double brightness);

    private native double nativeGetSaturation(long self);

    private native boolean nativeSetSaturation(long self, double saturation);

    private native boolean nativeApply(long self, long nativeSrcMat, long nativeDstMat);
}
