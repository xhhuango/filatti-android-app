package com.filatti.activities.effect.items;

import android.content.Context;
import android.view.View;

import com.filatti.activities.effect.ui.ValueBar;
import com.filatti.effects.Effect;
import com.google.common.base.Preconditions;

public abstract class ValueBarEffectItem<E extends Effect, T> extends EffectItem<E> {
    private final int mMinValue;
    private final int mMaxValue;

    private int mOriginalValue;
    private int mAppliedValue;
    private int mTemporaryValue;
    private ValueBar mValueBar;

    protected abstract T getEffectValue();

    protected abstract void setEffectValue(T effectValue);

    protected abstract T toEffectValue(int barValue);

    protected abstract int fromEffectValue(T effectValue);

    protected ValueBarEffectItem(E effect,
                                 int minValue,
                                 int maxValue,
                                 OnEffectChangeListener listener) {
        super(effect, listener);

        Preconditions.checkArgument(minValue < maxValue);
        mMinValue = minValue;
        mMaxValue = maxValue;

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
        mValueBar.initialize(mMinValue, mMaxValue, mAppliedValue, new ValueBar.OnValueChangeListener() {
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
