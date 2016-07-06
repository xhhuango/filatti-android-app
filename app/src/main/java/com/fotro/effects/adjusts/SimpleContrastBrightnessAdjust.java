package com.fotro.effects.adjusts;

import com.fotro.effects.EffectException;
import com.google.common.base.Preconditions;

public class SimpleContrastBrightnessAdjust extends ContrastBrightnessAdjust {
    /**
     * CONTRAST: [-100, 0, 100]
     * value == 0: no change
     * -100 <= value < 0: decrease contrast
     * 0 < value <= 100: increase contrast
     */
    @Override
    public void setContrast(double contrast) throws EffectException {
        Preconditions.checkArgument(contrast >= -100 && contrast <= 100);
        super.setContrast((contrast / 2.0 + 100.0) / 100.0);

    }

    /**
     * BRIGHTNESS: [-100, 0, 100]
     * value == 0: no change
     * -100 <= value < 0: decrease brightness
     * 0 < value <= 100: increase contrast
     */
    @Override
    public void setBrightness(int brightness) throws EffectException {
        Preconditions.checkArgument(brightness >= -100 && brightness <= 100);
        super.setBrightness(brightness);
    }
}
