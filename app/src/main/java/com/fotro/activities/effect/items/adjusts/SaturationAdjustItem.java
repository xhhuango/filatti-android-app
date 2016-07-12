package com.fotro.activities.effect.items.adjusts;

import com.fotro.R;
import com.fotro.activities.effect.items.ValueBarEffectItem;
import com.fotro.effects.EffectException;
import com.fotro.effects.adjusts.SaturationAdjust;
import com.fotro.logger.Logger;

public class SaturationAdjustItem extends ValueBarEffectItem<SaturationAdjust, Double> {
    private static final String TAG = SaturationAdjustItem.class.getSimpleName();

    public SaturationAdjustItem(SaturationAdjust effect, OnEffectChangeListener listener) {
        super(effect, listener);
    }

    @Override
    protected Double getEffectValue() {
        return mEffect.getSaturation();
    }

    @Override
    protected void setEffectValue(Double effectValue) {
        try {
            mEffect.setSaturation(effectValue);
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
        return R.string.saturation;
    }

    @Override
    public int getIcon() {
        return R.drawable.saturation;
    }
}
