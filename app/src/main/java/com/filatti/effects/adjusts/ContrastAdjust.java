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

    @Override
    public Adjust copy() throws EffectException {
        ContrastAdjust copied = new ContrastAdjust();
        copied.setContrast(getContrast());
        return copied;
    }

    public void setContrast(double contrast) throws EffectException {
        if (!nativeSetContrast(mNativeObj, contrast)) {
            throw new EffectException("Setting illegal contrast value: " + contrast);
        }
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

    @Override
    public String toString() {
        return "ContrastAdjust: {\n"
                + "\tcontrast: " + getContrast() + "\n"
                + "}";
    }

    private native long nativeCreateObject();

    private native void nativeDestroyObject(long self);

    private native double nativeGetContrast(long self);

    private native boolean nativeSetContrast(long self, double contrast);

    private native boolean nativeApply(long self, long nativeSrcMat, long nativeDstMat);
}
