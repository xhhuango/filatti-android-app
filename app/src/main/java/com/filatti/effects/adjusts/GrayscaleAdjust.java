package com.filatti.effects.adjusts;

import com.filatti.effects.EffectException;
import com.google.common.base.Preconditions;

import org.opencv.core.Mat;

@SuppressWarnings("JniMissingFunction")
public class GrayscaleAdjust extends Adjust {
    public enum Mode {
        NONE(0),
        GRAY(1),
        BLACK_AND_WHITE(2),
        PENCIL_SKETCH(3);

        final int mValue;

        Mode(int value) {
            mValue = value;
        }

        static Mode valueOf(int value) {
            for (Mode mode : values()) {
                if (mode.mValue == value) {
                    return mode;
                }
            }
            return null;
        }
    }

    private final long mNativeObj;

    private final Mode mInitMode;
    private final int mInitBlurKernelSize;

    public GrayscaleAdjust() {
        mNativeObj = nativeCreateObject();

        mInitMode = getMode();
        mInitBlurKernelSize = getBlurKernelSize();
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

    public void setMode(Mode mode) {
        Preconditions.checkNotNull(mode);
        nativeSetMode(mNativeObj, mode.mValue);
    }

    public Mode getMode() {
        int mode = nativeGetMode(mNativeObj);
        return Mode.valueOf(mode);
    }

    public Mode getInitMode() {
        return mInitMode;
    }

    public void setBlurKernelSize(int blurKernelSize) throws EffectException {
        if (!nativeSetBlurKernelSize(mNativeObj, blurKernelSize)) {
            throw new EffectException("Setting illegal blur kernel size value: " + blurKernelSize);
        }
    }

    public int getBlurKernelSize() {
        return nativeGetBlurKernelSize(mNativeObj);
    }

    public int getInitBlurKernelSize() {
        return mInitBlurKernelSize;
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
        return "GrayscaleAdjust: {\n"
                + "\tmode: " + getMode() + "\n"
                + "\tblur kernel size: " + getBlurKernelSize() + "\n"
                + "}";
    }

    private native long nativeCreateObject();

    private native void nativeDestroyObject(long self);

    private native boolean nativeHasEffect(long self);

    private native int nativeGetMode(long self);

    private native void nativeSetMode(long self, int mode);

    private native int nativeGetBlurKernelSize(long self);

    private native boolean nativeSetBlurKernelSize(long self, int blurKernelSize);

    private native boolean nativeApply(long self, long nativeSrcMat, long nativeDstMat);
}
