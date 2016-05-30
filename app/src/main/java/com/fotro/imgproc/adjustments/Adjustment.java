package com.fotro.imgproc.adjustments;

import com.fotro.imgproc.ImageProcessing;
import com.google.common.base.Preconditions;

import org.json.JSONException;
import org.json.JSONObject;

public abstract class Adjustment implements ImageProcessing {
    static final String ADJUSTMENT_KEY = "adjustment";
    private static final String PARAMS_KEY = "params";

    protected JSONObject mAdjustmentObject;

    protected Adjustment(JSONObject adjustmentObject) throws AdjustmentException {
        Preconditions.checkNotNull(adjustmentObject);
        try {
            String name;
            if ((name = adjustmentObject.getString(ADJUSTMENT_KEY)) == null)
                throw new AdjustmentException("Field '" + ADJUSTMENT_KEY + "' must not be null");
            if (!name.equalsIgnoreCase(getName()))
                throw new AdjustmentException("The params does not refer to " + getName() + " adjustment");
            if (adjustmentObject.getJSONObject(PARAMS_KEY) == null)
                throw new AdjustmentException("Field '" + PARAMS_KEY + "' must not be null");

            mAdjustmentObject = adjustmentObject;
        } catch (JSONException e) {
            throw new AdjustmentException(e);
        }
    }

    public JSONObject getParams() {
        return mAdjustmentObject.optJSONObject(PARAMS_KEY);
    }
}
