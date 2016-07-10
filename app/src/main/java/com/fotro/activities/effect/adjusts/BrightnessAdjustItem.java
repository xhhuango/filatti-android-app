package com.fotro.activities.effect.adjusts;

import android.content.Context;
import android.support.annotation.StringRes;
import android.view.View;

import com.fotro.R;
import com.fotro.activities.effect.EffectItem;
import com.fotro.activities.effect.ui.ValueBar;
import com.fotro.effects.Effect;
import com.fotro.effects.EffectException;
import com.fotro.effects.adjusts.ContrastBrightnessAdjust;
import com.fotro.logger.Logger;

public class BrightnessAdjustItem extends EffectItem<ContrastBrightnessAdjust> {
    private static final String TAG = BrightnessAdjustItem.class.getSimpleName();

    private int mOriginalValue;
    private int mAppliedValue;
    private int mTemperaryValue;

    public BrightnessAdjustItem(ContrastBrightnessAdjust effect, OnEffectChangeListener listener) {
        super(effect, listener);
        mOriginalValue = effect.getBrightness();
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
    public View getView(Context context) {
        ValueBar valueBar = new ValueBar(context);
        valueBar.setValueRange(100, -100, 0);
        valueBar.setOnValueChangeListener(new ValueBar.OnValueChangeListener() {
            @Override
            public void onValueChanged(int value) {
                try {
                    mTemperaryValue = value;
                    mEffect.setBrightness(value);
                    mOnEffectChangeListener.onEffectChanged();
                } catch (EffectException e) {
                    Logger.error(TAG, e);
                }
            }
        });
        return valueBar;
    }
}
