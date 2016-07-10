package com.fotro.activities.effect.adjusts;

import android.content.Context;
import android.view.View;

import com.fotro.R;
import com.fotro.activities.effect.EffectItem;
import com.fotro.activities.effect.ui.ValueBar;
import com.fotro.effects.EffectException;
import com.fotro.effects.adjusts.ContrastBrightnessAdjust;
import com.fotro.logger.Logger;

public class BrightnessAdjustItem extends EffectItem<ContrastBrightnessAdjust> {
    private static final String TAG = BrightnessAdjustItem.class.getSimpleName();

    private int mOriginalValue;
    private int mAppliedValue;
    private int mTemporaryValue;
    private ValueBar mValueBar;

    public BrightnessAdjustItem(ContrastBrightnessAdjust effect, OnEffectChangeListener listener) {
        super(effect, listener);
        mOriginalValue = effect.getBrightness();
        mAppliedValue = mOriginalValue;
        mTemporaryValue = mOriginalValue;
    }

    @Override
    public int getDisplayName() {
        return R.string.brightness;
    }

    @Override
    public int getIcon() {
        return R.drawable.brightness;
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
        mValueBar.initialize(100, -100, mAppliedValue, new ValueBar.OnValueChangeListener() {
            @Override
            public void onValueChanged(int value) {
                mTemporaryValue = value;
                show();
            }
        });
        return mValueBar;
    }

    private void show() {
        try {
            mEffect.setBrightness(mTemporaryValue);
            mOnEffectChangeListener.onEffectChanged();
        } catch (EffectException e) {
            Logger.error(TAG, e);
        }
    }
}
