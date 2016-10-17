package com.filatti.activities.adjust.items;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.filatti.R;
import com.filatti.activities.adjust.ui.SliderView;
import com.filatti.effects.EffectException;
import com.filatti.effects.adjusts.WhiteBalanceAdjust;

import timber.log.Timber;

public class WhiteBalanceAdjustItem extends AdjustItem<WhiteBalanceAdjust> {
    private SliderAdapter mSliderAdapter;

    public WhiteBalanceAdjustItem(WhiteBalanceAdjust effect) {
        super(effect);
    }

    @Override
    public int getDisplayName() {
        return R.string.white_balance;
    }

    @Override
    public int getIcon() {
        return R.drawable.white_balance;
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
        mSliderAdapter = new SliderAdapter(textView, sliderView, 0, 5);

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
            double percent = valueFromSlider / 100.0;
            try {
                mEffect.setPercent(percent);
            } catch (EffectException e) {
                Timber.e(e, "Failed to set percent %f", percent);
            }
        }

        private int convertFromEffect(double valueFromEffect) {
            return (int) (valueFromEffect * 100.0);
        }

        @Override
        protected int getFromEffect() {
            return convertFromEffect(mEffect.getPercent());
        }

        @Override
        protected int getInitFromEffect() {
            return convertFromEffect(mEffect.getInitPercent());
        }

        @Override
        protected OnAdjustListener getOnAdjustListener() {
            return mOnAdjustListener;
        }

    }
}
