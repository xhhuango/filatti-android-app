package com.filatti.effects;

import android.os.Debug;

import com.filatti.effects.adjusts.ColorBalanceAdjust;
import com.filatti.effects.adjusts.ContrastAdjust;
import com.filatti.effects.adjusts.CurvesAdjust;
import com.filatti.effects.adjusts.HighlightShadowAdjust;
import com.filatti.effects.adjusts.HlsAdjust;
import com.filatti.effects.adjusts.SharpnessAdjust;
import com.filatti.effects.adjusts.TemperatureAdjust;
import com.filatti.effects.adjusts.TiltShiftAdjust;
import com.filatti.effects.adjusts.VignetteAdjust;
import com.filatti.effects.adjusts.WhiteBalanceAdjust;
import com.filatti.utilities.Millis;
import com.google.common.base.Preconditions;

import org.opencv.core.Mat;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

public final class AdjustComposite implements Effect {
    private List<Effect> mEffectList = new ArrayList<>();

    public AdjustComposite() {
        mEffectList.add(new WhiteBalanceAdjust());
        mEffectList.add(new CurvesAdjust());
        mEffectList.add(new ColorBalanceAdjust());
        mEffectList.add(new HlsAdjust());
        mEffectList.add(new HighlightShadowAdjust());
        mEffectList.add(new ContrastAdjust());
        mEffectList.add(new TemperatureAdjust());
        mEffectList.add(new SharpnessAdjust());
        mEffectList.add(new VignetteAdjust());
        mEffectList.add(new TiltShiftAdjust());
    }

    public <T> T getEffect(Class<T> clazz) {
        Preconditions.checkNotNull(clazz);
        for (Effect effect : mEffectList) {
            if (clazz.isInstance(effect)) {
                return (T) effect;
            }
        }
        return null;
    }

    public List<Effect> list() {
        return mEffectList;
    }

    @Override
    public Mat apply(Mat src) {
        return apply(src, null);
    }

    public Mat apply(Mat src, Effect until) {
        Preconditions.checkNotNull(src);

        long beforeInTotal = Millis.now();
        for (Effect effect : mEffectList) {
            if (until != null && effect.getClass().isInstance(until)) {
                break;
            }

            long before = Millis.now();
            Mat dst = effect.apply(src);
            long after = Millis.now();
            Timber.d("%s spent %d ms, native heap=%d KB %s",
                     effect.getClass().getSimpleName(),
                     after - before,
                     Debug.getNativeHeapAllocatedSize() / 1000,
                     src == dst ? "SKIP" : "");
            Timber.d(effect.toString());

            if (src != dst) {
                src.release();
                src = dst;
            }
        }
        long afterInTotal = Millis.now();
        Timber.d("Spent %d ms in total", afterInTotal - beforeInTotal);

        return src;
    }
}
