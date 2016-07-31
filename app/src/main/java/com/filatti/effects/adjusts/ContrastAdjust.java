package com.filatti.effects.adjusts;

import com.filatti.effects.EffectException;
import com.google.common.base.Preconditions;

import org.opencv.core.Mat;

@SuppressWarnings("JniMissingFunction")
public class ContrastAdjust extends Adjust {
    private final long mNativeObj;

    public ContrastAdjust() {
        mNativeObj = nativeCreateObject();
    }

    @Override
    protected void finalize() throws Throwable {
        nativeDestroyObject(mNativeObj);
        super.finalize();
    }

    public void setContrast(double contrast) throws EffectException {
        if (!nativeSetContrast(mNativeObj, contrast))
            throw new EffectException("Contrast isn't within range: " + contrast);
    }

    public double getContrast() {
        return nativeGetContrast(mNativeObj);
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

    private native double nativeGetContrast(long self);

    private native boolean nativeSetContrast(long self, double contrast);

    private native boolean nativeApply(long self, long nativeSrcMat, long nativeDstMat);
}
