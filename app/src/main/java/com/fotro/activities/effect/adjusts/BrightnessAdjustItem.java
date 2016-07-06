package com.fotro.activities.effect.adjusts;

import com.fotro.R;
import com.fotro.effects.Effect;

public class BrightnessAdjustItem extends AdjustItem {
    public BrightnessAdjustItem(Effect effect) {
        super(effect);
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
