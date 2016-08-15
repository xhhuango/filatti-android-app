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

    public void getValueCurves(int[] curves) {
        Preconditions.checkNotNull(curves);
        Preconditions.checkArgument(curves.length == 256);
        nativeGetValueCurves(mNativeObj, curves);
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

    public void getBlueCurves(int[] curves) {
        Preconditions.checkNotNull(curves);
        Preconditions.checkArgument(curves.length == 256);
        nativeGetBlueCurves(mNativeObj, curves);
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

    public void getGreenCurves(int[] curves) {
        Preconditions.checkNotNull(curves);
        Preconditions.checkArgument(curves.length == 256);
        nativeGetGreenCurves(mNativeObj, curves);
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

    public void getRedCurves(int[] curves) {
        Preconditions.checkNotNull(curves);
        Preconditions.checkArgument(curves.length == 256);
        nativeGetRedCurves(mNativeObj, curves);
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

    private native void nativeGetValueCurves(long self, int[] arr);

    private native int[] nativeGetValuePoints(long self);

    private native boolean nativeSetValuePoints(long self, int[] arr);

    /**
     * Blue native
     */

    private native void nativeGetBlueCurves(long self, int[] arr);

    private native int[] nativeGetBluePoints(long self);

    private native boolean nativeSetBluePoints(long self, int[] arr);

    /**
     * Green native
     */

    private native void nativeGetGreenCurves(long self, int[] arr);

    private native int[] nativeGetGreenPoints(long self);

    private native boolean nativeSetGreenPoints(long self, int[] arr);

    /**
     * Red native
     */

    private native void nativeGetRedCurves(long self, int[] arr);

    private native int[] nativeGetRedPoints(long self);

    private native boolean nativeSetRedPoints(long self, int[] arr);
}
