package com.fotro.effects.adjustments;

import com.fotro.effects.ImageProcessingException;

import org.json.JSONException;
import org.json.JSONObject;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfInt;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

public class TemperatureAdjustment extends Adjustment {
    static final String NAME = "temperature";

    private static final String KELVIN = "kelvin";
    private static final String STRENGTH = "strength";

    private int mKelvin;
    private double mStrength;

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public void importJson(JSONObject jsonObject) throws ImageProcessingException {
        super.importJson(jsonObject);

        /**
         * TODO: check range
         * KELVIN: [1000, 40000]
         * STRENGTH: [0, 100]
         */
        try {
            mKelvin = getParams(jsonObject).getInt(KELVIN);
            mStrength = getParams(jsonObject).getInt(STRENGTH) / 200.0;
        } catch (JSONException e) {
            throw new ImageProcessingException(e);
        }
    }

    @Override
    public void apply(Mat srcRgb, Mat dstRgb) {
        double[] kelvinRgb = convertKelvinToRgb(mKelvin);
        kelvinRgb = new double[]{
                kelvinRgb[0] * mStrength,
                kelvinRgb[1] * mStrength,
                kelvinRgb[2] * mStrength};

        Mat l = getLOfHLS(srcRgb);

        dstRgb.create(srcRgb.size(), srcRgb.type());
        for (int row = 0, rows = dstRgb.rows(); row < rows; row++)
            for (int col = 0, cols = dstRgb.cols(); col < cols; col++) {
                double[] rgb = srcRgb.get(row, col);
                rgb = new double[]{
                        rgb[0] * (1 - mStrength) + kelvinRgb[0],
                        rgb[1] * (1 - mStrength) + kelvinRgb[1],
                        rgb[2] * (1 - mStrength) + kelvinRgb[2]};
                dstRgb.put(row, col, rgb);
            }

        Mat hls = new MatOfInt();
        Imgproc.cvtColor(dstRgb, hls, Imgproc.COLOR_RGB2HLS);
        List<Mat> hlsChannels = new ArrayList<>();
        Core.split(hls, hlsChannels);
        hlsChannels.set(1, l);
        Core.merge(hlsChannels, hls);

        Imgproc.cvtColor(hls, dstRgb, Imgproc.COLOR_HLS2RGB);
    }

    private double[] convertKelvinToRgb(int kelvin) {
        double red, green, blue;
        double k = kelvin / 100.0;

        if (k <= 66) {
            red = 255;
        } else {
            red = k - 60;
            red = 329.698727446 * Math.pow(red, -0.1332047592);
        }

        if (k <= 66) {
            green = 99.4708025861 * Math.log(k) - 161.1195681661;
        } else {
            green = k - 60;
            green = 288.1221695283 * Math.pow(green, -0.0755148492);
        }

        if (k >= 66) {
            blue = 255;
        } else if (k <= 19) {
            blue = 0;
        } else {
            blue = k - 10;
            blue = 138.5177312231 * Math.log(blue) - 305.0447927307;
        }

        red = clamp(0, red, 255);
        green = clamp(0, green, 255);
        blue = clamp(0, blue, 255);

        return new double[]{red, green, blue};
    }

    private double clamp(double left, double value, double right) {
        if (value < left)
            return left;
        else if (value > right)
            return right;
        else
            return value;

    }

    private Mat getLOfHLS(Mat rgb) {
        Mat hls = new MatOfInt();
        Imgproc.cvtColor(rgb, hls, Imgproc.COLOR_RGB2HLS);
        List<Mat> hlsChannels = new ArrayList<>();
        Core.split(hls, hlsChannels);
        return hlsChannels.get(1);
    }
}
