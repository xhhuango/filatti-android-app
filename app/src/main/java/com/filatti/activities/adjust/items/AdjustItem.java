package com.filatti.activities.adjust.items;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.view.View;
import android.view.ViewGroup;

import com.filatti.effects.Effect;
import com.google.common.base.Preconditions;

public abstract class AdjustItem<T extends Effect> {
    protected final T mEffect;
    protected OnAdjustListener mOnAdjustListener;

    protected AdjustItem(T effect) {
        Preconditions.checkNotNull(effect);
        mEffect = effect;
    }

    public T getEffect() {
        return mEffect;
    }

    @StringRes
    public abstract int getDisplayName();

    @DrawableRes
    public abstract int getIcon();

    public abstract void apply();

    public abstract void cancel();

    public abstract void reset();

    public abstract View getView(Context context, ViewGroup rootView);

    public void setOnAdjustListener(OnAdjustListener listener) {
        mOnAdjustListener = listener;
    }

    public interface OnAdjustListener {
        void onAdjustStart();

        void onAdjustStop();

        void onAdjustChange();
    }
}

