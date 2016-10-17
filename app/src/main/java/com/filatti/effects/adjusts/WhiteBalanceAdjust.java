package com.filatti.effects.adjusts;

import com.filatti.effects.EffectException;
import com.google.common.base.Preconditions;

import org.opencv.core.Mat;

@SuppressWarnings("JniMissingFunction")
public class WhiteBalanceAdjust extends Adjust {
    private final long mNativeObj;

    private final double mInitPercent;

    public WhiteBalanceAdjust() {
        mNativeObj = nativeCreateObject();

        mInitPercent = getPercent();
    }

    @Override
    protected void finalize() throws Throwable {
        nativeDestroyObject(mNativeObj);
        super.finalize();
    }

    public void setPercent(double percent) throws EffectException {
        if (!nativeSetPercent(mNativeObj, percent)) {
            throw new EffectException("Setting illegal percent value: " + percent);
        }
    }

    public double getPercent() {
        return nativeGetPercent(mNativeObj);
    }

    public double getInitPercent() {
        return mInitPercent;
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
        return "WhiteBalanceAdjust: {\n"
                + "\tpercent: " + getPercent() + "\n"
                + "}";
    }

    private native long nativeCreateObject();

    private native void nativeDestroyObject(long self);

    private native double nativeGetPercent(long self);

    private native boolean nativeSetPercent(long self, double percent);

    private native boolean nativeApply(long self, long nativeSrcMat, long nativeDstMat);
}
