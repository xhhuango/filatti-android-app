package com.fotro.effects.adjustments;

import com.fotro.effects.EffectException;

import org.json.JSONObject;

public interface JsonSerializable {
    void fromJson(JSONObject jsonObject) throws EffectException;

    JSONObject toJson() throws EffectException;
}
