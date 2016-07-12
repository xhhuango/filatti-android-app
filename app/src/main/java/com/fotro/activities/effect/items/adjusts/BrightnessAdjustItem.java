package com.fotro.activities.effect.items.adjusts;

import com.fotro.R;
import com.fotro.activities.effect.items.ValueBarEffectItem;
import com.fotro.effects.EffectException;
import com.fotro.effects.adjusts.ContrastBrightnessAdjust;
import com.fotro.logger.Logger;

public class BrightnessAdjustItem extends ValueBarEffectItem<ContrastBrightnessAdjust, Integer> {
    private static final String TAG = BrightnessAdjustItem.class.getSimpleName();

    public BrightnessAdjustItem(ContrastBrightnessAdjust effect, OnEffectChangeListener listener) {
        super(effect, listener);
    }

    @Override
    protected Integer getEffectValue() {
        return mEffect.getBrightness();
    }

    @Override
    protected void setEffectValue(Integer effectValue) {
        try {
            mEffect.setBrightness(effectValue);
        } catch (EffectException e) {
            Logger.error(TAG, e);
        }
    }

    @Override
    protected Integer toEffectValue(int barValue) {
        return barValue;
    }

    @Override
    protected int fromEffectValue(Integer effectValue) {
        return effectValue;
    }

    @Override
    public int getDisplayName() {
        return R.string.brightness;
    }

    @Override
    public int getIcon() {
        return R.drawable.brightness;
    }
}
