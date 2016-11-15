package com.filatti.effects.adjusts;

import com.filatti.effects.EffectException;
import com.google.common.base.Preconditions;

import org.opencv.core.Mat;

@SuppressWarnings("JniMissingFunction")
public class ContrastAdjust extends Adjust {
    private final long mNativeObj;

    private final float mInitContrast;

    public ContrastAdjust() {
        mNativeObj = nativeCreateObject();

        mInitContrast = getContrast();
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

    public void setContrast(float contrast) throws EffectException {
        if (!nativeSetContrast(mNativeObj, contrast)) {
            throw new EffectException("Setting illegal contrast value: " + contrast);
        }
    }

    public float getContrast() {
        return nativeGetContrast(mNativeObj);
    }

    public float getInitContrast() {
        return mInitContrast;
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
        return "ContrastAdjust: {\n"
                + "\tcontrast: " + getContrast() + "\n"
                + "}";
    }

    private native long nativeCreateObject();

    private native void nativeDestroyObject(long self);

    private native boolean nativeHasEffect(long self);

    private native float nativeGetContrast(long self);

    private native boolean nativeSetContrast(long self, float contrast);

    private native boolean nativeApply(long self, long nativeSrcMat, long nativeDstMat);
}
