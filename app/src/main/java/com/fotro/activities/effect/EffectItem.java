package com.fotro.activities.effect;

import com.fotro.effects.Effect;
import com.google.common.base.Preconditions;

class EffectItem {
    private final Effect mEffect;

    EffectItem(Effect effect) {
        Preconditions.checkNotNull(effect);
        mEffect = effect;
    }

    String getName() {
        return mEffect.getName();
    }
}

