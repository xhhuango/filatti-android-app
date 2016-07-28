package com.filatti.activities.effect.items.adjusts;

import com.filatti.R;
import com.filatti.activities.effect.items.ValueBarEffectItem;
import com.filatti.effects.EffectException;
import com.filatti.effects.adjusts.VignetteAdjust;
import com.filatti.logger.Logger;

public class VignetteAdjustItem extends ValueBarEffectItem<VignetteAdjust, Double> {
    private static final String TAG = VignetteAdjustItem.class.getSimpleName();

    public VignetteAdjustItem(VignetteAdjust effect, OnEffectChangeListener listener) {
        super(effect, 0, 100, listener);
    }

    @Override
    protected Double getEffectValue() {
        return mEffect.getStrength();
    }

    @Override
    protected void setEffectValue(Double effectValue) {
        try {
            mEffect.setStrength(effectValue);
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
        return R.string.vignette;
    }

    @Override
    public int getIcon() {
        return R.drawable.vignette;
    }
}
