package com.fotro.imgproc.filters;

import com.fotro.imgproc.ImgProc;
import com.fotro.imgproc.ImgProcException;
import com.fotro.imgproc.adjustments.Adjustment;
import com.fotro.imgproc.adjustments.AdjustmentFactory;
import com.google.common.base.Preconditions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.opencv.core.Mat;
import org.opencv.core.MatOfInt;

import java.util.ArrayList;
import java.util.List;

public class Filter implements ImgProc {
    private static final String FILTER_KEY = "filter";
    private static final String ACTIONS_KEY = "actions";

    private String mName;
    private final List<Adjustment> mAdjustmentList = new ArrayList<>();

    @Override
    public String getName() {
        return mName;
    }

    @Override
    public void importObject(JSONObject object) throws ImgProcException {
        Preconditions.checkNotNull(object);
        try {
            if ((mName = object.getString(FILTER_KEY)) == null)
                throw new ImgProcException("Field " + FILTER_KEY + " must not be null");

            JSONArray actions;
            if ((actions = object.getJSONArray(ACTIONS_KEY)) == null)
                throw new ImgProcException("Field " + ACTIONS_KEY + " must not be null");

            initActions(actions);
        } catch (JSONException e) {
            throw new ImgProcException(e);
        }
    }

    private void initActions(JSONArray actions) throws JSONException, ImgProcException {
        AdjustmentFactory factory = new AdjustmentFactory();
        for (int i = 0, j = actions.length(); i < j; i++) {
            JSONObject action = actions.getJSONObject(i);
            Adjustment adjustment = factory.create(action);
            adjustment.importObject(action);
            mAdjustmentList.add(adjustment);
        }
    }

    @Override
    public void apply(Mat srcRgb, Mat dstRgb) {
        Mat source = new MatOfInt();
        Mat destination = new MatOfInt();

        srcRgb.copyTo(source);

        for (Adjustment adjustment : mAdjustmentList) {
            adjustment.apply(source, destination);
            Mat tmp = source;
            source = destination;
            destination = tmp;
        }

        source.assignTo(dstRgb);
    }
}
