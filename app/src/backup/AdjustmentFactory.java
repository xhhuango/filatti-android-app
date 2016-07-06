package com.fotro.effects.adjusts;

import com.fotro.effects.ImageProcessingException;
import com.fotro.effects.adjusts.core.ContrastBrightnessAdjustment;
import com.fotro.effects.adjusts.core.NoneAdjustment;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AdjustmentFactory {
    private static final Map<String, Class<? extends Adjustment>> ADJUSTMENT_MAP = new HashMap<>();

    static {
        ADJUSTMENT_MAP.put(ContrastBrightnessAdjustment.NAME, ContrastBrightnessAdjustment.class);
    }

    public Adjustment importAdjustment(JSONObject object) throws ImageProcessingException {
        try {
            String adjustmentName = object.getString(Adjustment.ADJUST);
            Class<? extends Adjustment> clazz = ADJUSTMENT_MAP.get(adjustmentName);
            Adjustment adjustment = (clazz != null) ? clazz.newInstance() : new NoneAdjustment();
            adjustment.importJson(object);
            return adjustment;
        } catch (JSONException | InstantiationException | IllegalAccessException e) {
            throw new ImageProcessingException(e);
        }
    }
}
