package com.filatti.effects.adjusts;

import com.google.common.base.Preconditions;

import org.opencv.core.Mat;

@SuppressWarnings("JniMissingFunction")
public class CurvesAdjust extends Adjust {
    private final long mNativeObj;

    public CurvesAdjust() {
        mNativeObj = nativeCreateObject();
    }

    @Override
    protected void finalize() throws Throwable {
        nativeDestroyObject(mNativeObj);
        super.finalize();
    }

    /**
     * Value
     */

    public int[] getValueCurves() {
        return nativeGetValueCurves(mNativeObj);
    }

    public int[] getValuePoints() {
        return nativeGetValuePoints(mNativeObj);
    }

    public void setValuePoints(int[] points) {
        nativeSetValuePoints(mNativeObj, points);
    }

    /**
     * Blue
     */

    public int[] getBlueCurves() {
        return nativeGetBlueCurves(mNativeObj);
    }

    public int[] getBluePoints() {
        return nativeGetBluePoints(mNativeObj);
    }

    public void setBluePoints(int[] points) {
        nativeSetBluePoints(mNativeObj, points);
    }

    /**
     * Green
     */

    public int[] getGreenCurves() {
        return nativeGetGreenCurves(mNativeObj);
    }

    public int[] getGreenPoints() {
        return nativeGetGreenPoints(mNativeObj);
    }

    public void setGreenPoints(int[] points) {
        nativeSetGreenPoints(mNativeObj, points);
    }

    /**
     * Red
     */

    public int[] getRedCurves() {
        return nativeGetRedCurves(mNativeObj);
    }

    public int[] getRedPoints() {
        return nativeGetRedPoints(mNativeObj);
    }

    public void setRedPoints(int[] points) {
        nativeSetRedPoints(mNativeObj, points);
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

    private native boolean nativeApply(long self, long nativeSrcMat, long nativeDstMat);

    /**
     * Value native
     */

    private native int[] nativeGetValueCurves(long self);

    private native int[] nativeGetValuePoints(long self);

    private native boolean nativeSetValuePoints(long self, int[] arr);

    /**
     * Blue native
     */

    private native int[] nativeGetBlueCurves(long self);

    private native int[] nativeGetBluePoints(long self);

    private native boolean nativeSetBluePoints(long self, int[] arr);

    /**
     * Green native
     */

    private native int[] nativeGetGreenCurves(long self);

    private native int[] nativeGetGreenPoints(long self);

    private native boolean nativeSetGreenPoints(long self, int[] arr);

    /**
     * Red native
     */

    private native int[] nativeGetRedCurves(long self);

    private native int[] nativeGetRedPoints(long self);

    private native boolean nativeSetRedPoints(long self, int[] arr);
}
