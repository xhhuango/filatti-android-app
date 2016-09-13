package com.filatti.activities.adjust.ui;

import android.content.Context;
import android.graphics.PorterDuff;
import android.support.annotation.StringRes;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.filatti.R;
import com.filatti.activities.adjust.AdjustAction;
import com.google.common.base.Preconditions;

public class ValueBarView extends LinearLayout implements AdjustAction {
    private TextView mTitleTextView;
    private TextView mValueTextView;
    private SeekBar mSeekBar;

    private boolean mIsTitleVisible = false;
    @StringRes
    private int mTitleTextRes = 0;

    private OnValueChangeListener mOnValueChangeListener;
    private int mMaxValue = 100;
    private int mMinValue = -100;

    private int mInitValue;
    private int mTemporaryValue;
    private int mAppliedValue;

    public ValueBarView(Context context) {
        super(context);
        initViews(context);
    }

    public ValueBarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews(context);
    }

    public ValueBarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews(context);
    }

    private void initViews(Context context) {
        LayoutInflater inflater =
                (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.value_bar_view, this, true);
        mTitleTextView = (TextView) root.findViewById(R.id.title);
        mValueTextView = (TextView) root.findViewById(R.id.value);

        mSeekBar = (SeekBar) root.findViewById(R.id.seekBar);
        tintProgressBar();
        initProgressBar();

        initialize(mIsTitleVisible,
                   mTitleTextRes,
                   mMinValue,
                   mMaxValue,
                   mInitValue,
                   mAppliedValue,
                   mOnValueChangeListener);
    }

    private void tintProgressBar() {
        mSeekBar.getProgressDrawable().setColorFilter(0xFF000000, PorterDuff.Mode.MULTIPLY);
        mSeekBar.getThumb().setColorFilter(0xFF000000, PorterDuff.Mode.MULTIPLY);
    }

    private void initProgressBar() {
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int value = fromProgressValue(progress);
                mValueTextView.setText(String.valueOf(value));
                if (mTemporaryValue != value) {
                    mTemporaryValue = value;
                    if (mOnValueChangeListener != null) {
                        mOnValueChangeListener.onChange(mTemporaryValue);
                    }
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                if (mOnValueChangeListener != null) {
                    mOnValueChangeListener.onStart(mTemporaryValue);
                }
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (mOnValueChangeListener != null) {
                    mOnValueChangeListener.onStop(mTemporaryValue);
                }
            }
        });
    }

    private int fromProgressValue(int progressValue) {
        return progressValue + mMinValue;
    }

    private int toProgressValue(int value) {
        return value - mMinValue;
    }

    public void initialize(boolean isTitleVisible,
                           @StringRes int titleTextRes,
                           int barMinValue,
                           int barMaxValue,
                           int initValue,
                           int value,
                           OnValueChangeListener onValueChangeListener) {
        Preconditions.checkArgument(barMinValue < barMaxValue);

        mIsTitleVisible = isTitleVisible;
        mTitleTextRes = titleTextRes;
        mMaxValue = barMaxValue;
        mMinValue = barMinValue;
        mInitValue = initValue;
        mTemporaryValue = value;
        mAppliedValue = value;
        mOnValueChangeListener = onValueChangeListener;

        if (mSeekBar != null) {
            mTitleTextView.setVisibility(isTitleVisible ? VISIBLE : GONE);
            if (mTitleTextRes != 0) {
                mTitleTextView.setText(mTitleTextRes);
            } else {
                mTitleTextView.setText("");
            }

            mSeekBar.setMax(barMaxValue - barMinValue);
            setValueToProgress(value);
        }
    }

    private void setValueToProgress(int value) {
        if (mSeekBar != null) {
            mSeekBar.setProgress(toProgressValue(value));
        }
    }

    @Override
    public void apply() {
        mAppliedValue = mTemporaryValue;
    }

    @Override
    public void reset() {
        if (mInitValue != fromProgressValue(mSeekBar.getProgress())) {
            setValueToProgress(mInitValue);
        }
    }

    @Override
    public void cancel() {
        setValueToProgress(mAppliedValue);
    }

    public interface OnValueChangeListener {
        void onStart(int value);

        void onStop(int value);

        void onChange(int value);
    }
}
