package com.fotro.imgproc;

import org.json.JSONObject;
import org.opencv.core.Mat;

public interface ImgProc {
    String getName();

    void importObject(JSONObject object) throws ImgProcException;

    // TODO
//    JSONObject exportObject() throws ImgProcException;

    void apply(Mat srcRgb, Mat dstRgb);
}
