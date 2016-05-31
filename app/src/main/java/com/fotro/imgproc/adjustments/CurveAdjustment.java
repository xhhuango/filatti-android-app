package com.fotro.imgproc.adjustments;

import com.fotro.imgproc.ImgProcException;

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

    private static final String PARAM_VALUE = "value";
    private static final String PARAM_RED = "red";
    private static final String PARAM_GREEN = "green";
    private static final String PARAM_BLUE = "blue";
    private static final String PARAM_IN = "in";
    private static final String PARAM_OUT = "out";

    private final Mat mLut = new MatOfInt();

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public void importObject(JSONObject object) throws ImgProcException {
        super.importObject(object);

        UnivariateFunction valueInterpolator =
                createInterpolator(extractParam(object, PARAM_VALUE, PARAM_IN),
                                   extractParam(object, PARAM_VALUE, PARAM_OUT));
        UnivariateFunction redInterpolator =
                createInterpolator(extractParam(object, PARAM_RED, PARAM_IN),
                                   extractParam(object, PARAM_RED, PARAM_OUT));
        UnivariateFunction greenInterpolator =
                createInterpolator(extractParam(object, PARAM_GREEN, PARAM_IN),
                                   extractParam(object, PARAM_GREEN, PARAM_OUT));
        UnivariateFunction blueInterpolator =
                createInterpolator(extractParam(object, PARAM_BLUE, PARAM_IN),
                                   extractParam(object, PARAM_BLUE, PARAM_OUT));

        mLut.create(256, 1, CvType.CV_8UC3);
        for (int i = 0; i < 256; i++) {
            double value = valueInterpolator.value(i);
            double red = redInterpolator.value(value);
            double green = greenInterpolator.value(value);
            double blue = blueInterpolator.value(value);
            mLut.put(i, 0, red, green, blue);
        }
    }

    private double[] extractParam(JSONObject object, String channel, String inOrOut)
            throws ImgProcException {
        try {
            JSONArray jsonArray = getParams(object).getJSONObject(channel).getJSONArray(inOrOut);
            double[] array = new double[jsonArray.length()];
            for (int i = 0, j = jsonArray.length(); i < j; i++)
                array[i] = (double) jsonArray.optInt(i);
            return array;
        } catch (JSONException e) {
            throw new ImgProcException(e);
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
