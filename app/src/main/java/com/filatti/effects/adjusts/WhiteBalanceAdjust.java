package com.filatti.effects.adjusts;

import com.filatti.effects.EffectException;
import com.google.common.base.Preconditions;

import org.opencv.core.Mat;

@SuppressWarnings("JniMissingFunction")
public class WhiteBalanceAdjust extends Adjust {
    private final long mNativeObj;

    private final float mInitPercent;

    public WhiteBalanceAdjust() {
        mNativeObj = nativeCreateObject();

        mInitPercent = getPercent();
    }

    @Override
    protected void finalize() throws Throwable {
        nativeDestroyObject(mNativeObj);
        super.finalize();
    }

    public void setPercent(float percent) throws EffectException {
        if (!nativeSetPercent(mNativeObj, percent)) {
            throw new EffectException("Setting illegal percent value: " + percent);
        }
    }

    public float getPercent() {
        return nativeGetPercent(mNativeObj);
    }

    public float getInitPercent() {
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

    private native float nativeGetPercent(long self);

    private native boolean nativeSetPercent(long self, float percent);

    private native boolean nativeApply(long self, long nativeSrcMat, long nativeDstMat);
}
