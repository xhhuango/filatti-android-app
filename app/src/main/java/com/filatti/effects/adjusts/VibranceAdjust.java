package com.filatti.effects.adjusts;

import com.filatti.effects.EffectException;
import com.google.common.base.Preconditions;

import org.opencv.core.Mat;

@SuppressWarnings("JniMissingFunction")
public class VibranceAdjust extends Adjust {
    private final long mNativeObj;

    private final float mInitVibrance;

    public VibranceAdjust() {
        mNativeObj = nativeCreateObject();

        mInitVibrance = getVibrance();
    }

    @Override
    protected void finalize() throws Throwable {
        nativeDestroyObject(mNativeObj);
        super.finalize();
    }

    @Override
    public boolean hasEffect() {
        return nativeHasEffect(mNativeObj);
    }

    public void setVibrance(float vibrance) throws EffectException {
        if (!nativeSetVibrance(mNativeObj, vibrance)) {
            throw new EffectException("Setting illegal vibrance value: " + vibrance);
        }
    }

    public float getVibrance() {
        return nativeGetVibrance(mNativeObj);
    }

    public float getInitVibrance() {
        return mInitVibrance;
    }

    @Override
    public Mat apply(Mat src) {
        Preconditions.checkNotNull(src);

        Mat dst = new Mat();
        return nativeApply(mNativeObj, src.getNativeObjAddr(), dst.getNativeObjAddr())
                ? dst
                : src;
    }

    @Override
    public String toString() {
        return "VibranceAdjust: {\n"
                + "\tvibrance: " + getVibrance() + "\n"
                + "}";
    }

    private native long nativeCreateObject();

    private native void nativeDestroyObject(long self);

    private native boolean nativeHasEffect(long self);

    private native float nativeGetVibrance(long self);

    private native boolean nativeSetVibrance(long self, float vibrance);

    private native boolean nativeApply(long self, long nativeSrcMat, long nativeDstMat);
}
