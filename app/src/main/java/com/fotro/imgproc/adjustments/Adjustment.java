package com.fotro.imgproc.adjustments;

import com.fotro.imgproc.ImgProc;
import com.fotro.imgproc.ImgProcException;
import com.google.common.base.Preconditions;

import org.json.JSONException;
import org.json.JSONObject;

public abstract class Adjustment implements ImgProc {
    static final String ADJUSTMENT_KEY = "adjustment";
    private static final String PARAMS_KEY = "params";

    @Override
    public void importObject(JSONObject object) throws ImgProcException {
        Preconditions.checkNotNull(object);
        try {
            String name;
            if ((name = object.getString(ADJUSTMENT_KEY)) == null)
                throw new ImgProcException("Field " + ADJUSTMENT_KEY + " must not be null");
            if (!name.equalsIgnoreCase(getName()))
                throw new ImgProcException("This object is not " + ADJUSTMENT_KEY);
            if (object.getJSONObject(PARAMS_KEY) == null)
                throw new ImgProcException("");
        } catch (JSONException e) {
            throw new ImgProcException(e);
        }
    }

    public JSONObject getParams(JSONObject object) throws JSONException {
        return object.getJSONObject(PARAMS_KEY);
    }
}
