package com.fotro.activities.effect;

import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;

import com.fotro.effects.Effect;
import com.google.common.base.Preconditions;

public abstract class EffectItem {
    protected final Effect mEffect;

    protected EffectItem(Effect effect) {
        Preconditions.checkNotNull(effect);
        mEffect = effect;
    }

    @StringRes
    public abstract int getDisplayName();

    @DrawableRes
    public abstract int getIcon();

    public String getName() {
        return mEffect.getName();
    }
}

