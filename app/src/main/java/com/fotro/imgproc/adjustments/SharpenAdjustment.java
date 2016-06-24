package com.fotro.imgproc.adjustments;

import com.fotro.imgproc.ImgProcException;

import org.json.JSONException;
import org.json.JSONObject;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfInt;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

public class SharpenAdjustment extends Adjustment {
    static final String NAME = "sharpen";

    private static final String SHARPEN = "sharpen";

    private double mSharpen = 0;

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public void importObject(JSONObject object) throws ImgProcException {
        super.importObject(object);

        /**
         * TODO: check range
         * SHARPEN: [0, 100]
         */

        try {
            mSharpen = getParams(object).getInt(SHARPEN) / 100.0;
        } catch (JSONException e) {
            throw new ImgProcException(e);
        }
    }

    @Override
    public void apply(Mat srcRgb, Mat dstRgb) {
        Mat blurredImage = new MatOfInt();
        blurredImage.create(srcRgb.size(), srcRgb.type());
        Imgproc.GaussianBlur(srcRgb, blurredImage, new Size(0, 0), 3);
        Core.addWeighted(srcRgb, 1.0 + mSharpen, blurredImage, -mSharpen, 0, dstRgb);
    }
}