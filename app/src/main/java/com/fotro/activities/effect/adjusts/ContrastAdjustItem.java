package com.fotro.activities.effect.adjusts;

import android.content.Context;
import android.view.View;

import com.fotro.R;
import com.fotro.activities.effect.EffectItem;
import com.fotro.activities.effect.ui.ValueBar;
import com.fotro.effects.adjusts.ContrastBrightnessAdjust;

public class ContrastAdjustItem extends EffectItem<ContrastBrightnessAdjust> {
    public ContrastAdjustItem(ContrastBrightnessAdjust effect, OnEffectChangeListener listener) {
        super(effect, listener);
    }

    @Override
    public int getDisplayName() {
        return R.string.contrast;
    }

    @Override
    public int getIcon() {
        return R.drawable.contrast;
    }

    @Override
    public void apply() {

    }

    @Override
    public void cancel() {

    }

    @Override
    public void reset() {

    }

    @Override
    public View getView(Context context) {
        ValueBar valueBar = new ValueBar(context);
        return valueBar;
    }
}
