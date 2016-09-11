package com.filatti.effects.adjusts;

import com.filatti.effects.EffectException;
import com.google.common.base.Preconditions;

import org.opencv.core.Mat;

@SuppressWarnings("JniMissingFunction")
public class TemperatureAdjust extends Adjust {
    private final long mNativeObj;

    private final double mInitTemperature;

    public TemperatureAdjust() {
        mNativeObj = nativeCreateObject();

        mInitTemperature = getTemperature();
    }

    @Override
    protected void finalize() throws Throwable {
        nativeDestroyObject(mNativeObj);
        super.finalize();
    }

    public double getInitTemperature() {
        return mInitTemperature;
    }

    public double getTemperature() {
        return nativeGetTemperature(mNativeObj);
    }

    public void setTemperature(double temperature) throws EffectException {
        if (!nativeSetTemperature(mNativeObj, temperature)) {
            throw new EffectException("Temperature isn't within range: " + temperature);
        }
    }

    @Override
    public Mat apply(Mat src) {
        Preconditions.checkNotNull(src);

        Mat dst = new Mat();
        return (nativeApply(mNativeObj, src.getNativeObjAddr(), dst.getNativeObjAddr()))
                ? dst
                : src;
    }

    @Override
    public String toString() {
        return "TemperatureAdjust: {\n"
                + "\ttemperature: " + getTemperature() + "\n"
                + "}";
    }

    private native long nativeCreateObject();

    private native void nativeDestroyObject(long self);

    private native double nativeGetTemperature(long self);

    private native boolean nativeSetTemperature(long self, double temperature);

    private native boolean nativeApply(long self, long nativeSrcMat, long nativeDstMat);
}
