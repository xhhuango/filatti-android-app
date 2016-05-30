package com.fotro.imgproc.filters;

import com.fotro.imgproc.ImageProcessing;
import com.fotro.imgproc.adjustments.Adjustment;
import com.fotro.imgproc.adjustments.AdjustmentException;
import com.fotro.imgproc.adjustments.AdjustmentFactory;
import com.google.common.base.Preconditions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.opencv.core.Mat;
import org.opencv.core.MatOfInt;

import java.util.ArrayList;
import java.util.List;

public class Filter implements ImageProcessing {
    private static final String NAME_KEY = "name";
    private static final String ACTIONS_KEY = "actions";

    private final String mName;
    private final List<Adjustment> mAdjustmentList = new ArrayList<>();

    public Filter(JSONObject filterObject) throws FilterException {
        Preconditions.checkNotNull(filterObject);
        try {
            if ((mName = filterObject.getString(NAME_KEY)) == null)
                throw new FilterException("Field '" + NAME_KEY + "' must not be null");

            JSONArray actions;
            if ((actions = filterObject.getJSONArray(ACTIONS_KEY)) == null)
                throw new FilterException("Field '" + ACTIONS_KEY + "' must not be null");

            initActions(actions);
        } catch (JSONException | AdjustmentException e) {
            throw new FilterException(e);
        }
    }

    private void initActions(JSONArray actions) throws JSONException, AdjustmentException {
        AdjustmentFactory factory = new AdjustmentFactory();
        for (int i = 0, j = actions.length(); i < j; i++) {
            JSONObject action = actions.getJSONObject(i);
            Adjustment adjustment = factory.create(action);
            mAdjustmentList.add(adjustment);
        }
    }

    @Override
    public String getName() {
        return mName;
    }

    @Override
    public boolean check() {
        for (Adjustment adjustment : mAdjustmentList)
            if (!adjustment.check())
                return false;
        return true;
    }

    @Override
    public void init() {
        for (Adjustment adjustment : mAdjustmentList)
            adjustment.init();
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
