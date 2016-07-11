package com.fotro.activities.effect.items.adjusts;

import com.fotro.R;
import com.fotro.activities.effect.items.ValueBarEffectItem;
import com.fotro.effects.EffectException;
import com.fotro.effects.adjusts.ContrastBrightnessAdjust;
import com.fotro.logger.Logger;

public class ContrastAdjustItem extends ValueBarEffectItem<Double> {
    private static final String TAG = ContrastAdjustItem.class.getSimpleName();

    public ContrastAdjustItem(ContrastBrightnessAdjust effect, OnEffectChangeListener listener) {
        super(effect, listener);
    }

    @Override
    protected Double getEffectValue() {
        return mEffect.getContrast();
    }

    @Override
    protected void setEffectValue(Double effectValue) {
        try {
            mEffect.setContrast(effectValue);
        } catch (EffectException e) {
            Logger.error(TAG, e);
        }
    }

    @Override
    protected Double toEffectValue(int barValue) {
        return (barValue / 2.0 + 100) / 100.0;
    }

    @Override
    protected int fromEffectValue(Double effectValue) {
        return (int) (((effectValue * 100.0) - 100.0) * 2.0);
    }

    @Override
    public int getDisplayName() {
        return R.string.contrast;
    }

    @Override
    public int getIcon() {
        return R.drawable.contrast;
    }
}
