package com.fotro.activities.effect.items;

import android.content.Context;
import android.view.View;

import com.fotro.activities.effect.ui.ValueBar;
import com.fotro.effects.adjusts.ContrastBrightnessAdjust;

public abstract class ValueBarEffectItem<T> extends EffectItem<ContrastBrightnessAdjust> {
    private int mOriginalValue;
    private int mAppliedValue;
    private int mTemporaryValue;
    private ValueBar mValueBar;

    protected abstract T getEffectValue();

    protected abstract void setEffectValue(T effectValue);

    protected abstract T toEffectValue(int barValue);

    protected abstract int fromEffectValue(T effectValue);

    protected ValueBarEffectItem(ContrastBrightnessAdjust effect, OnEffectChangeListener listener) {
        super(effect, listener);
        mOriginalValue = fromEffectValue(getEffectValue());
        mAppliedValue = mOriginalValue;
        mTemporaryValue = mOriginalValue;
    }

    @Override
    public void apply() {
        mAppliedValue = mTemporaryValue;
    }

    @Override
    public void cancel() {
        if (mTemporaryValue != mAppliedValue) {
            mTemporaryValue = mAppliedValue;
            show();
        }
    }

    @Override
    public void reset() {
        if (mOriginalValue != mAppliedValue || mOriginalValue != mTemporaryValue) {
            mTemporaryValue = mOriginalValue;
            mValueBar.setValue(mTemporaryValue);
            show();
        }
    }

    @Override
    public View getView(Context context) {
        mValueBar = new ValueBar(context);
        mValueBar.initialize(-100, 100, mAppliedValue, new ValueBar.OnValueChangeListener() {
            @Override
            public void onValueChanged(int value) {
                mTemporaryValue = value;
                show();
            }
        });
        return mValueBar;
    }

    protected void show() {
        setEffectValue(toEffectValue(mTemporaryValue));
        mOnEffectChangeListener.onEffectChanged();
    }
}
