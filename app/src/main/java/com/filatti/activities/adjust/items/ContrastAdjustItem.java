package com.filatti.activities.adjust.items;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.filatti.R;
import com.filatti.activities.adjust.AdjustAction;
import com.filatti.activities.adjust.ui.SliderView;
import com.filatti.effects.EffectException;
import com.filatti.effects.adjusts.ContrastAdjust;

import timber.log.Timber;

public class ContrastAdjustItem extends AdjustItem<ContrastAdjust> {
    private TextView mTextView;
    private SliderView mSliderView;

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

        mTextView = (TextView) viewGroup.findViewById(R.id.sliderTextView);

        mSliderView = (SliderView) viewGroup.findViewById(R.id.sliderView);
        mSliderAdapter = new SliderAdapter();
        mSliderView.setOnSliderChangeListener(mSliderAdapter);
        mSliderView.setMaxMinValue(-100, 100);
        mSliderView.setValue(mSliderAdapter.getFromEffect());

        return viewGroup;
    }

    private class SliderAdapter implements SliderView.OnSliderChangeListener, AdjustAction {
        private int mInit;
        private int mApplied;
        private int mTemporary;

        private SliderAdapter() {
            mInit = getInitFromEffect();
            mApplied = getFromEffect();
            mTemporary = mApplied;

            mTextView.setText(String.valueOf(mApplied));
        }

        private void setToEffect(int barValue) {
            double contrast = (barValue / 2.0 + 100.0) / 100.0;
            try {
                mEffect.setContrast(contrast);
            } catch (EffectException e) {
                Timber.e(e, "Failed to set contrast %f", contrast);
            }
        }

        private int convertFromEffect(double valueFromEffect) {
            return (int) (((valueFromEffect * 100.0) - 100.0) * 2.0);
        }

        private int getFromEffect() {
            return convertFromEffect(mEffect.getContrast());
        }

        private int getInitFromEffect() {
            return convertFromEffect(mEffect.getInitContrast());
        }

        @Override
        public void apply() {
            mApplied = mTemporary;
        }

        @Override
        public void cancel() {
            setToEffect(mApplied);
            mSliderView.setValue(mApplied);
        }

        @Override
        public void reset() {
            setToEffect(mInit);
            mSliderView.setValue(mInit);
        }

        @Override
        public void onStartTouch() {
            if (mOnAdjustListener != null) {
                mOnAdjustListener.onAdjustStart();
            }
        }

        @Override
        public void onStopTouch() {
            if (mOnAdjustListener != null) {
                mOnAdjustListener.onAdjustStop();
            }
        }

        @Override
        public void onSliderChange(int value, boolean fromUser) {
            setToEffect(value);
            mTextView.setText(String.valueOf(value));
            if (mOnAdjustListener != null) {
                mOnAdjustListener.onAdjustChange();
            }
        }
    }
}
