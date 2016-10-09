package com.filatti.activities.adjust.items;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.filatti.R;
import com.filatti.activities.adjust.ui.SliderView;
import com.filatti.effects.EffectException;
import com.filatti.effects.adjusts.TemperatureAdjust;

import timber.log.Timber;

public class TemperatureAdjustItem extends AdjustItem<TemperatureAdjust> {
    private SliderAdapter mSliderAdapter;

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
        mSliderAdapter.apply();
    }

    @Override
    public void cancel() {
        mSliderAdapter.cancel();
        if (mOnAdjustListener != null) {
            mOnAdjustListener.onAdjustChange();
        }
    }

    @Override
    public void reset() {
        mSliderAdapter.reset();
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
                (ViewGroup) inflater.inflate(R.layout.temperature_item_view, rootView, false);

        TextView textView = (TextView) viewGroup.findViewById(R.id.sliderTextView);

        SliderView sliderView = (SliderView) viewGroup.findViewById(R.id.sliderView);
        mSliderAdapter = new SliderAdapter(textView, sliderView, -100, 100);

        return viewGroup;
    }

    private class SliderAdapter extends SliderActionAdapter {
        SliderAdapter(TextView textView,
                      SliderView sliderView,
                      int minSliderView,
                      int maxSliderView) {
            super(textView, sliderView, minSliderView, maxSliderView);
        }

        @Override
        protected void setToEffect(int barValue) {
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

        @Override
        protected int getFromEffect() {
            return convertFromEffect(mEffect.getTemperature());
        }

        @Override
        protected int getInitFromEffect() {
            return convertFromEffect(mEffect.getInitTemperature());
        }

        @Override
        protected OnAdjustListener getOnAdjustListener() {
            return mOnAdjustListener;
        }

    }
}
