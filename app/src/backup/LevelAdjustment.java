package com.filatti.effects.adjusts;

import com.filatti.effects.ImageProcessingException;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.interpolation.LinearInterpolator;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfInt;

import java.util.ArrayList;
import java.util.List;

public class LevelAdjustment extends Adjustment {
    static final String NAME = "level";

    private static final String RED = "red";
    private static final String GREEN = "green";
    private static final String BLUE = "blue";

    private static final String IN = "in";
    private static final String OUT = "out";

    private double[] mRedIn;
    private double[] mRedOut;
    private double[] mGreenIn;
    private double[] mGreenOut;
    private double[] mBlueIn;
    private double[] mBlueOut;

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public void importJson(JSONObject jsonObject) throws ImageProcessingException {
        super.importJson(jsonObject);

        mRedIn = extractInParam(jsonObject, RED);
        mRedOut = extractOutParam(jsonObject, RED);

        mGreenIn = extractInParam(jsonObject, GREEN);
        mGreenOut = extractOutParam(jsonObject, GREEN);

        mBlueIn = extractInParam(jsonObject, BLUE);
        mBlueOut = extractOutParam(jsonObject, BLUE);
    }

    private double[] extractInParam(JSONObject object, String channel) throws ImageProcessingException {
        try {
            JSONArray jsonArray = getParams(object).getJSONObject(channel).getJSONArray(IN);
            int darkTone = jsonArray.getInt(0);
            double gamma = jsonArray.getDouble(1);
            int lightTone = jsonArray.getInt(2);

            double middle = (darkTone + lightTone) / 2.0;
            double diff = (lightTone - middle) * (1.0 - gamma);
            int midTone = (int) (middle + diff);

            return new double[]{darkTone, midTone, lightTone};
        } catch (JSONException e) {
            throw new ImageProcessingException(e);
        }
    }

    private double[] extractOutParam(JSONObject object, String channel) throws ImageProcessingException {
        try {
            JSONArray jsonArray = getParams(object).getJSONObject(channel).getJSONArray(OUT);
            int darkTone = jsonArray.getInt(0);
            int lightTone = jsonArray.getInt(1);
            int midTone = (darkTone + lightTone) / 2;

            return new double[]{darkTone, midTone, lightTone};
        } catch (JSONException e) {
            throw new ImageProcessingException(e);
        }
    }

    @Override
    public void apply(Mat srcRgb, Mat dstRgb) {
        processThresholds(srcRgb, dstRgb);
        processMapping(dstRgb, dstRgb);
    }

    private void processThresholds(Mat src, Mat dst) {
        Mat lut = new MatOfInt();
        lut.create(256, 1, CvType.CV_8UC3);

        for (int i = 0; i < 256; i++) {
            double red;
            if (i <= mRedIn[0])
                red = mRedIn[0];
            else if (i >= mRedIn[2])
                red = mRedIn[2];
            else
                red = i;

            double green;
            if (i <= mGreenIn[0])
                green = mGreenIn[0];
            else if (i >= mGreenIn[2])
                green = mGreenIn[2];
            else
                green = i;

            double blue;
            if (i <= mBlueIn[0])
                blue = mBlueIn[0];
            else if (i >= mBlueIn[2])
                blue = mBlueIn[2];
            else
                blue = i;

            lut.put(i, 0, red, green, blue);
        }

        Core.LUT(src, lut, dst);
    }

    private void processMapping(Mat src, Mat dst) {
        UnivariateFunction redInterpolator = createInterpolator(mRedIn, mRedOut);
        UnivariateFunction greenInterpolator = createInterpolator(mGreenIn, mGreenOut);
        UnivariateFunction blueInterpolator = createInterpolator(mBlueIn, mBlueOut);

        Mat lut = new MatOfInt();
        lut.create(256, 1, CvType.CV_8UC3);
        for (int i = 0; i < 256; i++) {
            double red = redInterpolator.value(i);
            double green = greenInterpolator.value(i);
            double blue = blueInterpolator.value(i);
            lut.put(i, 0, red, green, blue);
        }

        Core.LUT(src, lut, dst);
    }

    private UnivariateFunction createInterpolator(double[] in, double[] out) {
        List<Double> inList = new ArrayList<>();
        List<Double> outList = new ArrayList<>();

        if (in[0] != 0) {
            inList.add(0.0);
            outList.add(out[0]);
        }

        inList.add(in[0]);
        inList.add(in[1]);
        inList.add(in[2]);

        outList.add(out[0]);
        outList.add(out[1]);
        outList.add(out[2]);

        if (in[2] != 255) {
            inList.add(255.0);
            outList.add(out[2]);
        }

        double[] inArray = new double[inList.size()];
        double[] outArray = new double[outList.size()];
        for (int i = 0, j = inList.size(); i < j; i++) {
            inArray[i] = inList.get(i);
            outArray[i] = outList.get(i);
        }

        return new LinearInterpolator().interpolate(inArray, outArray);
    }
}
