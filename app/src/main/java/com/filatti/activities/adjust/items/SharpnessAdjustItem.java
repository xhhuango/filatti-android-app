package com.filatti.activities.adjust.items;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.filatti.R;
import com.filatti.activities.adjust.ui.SliderView;
import com.filatti.effects.EffectException;

import com.filatti.effects.adjusts.SharpnessAdjust;

import timber.log.Timber;

public class SharpnessAdjustItem extends AdjustItem<SharpnessAdjust> {
    private SliderAdapter mSliderAdapter;

    public SharpnessAdjustItem(SharpnessAdjust effect) {
        super(effect);
    }

    @Override
    public int getDisplayName() {
        return R.string.sharpen;
    }

    @Override
    public int getIcon() {
        return R.drawable.sharpen;
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
                (ViewGroup) inflater.inflate(R.layout.sharpness_item_view, rootView, false);
        TextView textView = (TextView) viewGroup.findViewById(R.id.sliderTextView);

        SliderView sliderView = (SliderView) viewGroup.findViewById(R.id.sliderView);
        mSliderAdapter = new SliderAdapter(textView, sliderView, 0, 100);

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
            float sharpness = barValue / 20.0f;
            try {
                mEffect.setSharpness(sharpness);
            } catch (EffectException e) {
                Timber.e(e, "Failed to set sharpness %f", sharpness);
            }
        }

        private int convertFromEffect(float value) {
            return (int) (value * 10.0f);
        }

        @Override
        protected int getFromEffect() {
            return convertFromEffect(mEffect.getSharpness());
        }

        @Override
        protected int getInitFromEffect() {
            return convertFromEffect(mEffect.getInitSharpness());
        }

        @Override
        protected OnAdjustListener getOnAdjustListener() {
            return mOnAdjustListener;
        }

        @Override
        public void onStartTouch() {
            if (mOnAdjustListener != null) {
                mEffect.setRebuildBlurred(false);
                mOnAdjustListener.onAdjustStart();
            }
        }

        @Override
        public void onStopTouch() {
            if (mOnAdjustListener != null) {
                mEffect.setRebuildBlurred(true);
                mOnAdjustListener.onAdjustStop();
            }
        }
    }
}
