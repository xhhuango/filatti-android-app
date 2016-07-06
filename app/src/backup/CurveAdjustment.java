package com.fotro.effects.adjusts.core;

import com.fotro.effects.ImageProcessingException;
import com.fotro.effects.adjusts.Adjustment;
import com.fotro.effects.adjusts.IllegalAdjustmentParameter;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.interpolation.LinearInterpolator;
import org.apache.commons.math3.analysis.interpolation.SplineInterpolator;
import org.apache.commons.math3.analysis.interpolation.UnivariateInterpolator;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfInt;

class CurveAdjustment extends Adjustment {
    static final String NAME = "curve";

    private static final String RGB = "rgb";
    private static final String RED = "red";
    private static final String GREEN = "green";
    private static final String BLUE = "blue";

    private static final String IN = "in";
    private static final String OUT = "out";

    private final Mat mLut = new MatOfInt();

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    protected void importParam(String key, Object value) throws IllegalAdjustmentParameter {
        if (!(value instanceof JSONObject))
            throw new IllegalAdjustmentParameter(getName(), key, JSONObject.class, value.getClass());
        JSONObject jsonObject = (JSONObject) value;
        if (!jsonObject.has(IN) || !jsonObject.has(OUT))
            throw new IllegalAdjustmentParameter(getName(),
                                                 key,
                                                 jsonObject.toString(),
                                                 "[" + IN + "|" + OUT + "]");

        switch (key) {
            case RGB:
                break;
            case RED:
                break;
            case GREEN:
                break;
            case BLUE:
                break;
            default:
        }
    }

    private double convertJsonArray2DoubleArray(JSONArray jsonArray) {

    }

    @Override
    public void importJson(JSONObject jsonObject) throws ImageProcessingException {
        super.importJson(jsonObject);

        /**
         * TODO: check range
         * IN: [0, 255]
         * OUT: [0, 255]
         */

        UnivariateFunction valueInterpolator =
                createInterpolator(extractParam(jsonObject, RGB, IN),
                                   extractParam(jsonObject, RGB, OUT));
        UnivariateFunction redInterpolator =
                createInterpolator(extractParam(jsonObject, RED, IN),
                                   extractParam(jsonObject, RED, OUT));
        UnivariateFunction greenInterpolator =
                createInterpolator(extractParam(jsonObject, GREEN, IN),
                                   extractParam(jsonObject, GREEN, OUT));
        UnivariateFunction blueInterpolator =
                createInterpolator(extractParam(jsonObject, BLUE, IN),
                                   extractParam(jsonObject, BLUE, OUT));

        mLut.create(256, 1, CvType.CV_8UC3);
        for (int i = 0; i < 256; i++) {
            double value = valueInterpolator.value(i);
            if (value > 255)
                value = 255;
            double red = redInterpolator.value(value);
            double green = greenInterpolator.value(value);
            double blue = blueInterpolator.value(value);
            mLut.put(i, 0, red, green, blue);
        }
    }

    private double[] extractParam(JSONObject object, String channel, String inOrOut)
            throws ImageProcessingException {
        try {
            JSONArray jsonArray = getParams(object).getJSONObject(channel).getJSONArray(inOrOut);
            double[] array = new double[jsonArray.length()];
            for (int i = 0, j = jsonArray.length(); i < j; i++)
                array[i] = (double) jsonArray.optInt(i);
            return array;
        } catch (JSONException e) {
            throw new ImageProcessingException(e);
        }
    }

    @Override
    public void apply(Mat srcRgb, Mat dstRgb) {
        Core.LUT(srcRgb, mLut, dstRgb);
    }

    private UnivariateFunction createInterpolator(double[] in, double[] out) {
        UnivariateInterpolator interpolator;
        if (in.length > 2) {
            interpolator = new SplineInterpolator();
        } else {
            interpolator = new LinearInterpolator();
        }
        return interpolator.interpolate(in, out);
    }
}
