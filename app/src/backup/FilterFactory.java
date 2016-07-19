package com.filatti.effects.filters;

import com.filatti.effects.ImageProcessingException;

import org.json.JSONObject;

public class FilterFactory {
    public Filter importFilter(JSONObject object) throws ImageProcessingException {
        Filter filter = new Filter();
        filter.importJson(object);
        return filter;
    }
}
