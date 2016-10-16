package com.filatti.effects.adjusts;

import com.filatti.effects.EffectException;
import com.google.common.base.Preconditions;

import org.opencv.core.Mat;

@SuppressWarnings("JniMissingFunction")
public class HighlightShadowAdjust extends Adjust {
    private final long mNativeObj;

    private final double mInitHighlightAmount;
    private final double mInitHighlightToneWidth;
    private final double mInitShadowAmount;
    private final double mInitShadowToneWidth;

    public HighlightShadowAdjust() {
        mNativeObj = nativeCreateObject();

        mInitHighlightAmount = getAmount(Tone.HIGHLIGHTS);
        mInitHighlightToneWidth = getToneWidth(Tone.HIGHLIGHTS);
        mInitShadowAmount = getAmount(Tone.SHADOWS);
        mInitShadowToneWidth = getToneWidth(Tone.SHADOWS);
    }

    @Override
    protected void finalize() throws Throwable {
        nativeDestroyObject(mNativeObj);
        super.finalize();
    }

    public void setAmount(Tone tone, double amount) throws EffectException {
        Preconditions.checkNotNull(tone);
        if (!nativeSetAmount(mNativeObj, tone.toInt(), amount)) {
            throw new EffectException("Setting illegal amount value: " + amount);
        }
    }

    public double getAmount(Tone tone) {
        Preconditions.checkNotNull(tone);
        return nativeGetAmount(mNativeObj, tone.toInt());
    }

    public double getInitAmount(Tone tone) {
        Preconditions.checkNotNull(tone);
        return tone == Tone.HIGHLIGHTS ? mInitHighlightAmount : mInitShadowAmount;
    }

    public void setToneWidth(Tone tone, double width) throws EffectException {
        Preconditions.checkNotNull(tone);
        if (!nativeSetToneWidth(mNativeObj, tone.toInt(), width)) {
            throw new EffectException("Setting illegal tone width value: " + width);
        }
    }

    public double getToneWidth(Tone tone) {
        Preconditions.checkNotNull(tone);
        return nativeGetToneWidth(mNativeObj, tone.toInt());
    }

    public double getInitToneWidth(Tone tone) {
        Preconditions.checkNotNull(tone);
        return tone == Tone.HIGHLIGHTS ? mInitHighlightToneWidth : mInitShadowToneWidth;
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
        return "HighlightShadowAdjust: {\n"
                + "\thighlight amount: " + getAmount(Tone.HIGHLIGHTS) + "\n"
                + "\thighlight tone width: " + getToneWidth(Tone.HIGHLIGHTS) + "\n"
                + "\tshadow amount: " + getAmount(Tone.SHADOWS) + "\n"
                + "\tshadow tone width: " + getToneWidth(Tone.SHADOWS) + "\n"
                + "}";
    }

    private native long nativeCreateObject();

    private native void nativeDestroyObject(long self);

    private native double nativeGetAmount(long self, int tone);

    private native boolean nativeSetAmount(long self, int tone, double amount);

    private native double nativeGetToneWidth(long self, int tone);

    private native boolean nativeSetToneWidth(long self, int tone, double toneWidth);

    private native boolean nativeApply(long self, long nativeSrcMat, long nativeDstMat);
}
