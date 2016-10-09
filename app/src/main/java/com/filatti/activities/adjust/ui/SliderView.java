package com.filatti.activities.adjust.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.filatti.utilities.photo.DisplayUtils;

public class SliderView extends View {
    private static final int MIN_HEIGHT = DisplayUtils.dipToPixel(20);
    private static final int MIN_WIDTH = DisplayUtils.dipToPixel(40);
    private static final int MARGIN = DisplayUtils.dipToPixel(15);
    private static final int RADIUS = DisplayUtils.dipToPixel(5);
    private static final int DARK_LINE_WIDTH = DisplayUtils.dipToPixel(1);

    private int mStartX;
    private int mEndX;
    private int mCenterY;

    private int mCurrentValue = 0;
    private int mMaxValue = 100;
    private int mMinValue = -100;

    private final Paint mLightPaint = new Paint();
    private final Paint mDarkPaint = new Paint();
    private final Paint mPointPaint = new Paint();

    private OnSliderChangeListener mOnSliderChangeListener;

    public SliderView(Context context) {
        super(context);
        init();
    }

    public SliderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SliderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mLightPaint.setColor(Color.GRAY);
        mDarkPaint.setColor(Color.BLACK);
        mDarkPaint.setStrokeWidth(DARK_LINE_WIDTH);
        mPointPaint.setColor(Color.BLACK);
    }

    public void setValue(int value) {
        if (value >= mMinValue && value <= mMaxValue) {
            mCurrentValue = value;
            if (mOnSliderChangeListener != null) {
                mOnSliderChangeListener.onSliderChange(mCurrentValue, false);
            }
            invalidate();
        }
    }

    public void setValueRange(int minValue, int maxValue) {
        if (maxValue > minValue) {
            mMinValue = minValue;
            mMaxValue = maxValue;
            invalidate();
        }
    }

    @Override
    protected void onSizeChanged(int width, int height, int oldWidth, int oldHeight) {
        mStartX = MARGIN;
        mEndX = width - MARGIN;
        mCenterY = height / 2;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int width = MIN_WIDTH;
        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else if (widthMode == MeasureSpec.AT_MOST) {
            width = Math.min(MIN_WIDTH, widthSize);
        }

        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int height = MIN_HEIGHT;
        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else if (heightMode == MeasureSpec.AT_MOST) {
            height = Math.min(MIN_HEIGHT, heightSize);
        }

        setMeasuredDimension(width, height);
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawLine(mStartX, mCenterY, mEndX, mCenterY, mLightPaint);
        int zeroX = valueToX(0);
        int pointX = valueToX(mCurrentValue);
        canvas.drawLine(zeroX, mCenterY, pointX, mCenterY, mDarkPaint);
        canvas.drawCircle(pointX, mCenterY, RADIUS, mPointPaint);
    }

    private int valueToX(int value) {
        double totalLength = mMaxValue - mMinValue;
        double xLength = mEndX - mStartX;
        double offset = value - mMinValue;
        int x = (int) (xLength / totalLength * offset);
        return mStartX + x;
    }

    private int xToValue(int x) {
        double totalLength = mMaxValue - mMinValue;
        double xLength = mEndX - mStartX;
        double offset = x - mStartX;
        int value = (int) (offset / (xLength / totalLength));
        return value + mMinValue;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (mOnSliderChangeListener != null) {
                    mOnSliderChangeListener.onStartTouch();
                }
                handleEvent(event);
                break;

            case MotionEvent.ACTION_MOVE:
                handleEvent(event);
                break;

            case MotionEvent.ACTION_UP:
                handleEvent(event);
                if (mOnSliderChangeListener != null) {
                    mOnSliderChangeListener.onStopTouch();
                }
                break;

            default:
        }

        return true;
    }

    private void handleEvent(MotionEvent event) {
        int x = (int) event.getX();
        if (x < mStartX) {
            x = mStartX;
        } else if (x > mEndX) {
            x = mEndX;
        }
        int value = xToValue(x);
        if (value != mCurrentValue) {
            mCurrentValue = value;
            if (mOnSliderChangeListener != null) {
                mOnSliderChangeListener.onSliderChange(mCurrentValue, true);
            }
            invalidate();
        }
    }

    public void setOnSliderChangeListener(OnSliderChangeListener listener) {
        mOnSliderChangeListener = listener;
    }

    public interface OnSliderChangeListener {
        void onStartTouch();

        void onStopTouch();

        void onSliderChange(int value, boolean fromUser);
    }
}
