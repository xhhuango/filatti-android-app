package com.filatti.activities.adjust.items;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.filatti.R;
import com.filatti.activities.adjust.ui.SliderView;
import com.filatti.effects.EffectException;
import com.filatti.effects.adjusts.HlsAdjust;

import timber.log.Timber;

public class HlsAdjustItem extends AdjustItem<HlsAdjust> {
    private HueSliderActionAdapter mHueSliderActionAdapter;
    private LightnessSliderActionAdapter mLightnessSliderActionAdapter;
    private SaturationSliderActionAdapter mSaturationSliderActionAdapter;

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
        mHueSliderActionAdapter.apply();
        mLightnessSliderActionAdapter.apply();
        mSaturationSliderActionAdapter.apply();
    }

    @Override
    public void cancel() {
        mHueSliderActionAdapter.cancel();
        mLightnessSliderActionAdapter.cancel();
        mSaturationSliderActionAdapter.cancel();
        if (mOnAdjustListener != null) {
            mOnAdjustListener.onAdjustChange();
        }
    }

    @Override
    public void reset() {
        mHueSliderActionAdapter.reset();
        mLightnessSliderActionAdapter.reset();
        mSaturationSliderActionAdapter.reset();
        if (mOnAdjustListener != null) {
            mOnAdjustListener.onAdjustChange();
        }
    }

    @Override
    public View getOverlayView(Context context, ViewGroup rootView) {
        return null;
    }

    @Override
    public View getView(Context context, ViewGroup rootView) {
        LayoutInflater inflater = LayoutInflater.from(context);
        ViewGroup viewGroup =
                (ViewGroup) inflater.inflate(R.layout.hls_item_view, rootView, false);

        ViewGroup hueViewGroup = (ViewGroup) viewGroup.findViewById(R.id.hueTextSlider);
        TextView hueTextView = (TextView) hueViewGroup.findViewById(R.id.sliderTextView);
        SliderView hueSliderView = (SliderView) hueViewGroup.findViewById(R.id.sliderView);
        mHueSliderActionAdapter = new HueSliderActionAdapter(hueTextView, hueSliderView);
        hueSliderView.setOnSliderChangeListener(mHueSliderActionAdapter);
        hueSliderView.setMaxMinValue(-180, 180);
        hueSliderView.setValue(mHueSliderActionAdapter.getFromEffect());

        ViewGroup lightnessViewGroup = (ViewGroup) viewGroup.findViewById(R.id.lightnessTextSlider);
        TextView lightnessTextView = (TextView) lightnessViewGroup.findViewById(R.id.sliderTextView);
        SliderView lightnessSliderView = (SliderView) lightnessViewGroup.findViewById(R.id.sliderView);
        mLightnessSliderActionAdapter =
                new LightnessSliderActionAdapter(lightnessTextView, lightnessSliderView);
        lightnessSliderView.setOnSliderChangeListener(mLightnessSliderActionAdapter);
        lightnessSliderView.setMaxMinValue(-100, 100);
        lightnessSliderView.setValue(mLightnessSliderActionAdapter.getFromEffect());

        ViewGroup saturationViewGroup = (ViewGroup) viewGroup.findViewById(R.id.saturationTextSlider);
        TextView saturationTextView = (TextView) saturationViewGroup.findViewById(R.id.sliderTextView);
        SliderView saturationSliderView = (SliderView) saturationViewGroup.findViewById(R.id.sliderView);
        mSaturationSliderActionAdapter =
                new SaturationSliderActionAdapter(saturationTextView, saturationSliderView);
        saturationSliderView.setOnSliderChangeListener(mSaturationSliderActionAdapter);
        saturationSliderView.setMaxMinValue(-100, 100);
        saturationSliderView.setValue(mSaturationSliderActionAdapter.getFromEffect());

        return viewGroup;
    }

    private class HueSliderActionAdapter extends SliderActionAdapter {
        HueSliderActionAdapter(TextView textView, SliderView sliderView) {
            super(textView, sliderView);
        }

        @Override
        protected void setToEffect(int value) {
            try {
                mEffect.setHue(value);
            } catch (EffectException e) {
                Timber.e(e, "Failed to set hue %d", value);
            }
        }

        @Override
        protected int getFromEffect() {
            return mEffect.getHue();
        }

        @Override
        protected int getInitFromEffect() {
            return mEffect.getInitHue();
        }

        @Override
        protected OnAdjustListener getOnAdjustListener() {
            return mOnAdjustListener;
        }
    }

    private class LightnessSliderActionAdapter extends SliderActionAdapter {
        LightnessSliderActionAdapter(TextView textView, SliderView sliderView) {
            super(textView, sliderView);
        }

        @Override
        protected void setToEffect(int value) {
            double brightness = value / 200.0;
            try {
                mEffect.setLightness(brightness);
            } catch (EffectException e) {
                Timber.e(e, "Failed to set brightness %f", brightness);
            }
        }

        private int convertFromEffect(double value) {
            return (int) (value * 200.0);
        }

        @Override
        protected int getFromEffect() {
            return convertFromEffect(mEffect.getLightness());
        }

        @Override
        protected int getInitFromEffect() {
            return convertFromEffect(mEffect.getInitLightness());
        }

        @Override
        protected OnAdjustListener getOnAdjustListener() {
            return mOnAdjustListener;
        }
    }

    private class SaturationSliderActionAdapter extends SliderActionAdapter {
        SaturationSliderActionAdapter(TextView textView, SliderView sliderView) {
            super(textView, sliderView);
        }

        @Override
        protected void setToEffect(int value) {
            double saturation = value / 100.0;
            try {
                mEffect.setSaturation(saturation);
            } catch (EffectException e) {
                Timber.e(e, "Failed to set saturation %f", saturation);
            }
        }

        private int convertFromEffect(double value) {
            return (int) (value * 100.0);
        }

        @Override
        protected int getFromEffect() {
            return convertFromEffect(mEffect.getSaturation());
        }

        @Override
        protected int getInitFromEffect() {
            return convertFromEffect(mEffect.getInitSaturation());
        }

        @Override
        protected OnAdjustListener getOnAdjustListener() {
            return mOnAdjustListener;
        }
    }
}
