package com.filatti.effects;

import com.filatti.effects.adjusts.ContrastAdjust;
import com.filatti.effects.adjusts.CurvesAdjust;
import com.google.common.base.Preconditions;

import java.util.ArrayList;
import java.util.List;

public final class EffectManager {
    private List<Effect> mEffectList = new ArrayList<>();

    public EffectManager() {
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
}
