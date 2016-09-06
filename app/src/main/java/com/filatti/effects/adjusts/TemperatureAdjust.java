package com.filatti.effects.adjusts;

import com.filatti.effects.EffectException;
import com.google.common.base.Preconditions;

import org.opencv.core.Mat;

import timber.log.Timber;

@SuppressWarnings("JniMissingFunction")
public class TemperatureAdjust extends Adjust {
    private final long mNativeObj;

    public TemperatureAdjust() {
        mNativeObj = nativeCreateObject();

        try {
            setKelvin(6600);
            setStrength(0.125);
        } catch (EffectException e) {
            Timber.e(e, "Failed to initialize Temperature");
        }
    }

    @Override
    protected void finalize() throws Throwable {
        nativeDestroyObject(mNativeObj);
        super.finalize();
    }

    public int getKelvin() {
        return nativeGetKelvin(mNativeObj);
    }

    public void setKelvin(int kelvin) throws EffectException {
        if (!nativeSetKelvin(mNativeObj, kelvin))
            throw new EffectException("Kelvin isn't within range: " + kelvin);
    }

    public double getStrength() {
        return nativeGetStrength(mNativeObj);
    }

    public void setStrength(double strength) throws EffectException {
        if (!nativeSetStrength(mNativeObj, strength))
            throw new EffectException("Strength isn't within range: " + strength);
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

    private native int nativeGetKelvin(long self);

    private native boolean nativeSetKelvin(long self, int kelvin);

    private native double nativeGetStrength(long self);

    private native boolean nativeSetStrength(long self, double strength);

    private native boolean nativeApply(long self, long nativeSrcMat, long nativeDstMat);
}
