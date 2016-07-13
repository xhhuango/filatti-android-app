package com.fotro.activities.effect.items.adjusts;

import com.fotro.R;
import com.fotro.activities.effect.items.ValueBarEffectItem;
import com.fotro.effects.EffectException;
import com.fotro.effects.adjusts.SharpenAdjust;
import com.fotro.logger.Logger;

public class SharpenAdjustItem extends ValueBarEffectItem<SharpenAdjust, Double> {
    private static final String TAG = SharpenAdjustItem.class.getSimpleName();

    public SharpenAdjustItem(SharpenAdjust effect, OnEffectChangeListener listener) {
        super(effect, 0, 100, listener);
    }

    @Override
    protected Double getEffectValue() {
        return mEffect.getSharpen();
    }

    @Override
    protected void setEffectValue(Double effectValue) {
        try {
            mEffect.setSharpe(effectValue);
        } catch (EffectException e) {
            Logger.error(TAG, e);
        }
    }

    @Override
    protected Double toEffectValue(int barValue) {
        return barValue / 100.0;
    }

    @Override
    protected int fromEffectValue(Double effectValue) {
        return (int) (effectValue * 100);
    }

    @Override
    public int getDisplayName() {
        return R.string.sharpen;
    }

    @Override
    public int getIcon() {
        return R.drawable.sharpen;
    }
}
