package com.fotro.imgproc.adjustments;

import com.fotro.imgproc.ImgProcException;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AdjustmentFactory {
    private static final Map<String, Class<? extends Adjustment>> ADJUSTMENT_MAP = new HashMap<>();

    static {
        ADJUSTMENT_MAP.put(CurveAdjustment.NAME, CurveAdjustment.class);
        ADJUSTMENT_MAP.put(ContrastBrightnessAdjustment.NAME, ContrastBrightnessAdjustment.class);
        ADJUSTMENT_MAP.put(SaturationAdjustment.NAME, SaturationAdjustment.class);
        ADJUSTMENT_MAP.put(HistogramEqualizationAdjustment.NAME, HistogramEqualizationAdjustment.class);
        ADJUSTMENT_MAP.put(ColorGradientAdjustment.NAME, ColorGradientAdjustment.class);
        ADJUSTMENT_MAP.put(VignetteAdjustment.NAME, VignetteAdjustment.class);
    }

    public Adjustment importAdjustment(JSONObject object) throws ImgProcException {
        try {
            String adjustmentName = object.getString(Adjustment.ADJUSTMENT_KEY);
            Class<? extends Adjustment> clazz = ADJUSTMENT_MAP.get(adjustmentName);
            Adjustment adjustment = (clazz != null) ? clazz.newInstance() : new NoneAdjustment();
            adjustment.importObject(object);
            return adjustment;
        } catch (JSONException | InstantiationException | IllegalAccessException e) {
            throw new ImgProcException(e);
        }
    }
}
