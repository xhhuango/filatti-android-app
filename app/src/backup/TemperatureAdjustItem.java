package com.filatti.activities.adjust.items;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.filatti.R;
import com.filatti.activities.adjust.ui.ValueBarView;
import com.filatti.effects.EffectException;
import com.filatti.effects.adjusts.TemperatureAdjust;

import timber.log.Timber;

public class TemperatureAdjustItem extends AdjustItem<TemperatureAdjust> {
    private ValueBarView mTemperatureValueBarView;
    private ValueBarView mStrengthValueBarView;

    public TemperatureAdjustItem(TemperatureAdjust effect) {
        super(effect);
    }

    @Override
    public int getDisplayName() {
        return R.string.temperature;
    }

    @Override
    public int getIcon() {
        return R.drawable.temperature;
    }

    @Override
    public void apply() {
        mTemperatureValueBarView.apply();
        mStrengthValueBarView.apply();
    }

    @Override
    public void cancel() {
        mTemperatureValueBarView.cancel();
        mStrengthValueBarView.cancel();
        mOnAdjustListener.onAdjustChange();
    }

    @Override
    public void reset() {
        mTemperatureValueBarView.reset();
        mStrengthValueBarView.reset();
        mOnAdjustListener.onAdjustChange();
    }

    @Override
    public View getView(Context context, ViewGroup rootView) {
        LayoutInflater inflater = LayoutInflater.from(context);
        ViewGroup viewGroup =
                (ViewGroup) inflater.inflate(R.layout.temperature_item_view, rootView, false);

        mTemperatureValueBarView =
                (ValueBarView) viewGroup.findViewById(R.id.temperatureValueBar);
        mTemperatureValueBarView.initialize(true,
                                            R.string.temperature_temperature_title,
                                            -100,
                                            100,
                                            getTemperatureFromEffect(),
                                            createTemperatureOnValueChangeListener());

        mStrengthValueBarView = (ValueBarView) viewGroup.findViewById(R.id.strengthValueBar);
        mStrengthValueBarView.initialize(true,
                                         R.string.temperature_strength_title,
                                         0,
                                         100,
                                         getStrengthFromEffect(),
                                         createStrengthOnValueChangeListener());
        return viewGroup;
    }

    private ValueBarView.OnValueChangeListener createTemperatureOnValueChangeListener() {
        return new ValueBarView.OnValueChangeListener() {
            @Override
            public void onValueChanged(int value) {
                setTemperatureToEffect(value);
                mOnAdjustListener.onAdjustChange();
            }
        };
    }

    private void setTemperatureToEffect(int barValue) {
        int kelvin;
        if (barValue == 0) {
            kelvin = 6600;
        } else if (barValue > 0) {
            kelvin = 3000 - barValue * 20;
        } else {
            kelvin = 8000 - barValue * 120;
        }

        try {
            mEffect.setKelvin(kelvin);
        } catch (EffectException e) {
            Timber.e(e, "Failed to set kelvin %d", kelvin);
        }
    }

    private int getTemperatureFromEffect() {
        int kelvin = mEffect.getKelvin();
        if (kelvin == 6600) {
            return 0;
        } else if (kelvin < 6600) {
            return (3000 - kelvin) / 20;
        } else {
            return (8000 - kelvin) / 120;
        }
    }

    private ValueBarView.OnValueChangeListener createStrengthOnValueChangeListener() {
        return new ValueBarView.OnValueChangeListener() {
            @Override
            public void onValueChanged(int value) {
                setStrengthToEffect(value);
                mOnAdjustListener.onAdjustChange();
            }
        };
    }

    private void setStrengthToEffect(int barValue) {
        double strength = barValue / 400.0;
        Timber.d("Set barValue=%d -> strength=%f", barValue, strength);
        try {
            mEffect.setStrength(strength);
        } catch (EffectException e) {
            Timber.e(e, "Failed to set strength %f", strength);
        }
    }

    private int getStrengthFromEffect() {
        return (int) (mEffect.getStrength() * 400.0);
    }
}
