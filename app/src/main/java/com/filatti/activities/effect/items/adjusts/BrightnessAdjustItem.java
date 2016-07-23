package com.filatti.activities.effect.items.adjusts;

import com.filatti.R;
import com.filatti.activities.effect.items.ValueBarEffectItem;
import com.filatti.effects.EffectException;
import com.filatti.effects.adjusts.BrightnessAdjust;
import com.filatti.logger.Logger;

public class BrightnessAdjustItem extends ValueBarEffectItem<BrightnessAdjust, Double> {
    private static final String TAG = BrightnessAdjustItem.class.getSimpleName();

    public BrightnessAdjustItem(BrightnessAdjust effect, OnEffectChangeListener listener) {
        super(effect, -100, 100, listener);
    }

    @Override
    protected Double getEffectValue() {
        return mEffect.getBrightness();
    }

    @Override
    protected void setEffectValue(Double effectValue) {
        try {
            mEffect.setBrightness(effectValue);
        } catch (EffectException e) {
            Logger.error(TAG, e);
        }
    }

    @Override
    protected Double toEffectValue(int barValue) {
        return barValue / 200.0;
    }

    @Override
    protected int fromEffectValue(Double effectValue) {
        return (int) (effectValue * 200);
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
