package com.filatti.effects.adjusts;

import com.filatti.effects.EffectException;
import com.google.common.base.Preconditions;

import org.opencv.core.Mat;

@SuppressWarnings("JniMissingFunction")
public class HlsAdjust extends Adjust {
    private final long mNativeObj;

    public HlsAdjust() {
        mNativeObj = nativeCreateObject();
    }

    @Override
    protected void finalize() throws Throwable {
        nativeDestroyObject(mNativeObj);
        super.finalize();
    }

    public void setHue(int hue) throws EffectException {
        if (!nativeSetHue(mNativeObj, hue))
            throw new EffectException("Hue isn't within range: " + hue);
    }

    public int getHue() {
        return nativeGetHue(mNativeObj);
    }

    public void setLightness(double lightness) throws EffectException {
        if (!nativeSetLightness(mNativeObj, lightness))
            throw new EffectException("Lightness isn't within range: " + lightness);
    }

    public double getLightness() {
        return nativeGetLightness(mNativeObj);
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

    private native int nativeGetHue(long self);

    private native boolean nativeSetHue(long self, int hue);

    private native double nativeGetLightness(long self);

    private native boolean nativeSetLightness(long self, double lightness);

    private native double nativeGetSaturation(long self);

    private native boolean nativeSetSaturation(long self, double saturation);

    private native boolean nativeApply(long self, long nativeSrcMat, long nativeDstMat);
}
