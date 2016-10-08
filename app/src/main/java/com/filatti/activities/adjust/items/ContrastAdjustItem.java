package com.filatti.activities.adjust.items;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.filatti.R;
import com.filatti.activities.adjust.ui.SliderView;
import com.filatti.effects.EffectException;
import com.filatti.effects.adjusts.ContrastAdjust;

import timber.log.Timber;

public class ContrastAdjustItem extends AdjustItem<ContrastAdjust> {
    private SliderAdapter mSliderAdapter;

    public ContrastAdjustItem(ContrastAdjust effect) {
        super(effect);
    }

    @Override
    public int getDisplayName() {
        return R.string.contrast;
    }

    @Override
    public int getIcon() {
        return R.drawable.contrast;
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
                (ViewGroup) inflater.inflate(R.layout.contrast_item_view, rootView, false);

        TextView textView = (TextView) viewGroup.findViewById(R.id.sliderTextView);

        SliderView sliderView = (SliderView) viewGroup.findViewById(R.id.sliderView);
        mSliderAdapter = new SliderAdapter(textView, sliderView);
        sliderView.setOnSliderChangeListener(mSliderAdapter);
        sliderView.setMaxMinValue(-100, 100);
        sliderView.setValue(mSliderAdapter.getFromEffect());

        return viewGroup;
    }

    private class SliderAdapter extends SliderActionAdapter {
        SliderAdapter(TextView textView, SliderView sliderView) {
            super(textView, sliderView);
        }

        @Override
        protected void setToEffect(int valueFromSlider) {
            double contrast = (valueFromSlider / 2.0 + 100.0) / 100.0;
            try {
                mEffect.setContrast(contrast);
            } catch (EffectException e) {
                Timber.e(e, "Failed to set contrast %f", contrast);
            }
        }

        private int convertFromEffect(double valueFromEffect) {
            return (int) (((valueFromEffect * 100.0) - 100.0) * 2.0);
        }

        @Override
        protected int getFromEffect() {
            return convertFromEffect(mEffect.getContrast());
        }

        @Override
        protected int getInitFromEffect() {
            return convertFromEffect(mEffect.getInitContrast());
        }

        @Override
        protected OnAdjustListener getOnAdjustListener() {
            return mOnAdjustListener;
        }

    }
}
