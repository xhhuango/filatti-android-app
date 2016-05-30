package com.fotro.imgproc.adjustments;

import org.json.JSONException;
import org.json.JSONObject;

public class AdjustmentFactory {
    public Adjustment create(JSONObject adjustmentObject) throws JSONException, AdjustmentException {
        switch (adjustmentObject.getString(Adjustment.ADJUSTMENT_KEY)) {
            case CurveAdjustment.NAME:
                return new CurveAdjustment(adjustmentObject);

            case ContractBrightnessAdjustment.NAME:
                return new ContractBrightnessAdjustment(adjustmentObject);

            default:
                return null;
        }
    }
}
