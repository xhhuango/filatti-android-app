package com.filatti.activities.adjust.items;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.filatti.R;
import com.filatti.activities.adjust.ui.ValueBarView;
import com.filatti.effects.EffectException;
import com.filatti.effects.adjusts.HlsAdjust;

import timber.log.Timber;

public class HlsAdjustItem extends AdjustItem<HlsAdjust> {
    private ValueBarView mHueValueBarView;
    private ValueBarView mLightnessValueBarView;
    private ValueBarView mSaturationValueBarView;

    public HlsAdjustItem(HlsAdjust effect) {
        super(effect);
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
        mOnAdjustListener.onAdjustChange();
    }

    @Override
    public void reset() {
        mHueValueBarView.reset();
        mLightnessValueBarView.reset();
        mSaturationValueBarView.reset();
        mOnAdjustListener.onAdjustChange();
    }

    @Override
    public View getView(Context context, ViewGroup rootView) {
        LayoutInflater inflater = LayoutInflater.from(context);
        ViewGroup viewGroup =
                (ViewGroup) inflater.inflate(R.layout.hls_item_view, rootView, false);

        mHueValueBarView = (ValueBarView) viewGroup.findViewById(R.id.hueValueBar);
        mHueValueBarView.initialize(true,
                                    R.string.hls_hue,
                                    -180,
                                    180,
                                    getHueFromEffect(),
                                    createHueOnValueChangeListener());

        mLightnessValueBarView =
                (ValueBarView) viewGroup.findViewById(R.id.lightnessValueBar);
        mLightnessValueBarView.initialize(true,
                                          R.string.hls_lightness,
                                          -100,
                                          100,
                                          getLightnessFromEffect(),
                                          createLightnessOnValueChangeListener());

        mSaturationValueBarView =
                (ValueBarView) viewGroup.findViewById(R.id.saturationValueBar);
        mSaturationValueBarView.initialize(true,
                                           R.string.hls_saturation,
                                           -100,
                                           100,
                                           getSaturationFromEffect(),
                                           createSaturationOnValueChangeListener());
        return viewGroup;
    }

    private ValueBarView.OnValueChangeListener createHueOnValueChangeListener() {
        return new ValueBarView.OnValueChangeListener() {
            @Override
            public void onValueChanged(int value) {
                setHueToEffect(value);
                mOnAdjustListener.onAdjustChange();
            }
        };
    }

    private void setHueToEffect(int barValue) {
        try {
            mEffect.setHue(barValue);
        } catch (EffectException e) {
            Timber.e(e, "Failed to set hue %d", barValue);
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
                mOnAdjustListener.onAdjustChange();
            }
        };
    }

    private void setLightnessToEffect(int barValue) {
        double brightness = barValue / 200.0;
        try {
            mEffect.setLightness(brightness);
        } catch (EffectException e) {
            Timber.e(e, "Failed to set brightness %f", brightness);
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
                mOnAdjustListener.onAdjustChange();
            }
        };
    }

    private void setRadiusToEffect(int barValue) {
        double saturation = barValue / 100.0;
        Timber.d("Set barValue=%d -> saturation=%f", barValue, saturation);
        try {
            mEffect.setSaturation(saturation);
        } catch (EffectException e) {
            Timber.e(e, "Failed to set saturation %f", saturation);
        }
    }

    private int getSaturationFromEffect() {
        return (int) (mEffect.getSaturation() * 100.0);
    }
}
