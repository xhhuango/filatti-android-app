package com.fotro.imgproc.filters;

import com.fotro.imgproc.ImgProcException;

import org.json.JSONObject;

public class FilterFactory {
    public Filter importFilter(JSONObject object) throws ImgProcException {
        Filter filter = new Filter();
        filter.importObject(object);
        return filter;
    }
}
