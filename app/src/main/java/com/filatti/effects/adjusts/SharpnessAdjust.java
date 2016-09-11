package com.filatti.effects.adjusts;

import com.filatti.effects.EffectException;
import com.google.common.base.Preconditions;

import org.opencv.core.Mat;

@SuppressWarnings("JniMissingFunction")
public class SharpnessAdjust extends Adjust {
    private final long mNativeObj;

    private final double mInitSharpness;

    public SharpnessAdjust() {
        mNativeObj = nativeCreateObject();

        mInitSharpness = getSharpness();
    }

    @Override
    protected void finalize() throws Throwable {
        nativeDestroyObject(mNativeObj);
        super.finalize();
    }

    public void setSharpness(double sharpness) throws EffectException {
        if (!nativeSetSharpness(mNativeObj, sharpness))
            throw new EffectException("Sharpness isn't within the range: " + sharpness);
    }

    public double getSharpness() {
        return nativeGetSharpness(mNativeObj);
    }

    public double getInitSharpness() {
        return mInitSharpness;
    }

    public void setRebuildBlurred(boolean doesRebuildBlurred) {
        nativeSetRebuildBlurred(mNativeObj, doesRebuildBlurred);
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
        return "SharpnessAdjust: {\n"
                + "\tsharpness: " + getSharpness() + "\n"
                + "}";
    }

    private native long nativeCreateObject();

    private native void nativeDestroyObject(long self);

    private native double nativeGetSharpness(long self);

    private native boolean nativeSetSharpness(long self, double sharpness);

    private native void nativeSetRebuildBlurred(long self, boolean doesRebuildBlurred);

    private native boolean nativeApply(long self, long nativeSrcMat, long nativeDstMat);
}
