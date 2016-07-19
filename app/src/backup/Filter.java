package com.filatti.effects.filters;

import com.filatti.effects.ImageProcessing;
import com.filatti.effects.ImageProcessingException;
import com.filatti.effects.adjusts.Adjustment;
import com.filatti.effects.adjusts.AdjustmentFactory;
import com.google.common.base.Preconditions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.opencv.core.Mat;
import org.opencv.core.MatOfInt;

import java.util.ArrayList;
import java.util.List;

public class Filter implements ImageProcessing {
    private static final String FILTER = "filter";
    private static final String ACTIONS = "actions";

    private String mName;
    private final List<Adjustment> mAdjustmentList = new ArrayList<>();

    @Override
    public String getName() {
        return mName;
    }

    @Override
    public void importJson(JSONObject jsonObject) throws ImageProcessingException {
        Preconditions.checkNotNull(jsonObject);
        try {
            if ((mName = jsonObject.getString(FILTER)) == null)
                throw new ImageProcessingException("Field " + FILTER + " must not be null");

            JSONArray actions;
            if ((actions = jsonObject.getJSONArray(ACTIONS)) == null)
                throw new ImageProcessingException("Field " + ACTIONS + " must not be null");

            initActions(actions);
        } catch (JSONException e) {
            throw new ImageProcessingException(e);
        }
    }

    @Override
    public JSONObject exportJson() throws ImageProcessingException {
        return null;
    }

    private void initActions(JSONArray actions) throws JSONException, ImageProcessingException {
        AdjustmentFactory factory = new AdjustmentFactory();
        for (int i = 0, j = actions.length(); i < j; i++) {
            JSONObject action = actions.getJSONObject(i);
            mAdjustmentList.add(factory.importAdjustment(action));
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
