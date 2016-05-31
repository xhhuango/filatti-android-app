package com.fotro.imgproc.adjustments;

import com.fotro.imgproc.ImgProcException;

import org.json.JSONException;
import org.json.JSONObject;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfInt;

public class ContractBrightnessAdjustment extends Adjustment {
    static final String NAME = "contract_brightness";

    private static final String CONTRACT = "contract";
    private static final String BRIGHTNESS = "brightness";

    private Mat mLut = null;

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public void importObject(JSONObject object) throws ImgProcException {
        super.importObject(object);

        double contract;
        double brightness;
        try {
            contract = (getParams(object).getInt(CONTRACT) / 2.0 + 100.0) / 100.0;
            brightness = getParams(object).getInt(BRIGHTNESS);
        } catch (JSONException e) {
            throw new ImgProcException(e);
        }

        if (contract == 1 && brightness == 0) {
            mLut = null;
        } else {
            mLut = new MatOfInt();
            mLut.create(256, 1, CvType.CV_8UC3);
            for (int i = 0; i < 256; i++) {
                double value = (i - 127.0) * contract + 127.0 + brightness;
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
