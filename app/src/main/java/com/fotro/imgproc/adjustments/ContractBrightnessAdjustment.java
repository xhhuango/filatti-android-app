package com.fotro.imgproc.adjustments;

import android.util.Log;

import org.json.JSONObject;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfInt;

public class ContractBrightnessAdjustment extends Adjustment {
    static final String NAME = "contract_brightness";

    private static final String CONTRACT = "contract";
    private static final String BRIGHTNESS = "brightness";

    private double mContract = 0;
    private double mBrightness = 0;
    private Mat mLut = null;

    protected ContractBrightnessAdjustment(JSONObject adjustmentObject) throws AdjustmentException {
        super(adjustmentObject);
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public boolean check() {
        // TODO:
        return true;
    }

    @Override
    public void init() {
        mContract = (getParams().optInt(CONTRACT) / 4.0 + 100.0) / 100.0;
        mBrightness = getParams().optInt(BRIGHTNESS);
        Log.d("wayne", "mContract=" + mContract);

        if (mContract == 1 && mBrightness == 0) {
            mLut = null;
        } else {
            mLut = new MatOfInt();
            mLut.create(256, 1, CvType.CV_8UC3);
            for (int i = 0; i < 256; i++) {
                double value = (i - 127.0) * mContract + 127.0 + mBrightness;
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
