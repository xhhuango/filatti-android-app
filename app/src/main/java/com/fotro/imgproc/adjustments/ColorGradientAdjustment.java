package com.fotro.imgproc.adjustments;

import com.fotro.imgproc.ImgProcException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfInt;

public class ColorGradientAdjustment extends Adjustment {
    static final String NAME = "color_gradient";

    private static final String FROM_COLOR = "fromColor";
    private static final String TO_COLOR = "toColor";
    private static final String TYPE = "type";
    private static final String ALPHA = "alpha";

    private double[] mFromColor;
    private double[] mToColor;
    private String mType;
    private double mAlpha;

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public void importObject(JSONObject object) throws ImgProcException {
        super.importObject(object);

        /**
         * TODO: check param
         * FROM_COLOR: ([0, 255], [0, 255], [0, 255])
         * TO_COLOR: ([0, 255], [0, 255], [0, 255])
         * TYPE: [vertical | horizontal]
         * ALPHA: [0.0, 1.0]
         */

        try {
            JSONObject params = getParams(object);

            JSONArray fromColorJsonArray = params.getJSONArray(FROM_COLOR);
            mFromColor = new double[]{
                    fromColorJsonArray.getDouble(0),
                    fromColorJsonArray.getDouble(1),
                    fromColorJsonArray.getDouble(2)};

            JSONArray toColorJsonArray = params.getJSONArray(TO_COLOR);
            mToColor = new double[]{
                    toColorJsonArray.getDouble(0),
                    toColorJsonArray.getDouble(1),
                    toColorJsonArray.getDouble(2)};

            mType = params.getString(TYPE);
            mAlpha = params.getDouble(ALPHA);
        } catch (JSONException e) {
            throw new ImgProcException(e);
        }
    }

    @Override
    public void apply(Mat srcRgb, Mat dstRgb) {
        Mat colorGradient = new MatOfInt();
        colorGradient.create(srcRgb.rows(), srcRgb.cols(), CvType.CV_8UC3);

        if (mType.equalsIgnoreCase("vertical"))
            generateVerticalLinearGradient(colorGradient);
        else
            generateHorizontalLinearGradient(colorGradient);

        Core.addWeighted(srcRgb, 1- mAlpha, colorGradient, mAlpha, 0.0, dstRgb);
    }

    private void generateHorizontalLinearGradient(Mat rgb) {
        for (int col = 0, colSize = rgb.cols(); col < colSize; col++) {
            double[] value = calculate(col, colSize);
            for (int row = 0, rowSize = rgb.rows(); row < rowSize; row++) {
                rgb.put(row, col, value);
            }
        }
    }

    private void generateVerticalLinearGradient(Mat rgb) {
        for (int row = 0, rowSize = rgb.rows(); row < rowSize; row++) {
            double[] value = calculate(row, rowSize);
            for (int col = 0, colSize = rgb.cols(); col < colSize; col++) {
                rgb.put(row, col, value);
            }
        }
    }

    private double[] calculate(int index, int length) {
        return new double[]{
                ((length - index) * mFromColor[0] + index * mToColor[0]) / length,
                ((length - index) * mFromColor[1] + index * mToColor[1]) / length,
                ((length - index) * mFromColor[2] + index * mToColor[2]) / length,
        };
    }
}
