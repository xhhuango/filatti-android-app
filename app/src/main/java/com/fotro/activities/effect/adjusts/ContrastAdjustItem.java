package com.fotro.activities.effect.adjusts;

import com.fotro.R;
import com.fotro.effects.Effect;

public class ContrastAdjustItem extends AdjustItem {
    public ContrastAdjustItem(Effect effect) {
        super(effect);
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
