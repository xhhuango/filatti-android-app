package com.filatti.effects.adjusts;

import com.filatti.effects.EffectException;
import com.google.common.base.Preconditions;

import org.opencv.core.Mat;

import java.util.Arrays;

import timber.log.Timber;

@SuppressWarnings("JniMissingFunction")
public class TiltShiftAdjust extends Adjust {
    public enum MaskType {
        CIRCULAR(0),
        ELLIPTIC(1),
        LINEAR(2);

        final int mValue;

        MaskType(int value) {
            mValue = value;
        }

        static MaskType valueOf(int value) {
            for (MaskType maskType : values()) {
                if (maskType.mValue == value) {
                    return maskType;
                }
            }
            return null;
        }
    }

    private final long mNativeObj;

    private final double[] mInitCenter;
    private final double mInitRadius;
    private final double mInitFeathering;
    private final double mInitStrength;
    private final double mInitAngle;
    private final MaskType mInitMaskType;

    public TiltShiftAdjust() {
        mNativeObj = nativeCreateObject();

        try {
            setRadius(0.5);
            setFeathering(0.5);
            setStrength(0);
            setCenter(0.5, 0.5);
            setAngle(0);
            setMaskType(MaskType.CIRCULAR);
        } catch (EffectException e) {
            Timber.e(e, "Failed to initialize TiltShift");
        }

        mInitCenter = getCenter();
        mInitRadius = getRadius();
        mInitFeathering = getFeathering();
        mInitStrength = getStrength();
        mInitAngle = getAngle();
        mInitMaskType = getMaskType();
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

    public double getInitAngle() {
        return mInitAngle;
    }

    public double getAngle() {
        return nativeGetAngle(mNativeObj);
    }

    public void setAngle(double angle) throws EffectException {
        if (!nativeSetAngle(mNativeObj, angle)) {
            throw new EffectException("Angle isn't within range: " + angle);
        }
    }

    public void setRebuildBlurred(boolean doesRebuildBlurred) {
        nativeSetRebuildBlurred(mNativeObj, doesRebuildBlurred);
    }

    public MaskType getInitMaskType() {
        return mInitMaskType;
    }

    public MaskType getMaskType() {
        int maskType = nativeGetMaskType(mNativeObj);
        return MaskType.valueOf(maskType);
    }

    public void setMaskType(MaskType maskType) {
        Preconditions.checkNotNull(maskType);
        nativeSetMaskType(mNativeObj, maskType.mValue);
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
        return "TiltShiftAdjust: {\n"
                + "\tcenter: {x: " + center[0] + ", y: " + center[1] + "}\n"
                + "\tradius: " + getRadius() + "\n"
                + "\tfeathering: " + getFeathering() + "\n"
                + "\tstrength: " + getStrength() + "\n"
                + "\tangle: " + getAngle() + "\n"
                + "\tmask type: " + getMaskType() + "\n"
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

    private native double nativeGetAngle(long self);

    private native boolean nativeSetAngle(long self, double angle);

    private native int nativeGetMaskType(long self);

    private native void nativeSetMaskType(long self, int maskType);

    private native void nativeSetRebuildBlurred(long self, boolean doesRebuildBlurred);

    private native boolean nativeApply(long self, long nativeSrcMat, long nativeDstMat);
}
