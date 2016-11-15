package com.filatti.effects.adjusts;

import com.filatti.effects.EffectException;
import com.google.common.base.Preconditions;

import org.opencv.core.Mat;

import java.util.Arrays;

@SuppressWarnings("JniMissingFunction")
public class ColorBalanceAdjust extends Adjust {
    private final long mNativeObj;

    private final int[] mInitRedCyan;
    private final int[] mInitGreenMagenta;
    private final int[] mInitBlueYellow;

    public ColorBalanceAdjust() {
        mNativeObj = nativeCreateObject();

        mInitRedCyan = new int[3];
        mInitRedCyan[Tone.SHADOWS.toInt()] = getRedCyan(Tone.SHADOWS);
        mInitRedCyan[Tone.MIDTONES.toInt()] = getRedCyan(Tone.MIDTONES);
        mInitRedCyan[Tone.HIGHLIGHTS.toInt()] = getRedCyan(Tone.HIGHLIGHTS);

        mInitGreenMagenta = new int[3];
        mInitGreenMagenta[Tone.SHADOWS.toInt()] = getGreenMagenta(Tone.SHADOWS);
        mInitGreenMagenta[Tone.MIDTONES.toInt()] = getGreenMagenta(Tone.MIDTONES);
        mInitGreenMagenta[Tone.HIGHLIGHTS.toInt()] = getGreenMagenta(Tone.HIGHLIGHTS);

        mInitBlueYellow = new int[3];
        mInitBlueYellow[Tone.SHADOWS.toInt()] = getBlueYellow(Tone.SHADOWS);
        mInitBlueYellow[Tone.MIDTONES.toInt()] = getBlueYellow(Tone.MIDTONES);
        mInitBlueYellow[Tone.HIGHLIGHTS.toInt()] = getBlueYellow(Tone.HIGHLIGHTS);
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

    public int getInitRedCyan(Tone tone) {
        Preconditions.checkNotNull(tone);
        return mInitRedCyan[tone.toInt()];
    }

    public int getRedCyan(Tone tone) {
        Preconditions.checkNotNull(tone);
        return nativeGetRedCyan(mNativeObj, tone.toInt());
    }

    public void setRedCyan(Tone tone, int value) throws EffectException {
        Preconditions.checkNotNull(tone);
        if (!nativeSetRedCyan(mNativeObj, tone.toInt(), value)) {
            throw new EffectException("Red/Cyan value isn't within range: " + value);
        }
    }

    public int getInitGreenMagenta(Tone tone) {
        Preconditions.checkNotNull(tone);
        return mInitGreenMagenta[tone.toInt()];
    }

    public int getGreenMagenta(Tone tone) {
        Preconditions.checkNotNull(tone);
        return nativeGetGreenMagenta(mNativeObj, tone.toInt());
    }

    public void setGreenMagenta(Tone tone, int value) throws EffectException {
        Preconditions.checkNotNull(tone);
        if (!nativeSetGreenMagenta(mNativeObj, tone.toInt(), value)) {
            throw new EffectException("Red/Cyan value isn't within range: " + value);
        }
    }

    public int getInitBlueYellow(Tone tone) {
        Preconditions.checkNotNull(tone);
        return mInitBlueYellow[tone.toInt()];
    }

    public int getBlueYellow(Tone tone) {
        Preconditions.checkNotNull(tone);
        return nativeGetBlueYellow(mNativeObj, tone.toInt());
    }

    public void setBlueYellow(Tone tone, int value) throws EffectException {
        Preconditions.checkNotNull(tone);
        if (!nativeSetBlueYellow(mNativeObj, tone.toInt(), value)) {
            throw new EffectException("Red/Cyan value isn't within range: " + value);
        }
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
        int[] redCyan = new int[]{
                getRedCyan(Tone.SHADOWS),
                getRedCyan(Tone.MIDTONES),
                getRedCyan(Tone.HIGHLIGHTS)};

        int[] green_magenta = new int[]{
                getGreenMagenta(Tone.SHADOWS),
                getGreenMagenta(Tone.MIDTONES),
                getGreenMagenta(Tone.HIGHLIGHTS)};

        int[] blue_yellow = new int[]{
                getBlueYellow(Tone.SHADOWS),
                getBlueYellow(Tone.MIDTONES),
                getBlueYellow(Tone.HIGHLIGHTS)};

        return "ColorBalanceAdjust: {\n"
                + "\tred_cyan: " + Arrays.toString(redCyan) + "\n"
                + "\tgreen_magenta: " + Arrays.toString(green_magenta) + "\n"
                + "\tblue_yellow: " + Arrays.toString(blue_yellow) + "\n"
                + "}";
    }

    private native long nativeCreateObject();

    private native void nativeDestroyObject(long self);

    private native boolean nativeHasEffect(long self);

    private native int nativeGetRedCyan(long self, int tone);

    private native boolean nativeSetRedCyan(long self, int tone, int value);

    private native int nativeGetGreenMagenta(long self, int tone);

    private native boolean nativeSetGreenMagenta(long self, int tone, int value);

    private native int nativeGetBlueYellow(long self, int tone);

    private native boolean nativeSetBlueYellow(long self, int tone, int value);

    private native boolean nativeApply(long self, long nativeSrcMat, long nativeDstMat);
}
