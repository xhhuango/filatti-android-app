package com.filatti.effects;

import android.os.Debug;

import com.filatti.effects.Effect;
import com.filatti.effects.adjusts.ContrastAdjust;
import com.filatti.effects.adjusts.CurvesAdjust;
import com.filatti.utilities.Millis;
import com.google.common.base.Preconditions;

import org.opencv.core.Mat;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

public final class AdjustComposite implements Effect {
    private List<Effect> mEffectList = new ArrayList<>();

    public AdjustComposite() {
        mEffectList.add(new CurvesAdjust());
        mEffectList.add(new ContrastAdjust());
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

    public Mat apply(Mat src, Effect util) {
        Preconditions.checkNotNull(src);

        for (Effect effect : mEffectList) {
            if (util != null && effect.getClass().isInstance(util)) {
                break;
            }

            long before = Millis.now();
            Mat dst = effect.apply(src);
            long after = Millis.now();
            Timber.d("Spent %d ms, native heap=%d KB",
                     after - before,
                     Debug.getNativeHeapAllocatedSize() / 1000);
            Timber.d(effect.toString());

            if (src != dst) {
                src.release();
                src = dst;
            }
        }

        return src;
    }
}
