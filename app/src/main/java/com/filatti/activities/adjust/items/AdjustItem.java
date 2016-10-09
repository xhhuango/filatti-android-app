package com.filatti.activities.adjust.items;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.view.View;
import android.view.ViewGroup;

import com.filatti.activities.adjust.AdjustAction;
import com.filatti.effects.Effect;
import com.google.common.base.Preconditions;

public abstract class AdjustItem<T extends Effect> implements AdjustAction {
    final T mEffect;
    OnAdjustListener mOnAdjustListener;

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

    public abstract View getView(Context context, ViewGroup rootView);

    public abstract View getOverlayView(Context context, ViewGroup rootView);

    public boolean doesApplyUponAdjusting() {
        return true;
    }

    public void setOnAdjustListener(OnAdjustListener listener) {
        mOnAdjustListener = listener;
    }

    public interface OnAdjustListener {
        void onAdjustStart();

        void onAdjustStop();

        void onAdjustChange();
    }
}

