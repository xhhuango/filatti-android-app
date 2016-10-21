package com.filatti.activities.adjust.items;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.filatti.R;
import com.filatti.activities.adjust.ui.SliderView;
import com.filatti.effects.EffectException;
import com.filatti.effects.adjusts.VibranceAdjust;

import timber.log.Timber;

public class VibranceAdjustItem extends AdjustItem<VibranceAdjust> {
    private SliderAdapter mSliderAdapter;

    public VibranceAdjustItem(VibranceAdjust effect) {
        super(effect);
    }

    @Override
    public int getDisplayName() {
        return R.string.vibrance;
    }

    @Override
    public int getIcon() {
        return R.drawable.vibrance;
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
                (ViewGroup) inflater.inflate(R.layout.white_balance_item_view, rootView, false);

        TextView textView = (TextView) viewGroup.findViewById(R.id.sliderTextView);

        SliderView sliderView = (SliderView) viewGroup.findViewById(R.id.sliderView);
        mSliderAdapter = new SliderAdapter(textView, sliderView, -100, 300);

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
        protected void setToEffect(int valueFromSlider) {
            float vibrance = valueFromSlider / 100.0f;
            try {
                mEffect.setVibrance(vibrance);
            } catch (EffectException e) {
                Timber.e(e, "Failed to set vibrance %f", vibrance);
            }
        }

        private int convertFromEffect(float valueFromEffect) {
            return (int) (valueFromEffect * 100.0f);
        }

        @Override
        protected int getFromEffect() {
            return convertFromEffect(mEffect.getVibrance());
        }

        @Override
        protected int getInitFromEffect() {
            return convertFromEffect(mEffect.getInitVibrance());
        }

        @Override
        protected OnAdjustListener getOnAdjustListener() {
            return mOnAdjustListener;
        }
    }
}
