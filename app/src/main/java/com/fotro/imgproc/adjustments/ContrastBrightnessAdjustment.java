package com.fotro.imgproc.adjustments;

import com.fotro.imgproc.ImgProcException;

import org.json.JSONException;
import org.json.JSONObject;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfInt;

public class ContrastBrightnessAdjustment extends Adjustment {
    static final String NAME = "contrast_brightness";

    private static final String CONTRAST = "contrast";
    private static final String BRIGHTNESS = "brightness";

    private Mat mLut = null;

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public void importObject(JSONObject object) throws ImgProcException {
        super.importObject(object);

        /**
         * TODO: check range
         * CONTRAST: [-100, 0, 100]
         * BRIGHTNESS: [-100, 0, 100]
         */

        /**
         * TODO: Maybe to implement contrast automatically adjustment
         * OpenCV's CLAHE (Contrast Limited Adaptive Histogram Equalization)
         * http://stackoverflow.com/questions/24341114/simple-illumination-correction-in-images-opencv-c
         * Then maybe this can replace HistogramEqualizationAdjustment
         */

        double contrast;
        double brightness;
        try {
            contrast = (getParams(object).getInt(CONTRAST) / 2.0 + 100.0) / 100.0;
            brightness = getParams(object).getInt(BRIGHTNESS);
        } catch (JSONException e) {
            throw new ImgProcException(e);
        }

        if (contrast == 1 && brightness == 0) {
            mLut = null;
        } else {
            mLut = new MatOfInt();
            mLut.create(256, 1, CvType.CV_8UC3);
            for (int i = 0; i < 256; i++) {
                double value = (i - 127.0) * contrast + 127.0 + brightness;
                mLut.put(i, 0, value, value, value);
            }
        }
    }

    @Override
    public void apply(Mat srcRgb, Mat dstRgb) {
        if (mLut == null)
            srcRgb.convertTo(dstRgb, -1);
        else
            Core.LUT(srcRgb, mLut, dstRgb);
    }
}
