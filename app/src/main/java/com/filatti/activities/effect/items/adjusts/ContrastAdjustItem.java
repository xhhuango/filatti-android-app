package com.filatti.activities.effect.items.adjusts;

import com.filatti.R;
import com.filatti.activities.effect.items.ValueBarEffectItem;
import com.filatti.effects.EffectException;
import com.filatti.effects.adjusts.ContrastAdjust;
import com.filatti.logger.Logger;

public class ContrastAdjustItem extends ValueBarEffectItem<ContrastAdjust, Double> {
    private static final String TAG = ContrastAdjustItem.class.getSimpleName();

    public ContrastAdjustItem(ContrastAdjust effect, OnEffectChangeListener listener) {
        super(effect, -100, 100, listener);
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
        return (barValue / 2.0 + 100.0) / 100.0;
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
