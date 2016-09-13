package com.filatti.effects.adjusts;

import android.graphics.Color;
import android.support.annotation.ColorInt;

import com.filatti.effects.EffectException;
import com.google.common.base.Preconditions;

import org.opencv.core.Mat;

import java.util.Arrays;

import timber.log.Timber;

@SuppressWarnings("JniMissingFunction")
public class VignetteAdjust extends Adjust {
    private final long mNativeObj;

    private final double mInitRadius;
    private final double mInitFeathering;
    private final double mInitStrength;
    private final double[] mInitCenter;
    @ColorInt
    private final int mInitColor;
    private final boolean mInitIsFitToImage;

    public VignetteAdjust() {
        mNativeObj = nativeCreateObject();

        try {
            setRadius(1.20);
            setFeathering(0.25);
            setStrength(0);
            setCenter(0.5, 0.5);
            setColor(Color.BLACK);
            setFitToImage(true);
        } catch (EffectException e) {
            Timber.e(e, "Failed to initialize Vignette");
        }

        mInitRadius = getRadius();
        mInitFeathering = getFeathering();
        mInitStrength = getStrength();
        mInitCenter = getCenter();
        mInitColor = getColor();
        mInitIsFitToImage = isFitToImage();
    }

    @Override
    protected void finalize() throws Throwable {
        nativeDestroyObject(mNativeObj);
        super.finalize();
    }

    public double[] getInitCenter() {
        return Arrays.copyOf(mInitCenter, mInitCenter.length);
    }

    public double[] getCenter() {
        double[] center = new double[2];
        nativeGetCenter(mNativeObj, center);
        return center;
    }

    public void setCenter(double x, double y) throws EffectException {
        if (!nativeSetCenter(mNativeObj, x, y)) {
            throw new EffectException("Center isn't with range: x=" + x + ", y=" + y);
        }
    }

    public double getInitRadius() {
        return mInitRadius;
    }

    public double getRadius() {
        return nativeGetRadius(mNativeObj);
    }

    public void setRadius(double radius) throws EffectException {
        if (!nativeSetRadius(mNativeObj, radius)) {
            throw new EffectException("Radius isn't within range: " + radius);
        }
    }

    public double getInitStrength() {
        return mInitStrength;
    }

    public double getStrength() {
        return nativeGetStrength(mNativeObj);
    }

    public void setStrength(double strength) throws EffectException {
        if (!nativeSetStrength(mNativeObj, strength)) {
            throw new EffectException("Strength isn't within range: " + strength);
        }
    }

    public double getInitFeathering() {
        return mInitFeathering;
    }

    public double getFeathering() {
        return nativeGetFeathering(mNativeObj);
    }

    public void setFeathering(double feathering) throws EffectException {
        if (!nativeSetFeathering(mNativeObj, feathering)) {
            throw new EffectException("Feathering isn't within range: " + feathering);
        }
    }

    @ColorInt
    public int getInitColor() {
        return mInitColor;
    }

    @ColorInt
    public int getColor() {
        int[] color = new int[3];
        nativeGetColor(mNativeObj, color);
        return Color.argb(255, color[2], color[1], color[0]);
    }

    public void setColor(@ColorInt int color) throws EffectException {
        if (!nativeSetColor(mNativeObj, Color.blue(color), Color.green(color), Color.red(color))) {
            throw new EffectException("Color is not correct");
        }
    }

    public boolean getInitIsFitToImage() {
        return mInitIsFitToImage;
    }

    public boolean isFitToImage() {
        return nativeIsFitToImage(mNativeObj);
    }

    public void setFitToImage(boolean isFitToImage) {
        nativeSetFitToImage(mNativeObj, isFitToImage);
    }

    @Override
    public Mat apply(Mat src) {
        Preconditions.checkNotNull(src);

        Mat dst = new Mat();
        return (nativeApply(mNativeObj, src.getNativeObjAddr(), dst.getNativeObjAddr()))
                ? dst
                : src;
    }

    @Override
    public String toString() {
        double[] center = getCenter();
        return "VignetteAdjust: {\n"
                + "\tradius: " + getRadius() + "\n"
                + "\tfeathering: " + getFeathering() + "\n"
                + "\tstrength: " + getStrength() + "\n"
                + "\tcenter: { x: " + center[0] + ", y: " + center[1] + "}\n"
                + "\tcolor: " + Integer.toHexString(getColor()) + "\n"
                + "\tisFitToImage: " + isFitToImage() + "\n"
                + "}";
    }

    private native long nativeCreateObject();

    private native void nativeDestroyObject(long self);

    private native void nativeGetCenter(long self, double[] center);

    private native boolean nativeSetCenter(long self, double x, double y);

    private native double nativeGetRadius(long self);

    private native boolean nativeSetRadius(long self, double radius);

    private native double nativeGetStrength(long self);

    private native boolean nativeSetStrength(long self, double strength);

    private native double nativeGetFeathering(long self);

    private native boolean nativeSetFeathering(long self, double feathering);

    private native void nativeGetColor(long self, int[] color);

    private native boolean nativeSetColor(long self, int b, int g, int r);

    private native boolean nativeIsFitToImage(long self);

    private native void nativeSetFitToImage(long self, boolean isFitToImage);

    private native boolean nativeApply(long self, long nativeSrcMat, long nativeDstMat);
}
