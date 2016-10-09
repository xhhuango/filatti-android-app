package com.filatti.activities.adjust.items;

import android.widget.TextView;

import com.filatti.activities.adjust.AdjustAction;
import com.filatti.activities.adjust.ui.SliderView;
import com.google.common.base.Preconditions;

abstract class SliderActionAdapter implements SliderView.OnSliderChangeListener, AdjustAction {
    private int mInit;
    private int mApplied;
    private int mTemporary;

    TextView mTextView;
    private SliderView mSliderView;

    SliderActionAdapter(TextView textView,
                        SliderView sliderView,
                        int minSliderView,
                        int maxSliderView) {
        Preconditions.checkNotNull(textView);
        Preconditions.checkNotNull(sliderView);

        mTextView = textView;
        mSliderView = sliderView;

        mInit = getInitFromEffect();
        mApplied = getFromEffect();
        mTemporary = mApplied;

        mTextView.setText(String.valueOf(mApplied));

        sliderView.setOnSliderChangeListener(this);
        mSliderView.setValueRange(minSliderView, maxSliderView);
        mSliderView.setValue(mApplied);
    }

    protected abstract void setToEffect(int valueFromSlider);

    protected abstract int getFromEffect();

    protected abstract int getInitFromEffect();

    protected abstract AdjustItem.OnAdjustListener getOnAdjustListener();

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
        AdjustItem.OnAdjustListener onAdjustListener = getOnAdjustListener();
        if (onAdjustListener != null) {
            onAdjustListener.onAdjustStart();
        }
    }

    @Override
    public void onStopTouch() {
        AdjustItem.OnAdjustListener onAdjustListener = getOnAdjustListener();
        if (onAdjustListener != null) {
            onAdjustListener.onAdjustStop();
        }
    }

    @Override
    public void onSliderChange(int value, boolean fromUser) {
        setToEffect(value);
        mTextView.setText(String.valueOf(value));

        AdjustItem.OnAdjustListener onAdjustListener = getOnAdjustListener();
        if (onAdjustListener != null) {
            onAdjustListener.onAdjustChange();
        }
    }
}
