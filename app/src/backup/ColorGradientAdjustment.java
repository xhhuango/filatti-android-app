package com.filatti.effects.adjusts;

import com.filatti.effects.ImageProcessingException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfInt;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;

import java.util.ArrayList;
import java.util.Collection;

public class ColorGradientAdjustment extends Adjustment {
    static final String NAME = "color_gradient";

    private static final String FROM_COLOR = "fromColor";
    private static final String TO_COLOR = "toColor";
    private static final String ORIENTATION = "orientation";
    private static final String ALPHA = "alpha";
    private static final String RADIUS = "radius";
    private static final String POWER = "power";

    private double[] mFromColor;
    private double[] mToColor;
    private String mOrientation;
    private double mAlpha;
    private double mRadius;
    private double mPower;

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public void importJson(JSONObject jsonObject) throws ImageProcessingException {
        super.importJson(jsonObject);

        /**
         * TODO: check param
         * FROM_COLOR: ([0, 255], [0, 255], [0, 255])
         * TO_COLOR: ([0, 255], [0, 255], [0, 255])
         * ORIENTATION: [vertical | horizontal]
         * ALPHA: [0.0, 1.0]
         */

        try {
            JSONObject params = getParams(jsonObject);

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

            mOrientation = params.getString(ORIENTATION);
            mAlpha = params.getDouble(ALPHA);
            mRadius = params.getDouble(RADIUS);
            mPower = params.getDouble(POWER);
        } catch (JSONException e) {
            throw new ImageProcessingException(e);
        }
    }

    @Override
    public void apply(Mat srcRgb, Mat dstRgb) {
        Mat colorGradient = new MatOfInt();
        colorGradient.create(srcRgb.size(), srcRgb.type());

        if (mOrientation.equalsIgnoreCase("vertical"))
            generateVerticalLinearGradient(colorGradient);
        else if (mOrientation.equalsIgnoreCase("horizontal"))
            generateHorizontalLinearGradient(colorGradient);
        else if (mOrientation.equalsIgnoreCase("radial"))
            generateRadialGradient(colorGradient);

        Core.addWeighted(srcRgb, 1 - mAlpha, colorGradient, mAlpha, 0.0, dstRgb);
    }

    private void generateHorizontalLinearGradient(Mat gradient) {
        for (int col = 0, colSize = gradient.cols(); col < colSize; col++) {
            double[] value = calculate(col, colSize);
            for (int row = 0, rowSize = gradient.rows(); row < rowSize; row++) {
                gradient.put(row, col, value);
            }
        }
    }

    private void generateVerticalLinearGradient(Mat gradient) {
        for (int row = 0, rowSize = gradient.rows(); row < rowSize; row++) {
            double[] value = calculate(row, rowSize);
            for (int col = 0, colSize = gradient.cols(); col < colSize; col++) {
                gradient.put(row, col, value);
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

    private void generateRadialGradient(Mat gradient) {
        Point firstPoint = new Point(gradient.size().width / 2, gradient.size().height / 2);
        double maxImageRad = mRadius * calculateMaxDistanceFromCorners(gradient.size(), firstPoint);

        gradient.setTo(new Scalar(1));
        for (int row = 0, rowSize = gradient.rows(); row < rowSize; row++) {
            for (int col = 0, colSize = gradient.cols(); col < colSize; col++) {
                double temp = calculateDistance(firstPoint, new Point(col, row)) / maxImageRad;
                temp *= mPower;
                double ratio = Math.pow(Math.cos(temp), 4);
                gradient.put(row, col, calculateColor(mFromColor, mToColor, ratio));
            }
        }
    }

    private double calculateMaxDistanceFromCorners(Size imageSize, Point center) {
        Collection<Point> corners = new ArrayList<>();
        corners.add(new Point(0, 0));
        corners.add(new Point(imageSize.width, 0));
        corners.add(new Point(0, imageSize.height));
        corners.add(new Point(imageSize.width, imageSize.height));

        double maxDistance = 0;

        for (Point corner : corners) {
            double distance = calculateDistance(center, corner);
            maxDistance = (distance > maxDistance) ? distance : maxDistance;
        }

        return maxDistance;
    }

    private double calculateDistance(Point a, Point b) {
        return Math.sqrt(Math.pow(a.x - b.x, 2) + Math.pow((a.y - b.y), 2));
    }

    private double[] calculateColor(double[] fromColor, double[] toColor, double ratio) {
        double beta = 1 - ratio;
        return new double[]{
                fromColor[0] * ratio + toColor[0] * beta,
                fromColor[1] * ratio + toColor[1] * beta,
                fromColor[2] * ratio + toColor[2] * beta
        };
    }
}
