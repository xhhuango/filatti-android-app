package com.filatti.effects.adjusts;

import com.filatti.effects.ImageProcessingException;

import org.json.JSONException;
import org.json.JSONObject;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;

import java.util.ArrayList;
import java.util.Collection;

public class VignetteAdjustment extends Adjustment {
    static final String NAME = "vignette";

    private static final String RADIUS = "radius";
    private static final String POWER = "power";

    private double mRadius = 1.0;
    private double mPower = 0.8;

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public void importJson(JSONObject jsonObject) throws ImageProcessingException {
        super.importJson(jsonObject);

        /**
         * TODO: check param
         */

        try {
            JSONObject params = getParams(jsonObject);

            mRadius = params.getDouble(RADIUS);
            mPower = params.getDouble(POWER);
        } catch (JSONException e) {
            throw new ImageProcessingException(e);
        }
    }

    @Override
    public void apply(Mat srcRgb, Mat dstRgb) {
        dstRgb.create(srcRgb.size(), srcRgb.type());
        generateGradient(srcRgb, dstRgb);
    }

    private void generateGradient(Mat src, Mat dst) {
        Point firstPoint = new Point(dst.size().width / 2, dst.size().height / 2);
        double maxImageRad = mRadius * calculateMaxDistanceFromCorners(dst.size(), firstPoint);

        dst.setTo(new Scalar(1));
        for (int row = 0, rowSize = dst.rows(); row < rowSize; row++) {
            for (int col = 0, colSize = dst.cols(); col < colSize; col++) {
                double temp = calculateDistance(firstPoint, new Point(col, row)) / maxImageRad;
                temp *= mPower;
                double ratio = Math.pow(Math.cos(temp), 4);
                double[] pixel = src.get(row, col);
                dst.put(row, col, pixel[0] * ratio, pixel[1] * ratio, pixel[2] * ratio);
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
}
