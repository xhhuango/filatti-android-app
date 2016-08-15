package com.filatti.activities.effect.items.adjusts;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.filatti.R;
import com.filatti.activities.effect.items.EffectItem;
import com.filatti.activities.effect.ui.ValueBarView;
import com.filatti.effects.EffectException;
import com.filatti.effects.adjusts.HlsAdjust;

import timber.log.Timber;

public class HlsAdjustItem extends EffectItem<HlsAdjust> {
    private ViewGroup mViewGroup;
    private ValueBarView mHueValueBarView;
    private ValueBarView mLightnessValueBarView;
    private ValueBarView mSaturationValueBarView;

    public HlsAdjustItem(HlsAdjust effect, OnEffectChangeListener listener) {
        super(effect, listener);
    }

    @Override
    public int getDisplayName() {
        return R.string.hls;
    }

    @Override
    public int getIcon() {
        return R.drawable.hls;
    }

    @Override
    public void apply() {
        mHueValueBarView.apply();
        mLightnessValueBarView.apply();
        mSaturationValueBarView.apply();
    }

    @Override
    public void cancel() {
        mHueValueBarView.cancel();
        mLightnessValueBarView.cancel();
        mSaturationValueBarView.cancel();
        mOnEffectChangeListener.onEffectChanged();
    }

    @Override
    public void reset() {
        mHueValueBarView.reset();
        mLightnessValueBarView.reset();
        mSaturationValueBarView.reset();
        mOnEffectChangeListener.onEffectChanged();
    }

    @Override
    public View getView(Context context, ViewGroup rootView) {
        if (mViewGroup == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            mViewGroup =
                    (ViewGroup) inflater.inflate(R.layout.hls_item_view, rootView, false);

            mHueValueBarView = (ValueBarView) mViewGroup.findViewById(R.id.hueValueBar);
            mHueValueBarView.initialize(true,
                                        R.string.hls_hue,
                                        -180,
                                        180,
                                        getHueFromEffect(),
                                        createHueOnValueChangeListener());

            mLightnessValueBarView =
                    (ValueBarView) mViewGroup.findViewById(R.id.lightnessValueBar);
            mLightnessValueBarView.initialize(true,
                                              R.string.hls_lightness,
                                              -100,
                                              100,
                                              getLightnessFromEffect(),
                                              createLightnessOnValueChangeListener());

            mSaturationValueBarView =
                    (ValueBarView) mViewGroup.findViewById(R.id.saturationValueBar);
            mSaturationValueBarView.initialize(true,
                                               R.string.hls_saturation,
                                               -100,
                                               100,
                                               getSaturationFromEffect(),
                                               createSaturationOnValueChangeListener());
        }
        return mViewGroup;
    }

    private ValueBarView.OnValueChangeListener createHueOnValueChangeListener() {
        return new ValueBarView.OnValueChangeListener() {
            @Override
            public void onValueChanged(int value) {
                setHueToEffect(value);
                mOnEffectChangeListener.onEffectChanged();
            }
        };
    }

    private void setHueToEffect(int barValue) {
        try {
            mEffect.setHue(barValue);
        } catch (EffectException e) {
            Timber.e(e, "Failed to set hue " + barValue);
        }
    }

    private int getHueFromEffect() {
        return mEffect.getHue();
    }

    private ValueBarView.OnValueChangeListener createLightnessOnValueChangeListener() {
        return new ValueBarView.OnValueChangeListener() {
            @Override
            public void onValueChanged(int value) {
                setLightnessToEffect(value);
                mOnEffectChangeListener.onEffectChanged();
            }
        };
    }

    private void setLightnessToEffect(int barValue) {
        double brightness = barValue / 200.0;
        try {
            mEffect.setLightness(brightness);
        } catch (EffectException e) {
            Timber.e(e, "Failed to set brightness " + brightness);
        }
    }

    private int getLightnessFromEffect() {
        return (int) (mEffect.getLightness() * 200.0);
    }

    private ValueBarView.OnValueChangeListener createSaturationOnValueChangeListener() {
        return new ValueBarView.OnValueChangeListener() {
            @Override
            public void onValueChanged(int value) {
                setRadiusToEffect(value);
                mOnEffectChangeListener.onEffectChanged();
            }
        };
    }

    private void setRadiusToEffect(int barValue) {
        double saturation = barValue / 100.0;
        Timber.d("Set barValue=" + barValue + " -> saturation=" + saturation);
        try {
            mEffect.setSaturation(saturation);
        } catch (EffectException e) {
            Timber.e(e, "Failed to set saturation " + saturation);
        }
    }

    private int getSaturationFromEffect() {
        return (int) (mEffect.getSaturation() * 100.0);
    }
}
