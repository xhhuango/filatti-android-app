package com.filatti.effects.adjusts;

import com.filatti.effects.EffectException;
import com.google.common.base.Preconditions;

import org.opencv.core.Mat;

import java.util.Arrays;

@SuppressWarnings("JniMissingFunction")
public class CurvesAdjust extends Adjust {
    private final long mNativeObj;

    private final int[] mInitValuePoints;
    private final int[] mInitBluePoints;
    private final int[] mInitGreenPoints;
    private final int[] mInitRedPoints;

    public CurvesAdjust() {
        mNativeObj = nativeCreateObject();

        mInitValuePoints = getValuePoints();
        mInitBluePoints = getBluePoints();
        mInitGreenPoints = getGreenPoints();
        mInitRedPoints = getRedPoints();
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

    /**
     * Value
     */

    public void getValueCurves(int[] curves) {
        Preconditions.checkNotNull(curves);
        Preconditions.checkArgument(curves.length == 256);
        nativeGetValueCurves(mNativeObj, curves);
    }

    public int[] getInitValuePoints() {
        return Arrays.copyOf(mInitValuePoints, mInitValuePoints.length);
    }

    public int[] getValuePoints() {
        return nativeGetValuePoints(mNativeObj);
    }

    public void setValuePoints(int[] points) throws EffectException {
        if (!nativeSetValuePoints(mNativeObj, points)) {
            throw new EffectException("Setting illegal value points: " + Arrays.toString(points));
        }
    }

    /**
     * Blue
     */

    public void getBlueCurves(int[] curves) {
        Preconditions.checkNotNull(curves);
        Preconditions.checkArgument(curves.length == 256);
        nativeGetBlueCurves(mNativeObj, curves);
    }

    public int[] getInitBluePoints() {
        return Arrays.copyOf(mInitBluePoints, mInitBluePoints.length);
    }

    public int[] getBluePoints() {
        return nativeGetBluePoints(mNativeObj);
    }

    public void setBluePoints(int[] points) throws EffectException {
        if (!nativeSetBluePoints(mNativeObj, points)) {
            throw new EffectException("Setting illegal blue points: " + Arrays.toString(points));
        }
    }

    /**
     * Green
     */

    public void getGreenCurves(int[] curves) {
        Preconditions.checkNotNull(curves);
        Preconditions.checkArgument(curves.length == 256);
        nativeGetGreenCurves(mNativeObj, curves);
    }

    public int[] getInitGreenPoints() {
        return Arrays.copyOf(mInitGreenPoints, mInitGreenPoints.length);
    }

    public int[] getGreenPoints() {
        return nativeGetGreenPoints(mNativeObj);
    }

    public void setGreenPoints(int[] points) throws EffectException {
        if (!nativeSetGreenPoints(mNativeObj, points)) {
            throw new EffectException("Setting illegal green points: " + Arrays.toString(points));
        }
    }

    /**
     * Red
     */

    public void getRedCurves(int[] curves) {
        Preconditions.checkNotNull(curves);
        Preconditions.checkArgument(curves.length == 256);
        nativeGetRedCurves(mNativeObj, curves);
    }

    public int[] getInitRedPoints() {
        return Arrays.copyOf(mInitRedPoints, mInitRedPoints.length);
    }

    public int[] getRedPoints() {
        return nativeGetRedPoints(mNativeObj);
    }

    public void setRedPoints(int[] points) throws EffectException {
        if (!nativeSetRedPoints(mNativeObj, points)) {
            throw new EffectException("Setting illegal red points: " + Arrays.toString(points));
        }
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
        return "CurvesAdjust: {\n"
                + "\tvalue: " + Arrays.toString(getValuePoints()) + ",\n"
                + "\tblue: " + Arrays.toString(getBluePoints()) + ",\n"
                + "\tgreen: " + Arrays.toString(getGreenPoints()) + ",\n"
                + "\tred: " + Arrays.toString(getRedPoints()) + "\n"
                + "}";
    }

    private native long nativeCreateObject();

    private native void nativeDestroyObject(long self);

    private native boolean nativeHasEffect(long self);

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
