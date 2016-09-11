package com.filatti.activities.adjust.items;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.filatti.R;
import com.filatti.activities.adjust.ui.OnAffectListener;
import com.filatti.activities.adjust.ui.ValueBarView;
import com.filatti.effects.EffectException;
import com.filatti.effects.adjusts.TemperatureAdjust;

import timber.log.Timber;

public class TemperatureAdjustItem extends AdjustItem<TemperatureAdjust> {
    private ValueBarView mTemperatureValueBarView;

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
    }

    @Override
    public void cancel() {
        mTemperatureValueBarView.cancel();
        if (mOnAdjustListener != null) {
            mOnAdjustListener.onAdjustChange();
        }
    }

    @Override
    public void reset() {
        mTemperatureValueBarView.reset();
        if (mOnAdjustListener != null) {
            mOnAdjustListener.onAdjustChange();
        }
    }

    @Override
    public View getView(Context context, ViewGroup rootView) {
        LayoutInflater inflater = LayoutInflater.from(context);
        ViewGroup viewGroup =
                (ViewGroup) inflater.inflate(R.layout.temperature_item_view, rootView, false);

        mTemperatureValueBarView =
                (ValueBarView) viewGroup.findViewById(R.id.temperatureValueBar);
        mTemperatureValueBarView.initialize(true,
                                            0,
                                            -100,
                                            100,
                                            getInitTemperatureFromEffect(),
                                            getTemperatureFromEffect(),
                                            createOnAffectListener(),
                                            createOnValueChangeListener());

        return viewGroup;
    }

    private OnAffectListener createOnAffectListener() {
        return new OnAffectListener() {
            @Override
            public void onStartAffect() {
                if (mOnAdjustListener != null) {
                    mOnAdjustListener.onAdjustStart();
                }
            }

            @Override
            public void onStopAffect() {
                if (mOnAdjustListener != null) {
                    mOnAdjustListener.onAdjustStop();
                }
            }
        };
    }

    private ValueBarView.OnValueChangeListener createOnValueChangeListener() {
        return new ValueBarView.OnValueChangeListener() {
            @Override
            public void onValueChanged(int value) {
                setTemperatureToEffect(value);
                if (mOnAdjustListener != null) {
                    mOnAdjustListener.onAdjustChange();
                }
            }
        };
    }

    private void setTemperatureToEffect(int barValue) {
        double temperature = barValue / 200.0;
        try {
            mEffect.setTemperature(temperature);
        } catch (EffectException e) {
            Timber.e(e, "Failed to set temperature %d", temperature);
        }
    }

    private int convertFromEffect(double valueFromEffect) {
        return (int) (valueFromEffect * 200.0);
    }
    private int getTemperatureFromEffect() {
        return convertFromEffect(mEffect.getTemperature());
    }

    private int getInitTemperatureFromEffect() {
        return convertFromEffect(mEffect.getInitTemperature());
    }
}
