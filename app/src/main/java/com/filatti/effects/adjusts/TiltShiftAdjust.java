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

    private final float[] mInitCenter;
    private final float mInitRadius;
    private final float mInitFeathering;
    private final float mInitStrength;
    private final float mInitAngle;
    private final MaskType mInitMaskType;

    public TiltShiftAdjust() {
        mNativeObj = nativeCreateObject();

        try {
            setRadius(0.5f);
            setFeathering(0.5f);
            setStrength(0);
            setCenter(0.5f, 0.5f);
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

    public float[] getInitCenter() {
        return Arrays.copyOf(mInitCenter, mInitCenter.length);
    }

    public float[] getCenter() {
        float[] center = new float[2];
        nativeGetCenter(mNativeObj, center);
        return center;
    }

    public void setCenter(float x, float y) throws EffectException {
        if (!nativeSetCenter(mNativeObj, x, y)) {
            throw new EffectException("Center isn't with range: x=" + x + ", y=" + y);
        }
    }

    public float getInitRadius() {
        return mInitRadius;
    }

    public float getRadius() {
        return nativeGetRadius(mNativeObj);
    }

    public void setRadius(float radius) throws EffectException {
        if (!nativeSetRadius(mNativeObj, radius)) {
            throw new EffectException("Radius isn't within range: " + radius);
        }
    }

    public float getInitStrength() {
        return mInitStrength;
    }

    public float getStrength() {
        return nativeGetStrength(mNativeObj);
    }

    public void setStrength(float strength) throws EffectException {
        if (!nativeSetStrength(mNativeObj, strength)) {
            throw new EffectException("Strength isn't within range: " + strength);
        }
    }

    public float getInitFeathering() {
        return mInitFeathering;
    }

    public float getFeathering() {
        return nativeGetFeathering(mNativeObj);
    }

    public void setFeathering(float feathering) throws EffectException {
        if (!nativeSetFeathering(mNativeObj, feathering)) {
            throw new EffectException("Feathering isn't within range: " + feathering);
        }
    }

    public float getInitAngle() {
        return mInitAngle;
    }

    public float getAngle() {
        return nativeGetAngle(mNativeObj);
    }

    public void setAngle(float angle) throws EffectException {
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
        float[] center = getCenter();
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

    private native void nativeGetCenter(long self, float[] center);

    private native boolean nativeSetCenter(long self, float x, float y);

    private native float nativeGetRadius(long self);

    private native boolean nativeSetRadius(long self, float radius);

    private native float nativeGetStrength(long self);

    private native boolean nativeSetStrength(long self, float strength);

    private native float nativeGetFeathering(long self);

    private native boolean nativeSetFeathering(long self, float feathering);

    private native float nativeGetAngle(long self);

    private native boolean nativeSetAngle(long self, float angle);

    private native int nativeGetMaskType(long self);

    private native void nativeSetMaskType(long self, int maskType);

    private native void nativeSetRebuildBlurred(long self, boolean doesRebuildBlurred);

    private native boolean nativeApply(long self, long nativeSrcMat, long nativeDstMat);
}
