package com.fotro.effects.adjustments.simple;

import com.fotro.effects.adjustments.IllegalAdjustmentParameter;
import com.fotro.effects.adjustments.core.ContrastBrightnessAdjustment;
import com.google.common.base.Preconditions;

public class SimpleContrastBrightnessAdjustment extends ContrastBrightnessAdjustment {
    /**
     * CONTRAST: [-100, 0, 100]
     * value == 0: no change
     * -100 <= value < 0: decrease contrast
     * 0 < value <= 100: increase contrast
     */
    @Override
    public void setContrast(double contrast) throws IllegalAdjustmentParameter {
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
    public void setBrightness(int brightness) throws IllegalAdjustmentParameter {
        Preconditions.checkArgument(brightness >= -100 && brightness <= 100);
        super.setBrightness(brightness);
    }
}
