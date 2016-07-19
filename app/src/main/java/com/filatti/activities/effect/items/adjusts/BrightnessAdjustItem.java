package com.filatti.activities.effect.items.adjusts;

import com.filatti.R;
import com.filatti.activities.effect.items.ValueBarEffectItem;
import com.filatti.effects.EffectException;
import com.filatti.effects.adjusts.ContrastBrightnessAdjust;
import com.filatti.logger.Logger;

public class BrightnessAdjustItem extends ValueBarEffectItem<ContrastBrightnessAdjust, Integer> {
    private static final String TAG = BrightnessAdjustItem.class.getSimpleName();

    public BrightnessAdjustItem(ContrastBrightnessAdjust effect, OnEffectChangeListener listener) {
        super(effect, -100, 100, listener);
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
