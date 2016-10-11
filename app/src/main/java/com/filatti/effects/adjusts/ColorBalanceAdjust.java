package com.filatti.effects.adjusts;

import com.filatti.effects.EffectException;
import com.google.common.base.Preconditions;

import org.opencv.core.Mat;

import java.util.Arrays;

@SuppressWarnings("JniMissingFunction")
public class ColorBalanceAdjust extends Adjust {
    public enum Tone {
        SHADOWS(0),
        MIDTONES(1),
        HIGHLIGHTS(2);

        final int mValue;

        Tone(int value) {
            mValue = value;
        }

        public int toInt() {
            return mValue;
        }
    }

    private final long mNativeObj;

    private final int[] mInitRedCyan;
    private final int[] mInitGreenMagenta;
    private final int[] mInitBlueYellow;

    public ColorBalanceAdjust() {
        mNativeObj = nativeCreateObject();

        mInitRedCyan = new int[3];
        mInitRedCyan[Tone.SHADOWS.mValue] = getRedCyan(Tone.SHADOWS);
        mInitRedCyan[Tone.MIDTONES.mValue] = getRedCyan(Tone.MIDTONES);
        mInitRedCyan[Tone.HIGHLIGHTS.mValue] = getRedCyan(Tone.HIGHLIGHTS);

        mInitGreenMagenta = new int[3];
        mInitGreenMagenta[Tone.SHADOWS.mValue] = getGreenMagenta(Tone.SHADOWS);
        mInitGreenMagenta[Tone.MIDTONES.mValue] = getGreenMagenta(Tone.MIDTONES);
        mInitGreenMagenta[Tone.HIGHLIGHTS.mValue] = getGreenMagenta(Tone.HIGHLIGHTS);

        mInitBlueYellow = new int[3];
        mInitBlueYellow[Tone.SHADOWS.mValue] = getBlueYellow(Tone.SHADOWS);
        mInitBlueYellow[Tone.MIDTONES.mValue] = getBlueYellow(Tone.MIDTONES);
        mInitBlueYellow[Tone.HIGHLIGHTS.mValue] = getBlueYellow(Tone.HIGHLIGHTS);
    }

    @Override
    protected void finalize() throws Throwable {
        nativeDestroyObject(mNativeObj);
        super.finalize();
    }

    public int getInitRedCyan(Tone tone) {
        Preconditions.checkNotNull(tone);
        return mInitRedCyan[tone.mValue];
    }

    public int getRedCyan(Tone tone) {
        Preconditions.checkNotNull(tone);
        return nativeGetRedCyan(mNativeObj, tone.mValue);
    }

    public void setRedCyan(Tone tone, int value) throws EffectException {
        Preconditions.checkNotNull(tone);
        if (!nativeSetRedCyan(mNativeObj, tone.mValue, value)) {
            throw new EffectException("Red/Cyan value isn't within range: " + value);
        }
    }

    public int getInitGreenMagenta(Tone tone) {
        Preconditions.checkNotNull(tone);
        return mInitGreenMagenta[tone.mValue];
    }

    public int getGreenMagenta(Tone tone) {
        Preconditions.checkNotNull(tone);
        return nativeGetGreenMagenta(mNativeObj, tone.mValue);
    }

    public void setGreenMagenta(Tone tone, int value) throws EffectException {
        Preconditions.checkNotNull(tone);
        if (!nativeSetGreenMagenta(mNativeObj, tone.mValue, value)) {
            throw new EffectException("Red/Cyan value isn't within range: " + value);
        }
    }

    public int getInitBlueYellow(Tone tone) {
        Preconditions.checkNotNull(tone);
        return mInitBlueYellow[tone.mValue];
    }

    public int getBlueYellow(Tone tone) {
        Preconditions.checkNotNull(tone);
        return nativeGetBlueYellow(mNativeObj, tone.mValue);
    }

    public void setBlueYellow(Tone tone, int value) throws EffectException {
        Preconditions.checkNotNull(tone);
        if (!nativeSetBlueYellow(mNativeObj, tone.mValue, value)) {
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

    private native int nativeGetRedCyan(long self, int tone);

    private native boolean nativeSetRedCyan(long self, int tone, int value);

    private native int nativeGetGreenMagenta(long self, int tone);

    private native boolean nativeSetGreenMagenta(long self, int tone, int value);

    private native int nativeGetBlueYellow(long self, int tone);

    private native boolean nativeSetBlueYellow(long self, int tone, int value);

    private native boolean nativeApply(long self, long nativeSrcMat, long nativeDstMat);
}
