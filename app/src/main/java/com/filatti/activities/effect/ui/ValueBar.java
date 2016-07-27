package com.filatti.activities.effect.ui;

import android.content.Context;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.filatti.R;
import com.google.common.base.Preconditions;

public class ValueBar extends LinearLayout {
    private LinearLayout mRoot;
    private TextView mValueTextView;
    private SeekBar mSeekBar;

    private OnValueChangeListener mOnValueChangeListener;
    private int mMaxValue = 100;
    private int mMinValue = -100;
    private int mValue = 0;

    public ValueBar(Context context) {
        super(context);
        initViews(context);
    }

    public ValueBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews(context);
    }

    public ValueBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews(context);
    }

    private void initViews(Context context) {
        LayoutInflater inflater =
                (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mRoot = (LinearLayout) inflater.inflate(R.layout.view_value_bar, this, true);
        mValueTextView = (TextView) mRoot.findViewById(R.id.valueBarValue);

        mSeekBar = (SeekBar) mRoot.findViewById(R.id.valueBarBar);
        tintProgressBar();
        initProgressBar();

        initialize(mMinValue, mMaxValue, mValue, mOnValueChangeListener);
    }

    private void tintProgressBar() {
        mSeekBar.getProgressDrawable().setColorFilter(0xFF000000, PorterDuff.Mode.MULTIPLY);
        mSeekBar.getThumb().setColorFilter(0xFF000000, PorterDuff.Mode.MULTIPLY);
    }

    private void initProgressBar() {
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mValue = progress + mMinValue;
                mValueTextView.setText(String.valueOf(mValue));
                if (mOnValueChangeListener != null) {
                    mOnValueChangeListener.onValueChanged(mValue);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    public void initialize(int minValue,
                           int maxValue,
                           int initValue,
                           OnValueChangeListener listener) {
        Preconditions.checkArgument(minValue < maxValue);

        mMaxValue = maxValue;
        mMinValue = minValue;
        mOnValueChangeListener = listener;
        if (mSeekBar != null) {
            mSeekBar.setMax(maxValue - minValue);
            mSeekBar.setProgress(initValue - minValue);
            setValue(initValue);
        }
    }

    public void setValue(int value) {
        Preconditions.checkArgument(value >= mMinValue);
        Preconditions.checkArgument(value <= mMaxValue);

        mValue = value;
        if (mSeekBar != null) {
            mSeekBar.setProgress(value - mMinValue);
        }
    }

    public interface OnValueChangeListener {
        void onValueChanged(int value);
    }
}
