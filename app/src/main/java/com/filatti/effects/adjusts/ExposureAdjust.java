package com.filatti.effects.adjusts;

import com.filatti.effects.EffectException;
import com.google.common.base.Preconditions;

import org.opencv.core.Mat;

@SuppressWarnings("JniMissingFunction")
public class ExposureAdjust extends Adjust {
    private final long mNativeObj;

    private final float mInitExposure;

    public ExposureAdjust() {
        mNativeObj = nativeCreateObject();

        mInitExposure = getExposure();
    }

    @Override
    protected void finalize() throws Throwable {
        nativeDestroyObject(mNativeObj);
        super.finalize();
    }

    public void setExposure(float exposure) throws EffectException {
        if (!nativeSetExposure(mNativeObj, exposure)) {
            throw new EffectException("Setting illegal exposure value: " + exposure);
        }
    }

    public float getExposure() {
        return nativeGetExposure(mNativeObj);
    }

    public float getInitExposure() {
        return mInitExposure;
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
        return "ExposureAdjust: {\n"
                + "\texposure: " + getExposure() + "\n"
                + "}";
    }

    private native long nativeCreateObject();

    private native void nativeDestroyObject(long self);

    private native float nativeGetExposure(long self);

    private native boolean nativeSetExposure(long self, float exposure);

    private native boolean nativeApply(long self, long nativeSrcMat, long nativeDstMat);
}
