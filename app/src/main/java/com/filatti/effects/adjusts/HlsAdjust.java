package com.filatti.effects.adjusts;

import com.filatti.effects.EffectException;
import com.google.common.base.Preconditions;

import org.opencv.core.Mat;

@SuppressWarnings("JniMissingFunction")
public class HlsAdjust extends Adjust {
    private final long mNativeObj;

    private final int mInitHue;
    private final float mInitLightness;
    private final float mInitSaturation;

    public HlsAdjust() {
        mNativeObj = nativeCreateObject();

        mInitHue = getHue();
        mInitLightness = getLightness();
        mInitSaturation = getSaturation();
    }

    @Override
    protected void finalize() throws Throwable {
        nativeDestroyObject(mNativeObj);
        super.finalize();
    }

    public void setHue(int hue) throws EffectException {
        if (!nativeSetHue(mNativeObj, hue)) {
            throw new EffectException("Hue isn't within range: " + hue);
        }
    }

    public int getHue() {
        return nativeGetHue(mNativeObj);
    }

    public int getInitHue() {
        return mInitHue;
    }

    public void setLightness(float lightness) throws EffectException {
        if (!nativeSetLightness(mNativeObj, lightness)) {
            throw new EffectException("Lightness isn't within range: " + lightness);
        }
    }

    public float getLightness() {
        return nativeGetLightness(mNativeObj);
    }

    public float getInitLightness() {
        return mInitLightness;
    }

    public void setSaturation(float saturation) throws EffectException {
        if (!nativeSetSaturation(mNativeObj, saturation)) {
            throw new EffectException("Saturation isn't within range: " + saturation);
        }
    }

    public float getSaturation() {
        return nativeGetSaturation(mNativeObj);
    }

    public float getInitSaturation() {
        return mInitSaturation;
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
        return "HlsAdjust: {\n"
                + "\thue: " + getHue() + "\n"
                + "\tlightness: " + getLightness() + "\n"
                + "\tsaturation: " + getSaturation() + "\n"
                + "}";
    }

    private native long nativeCreateObject();

    private native void nativeDestroyObject(long self);

    private native int nativeGetHue(long self);

    private native boolean nativeSetHue(long self, int hue);

    private native float nativeGetLightness(long self);

    private native boolean nativeSetLightness(long self, float lightness);

    private native float nativeGetSaturation(long self);

    private native boolean nativeSetSaturation(long self, float saturation);

    private native boolean nativeApply(long self, long nativeSrcMat, long nativeDstMat);
}
