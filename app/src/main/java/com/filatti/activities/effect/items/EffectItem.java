package com.filatti.activities.effect.items;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.view.View;
import android.view.ViewGroup;

import com.filatti.effects.Effect;
import com.google.common.base.Preconditions;

public abstract class EffectItem<T extends Effect> {
    protected final T mEffect;
    protected final OnEffectChangeListener mOnEffectChangeListener;

    protected EffectItem(T effect, OnEffectChangeListener listener) {
        Preconditions.checkNotNull(effect);
        Preconditions.checkNotNull(listener);

        mEffect = effect;
        mOnEffectChangeListener = listener;
    }

    @StringRes
    public abstract int getDisplayName();

    @DrawableRes
    public abstract int getIcon();

    public abstract void apply();

    public abstract void cancel();

    public abstract void reset();

    public abstract View getView(Context context, ViewGroup rootView);

    public interface OnEffectChangeListener {
        void onEffectChanged();
    }
}

