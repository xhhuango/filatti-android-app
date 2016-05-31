package com.fotro.imgproc.adjustments;

import com.fotro.imgproc.ImgProcException;

import org.json.JSONException;
import org.json.JSONObject;
import org.opencv.core.Mat;
import org.opencv.core.MatOfInt;
import org.opencv.imgproc.Imgproc;

public class SaturationAdjustment extends Adjustment {
    static final String NAME = "saturation";

    private static final String SATURATION = "saturation";

    private double mSaturation;

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public void importObject(JSONObject object) throws ImgProcException {
        super.importObject(object);

        /**
         * TODO: check range
         * SATURATION: [-100, 100]
         */

        try {
            mSaturation = getParams(object).getInt(SATURATION) / 100.0;
        } catch (JSONException e) {
            throw new ImgProcException(e);
        }
    }

    @Override
    public void apply(Mat srcRgb, Mat dstRgb) {
        Mat hsv = new MatOfInt();
        Imgproc.cvtColor(srcRgb, hsv, Imgproc.COLOR_RGB2HSV);
        for (int row = 0, maxRow = hsv.rows(); row < maxRow; row++) {
            for (int col = 0, maxCol = hsv.cols(); col < maxCol; col++) {
                double[] pixel = hsv.get(row, col);
                pixel[1] += pixel[1] * mSaturation;
                hsv.put(row, col, pixel);
            }
        }
        Imgproc.cvtColor(hsv, dstRgb, Imgproc.COLOR_HSV2RGB);
    }
}
