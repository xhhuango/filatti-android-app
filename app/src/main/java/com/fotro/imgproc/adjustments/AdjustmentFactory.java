package com.fotro.imgproc.adjustments;

import com.fotro.imgproc.ImgProcException;

import org.json.JSONException;
import org.json.JSONObject;

public class AdjustmentFactory {
    public Adjustment create(JSONObject object) throws ImgProcException {
        try {
            Adjustment adjustment;
            switch (object.getString(Adjustment.ADJUSTMENT_KEY)) {
                case CurveAdjustment.NAME:
                    adjustment = new CurveAdjustment();
                    break;

                case ContractBrightnessAdjustment.NAME:
                    adjustment = new ContractBrightnessAdjustment();
                    break;

                default:
                    return null;
            }
            adjustment.importObject(object);
            return adjustment;
        } catch (JSONException e) {
            throw new ImgProcException(e);
        }
    }
}
