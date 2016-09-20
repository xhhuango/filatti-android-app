package com.filatti.activities.adjust.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.PointF;
import android.support.annotation.ColorInt;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import com.filatti.utilities.photo.DisplayUtils;
import com.google.common.base.Preconditions;

public abstract class AbstractPinchMaskView extends View {
    protected static final float OUTER_RADIUS_MAX = 2.0f;
    protected static final float OUTER_RADIUS_MIN = 0;
    protected static final float INNER_RADIUS_MAX = 1.0f;
    protected static final float INNER_RADIUS_MIN = 0;
    protected static final float OUTER_RADIUS_SMALLEST = 0.05f;
    protected static final int SCALE_SPAN_LIMIT = DisplayUtils.dipToPixel(5);

    protected int mWidth;
    protected int mHeight;

    protected final PointF mCenter = new PointF(0.5f, 0.5f);
    protected final Point mCenterPoint = new Point();
    protected float mOuterRadius = 1f;
    protected float mInnerRadius = 0.5f;

    @ColorInt
    protected int mColor = Color.WHITE;
    protected float mStrength = 1;

    protected Bitmap mBitmap;
    protected Canvas mCanvas;

    protected boolean mDoesDisplay = false;
    protected boolean mCenterMoveEnabled = false;
    protected boolean mDidGestureHandle = false;

    protected OnChangeListener mOnChangeListener;
    protected ScaleGestureDetector mScaleGestureDetector;

    public AbstractPinchMaskView(Context context) {
        super(context);
        init();
    }

    public AbstractPinchMaskView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AbstractPinchMaskView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mScaleGestureDetector = new ScaleGestureDetector(getContext(), new ScaleGestureListener());
    }

    public void setColor(int color) {
        mColor = color;
    }

    public PointF getCenter() {
        return mCenter;
    }

    public void setCenter(float x, float y) {
        adjust(x, y, mOuterRadius, mInnerRadius);
    }

    public void display(boolean doesDisplay) {
        mDoesDisplay = doesDisplay;
        invalidate();
    }

    public void setStrength(float strength) {
        Preconditions.checkArgument(strength >= 0 && strength <= 1);
        mStrength = strength;
        setAlpha(strength);
    }

    public void enableCenterMove(boolean enabled) {
        mCenterMoveEnabled = enabled;
    }

    public float getOuterRadius() {
        return mOuterRadius;
    }

    public void setOuterRadius(float outerRadius) {
        adjust(mCenter.x, mCenter.y, outerRadius, mInnerRadius);
    }

    public void setInnerRadius(float innerRadius) {
        adjust(mCenter.x, mCenter.y, mOuterRadius, innerRadius);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mWidth = w;
        mHeight = h;

        adjust(mCenter.x, mCenter.y, mOuterRadius, mInnerRadius);
        setColor(mColor);

        mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
    }

    protected void adjustCenterWithOffset(int dx, int dy) {
        int x = mCenterPoint.x + dx;
        if (x < 0) {
            x = 0;
        } else if (x > mWidth) {
            x = mWidth;
        }

        int y = mCenterPoint.y + dy;
        if (y < 0) {
            y = 0;
        } else if (y > mHeight) {
            y = mHeight;
        }

        if (x != mCenterPoint.x || y != mCenterPoint.y) {
            adjustCenter((float) x / (float) mWidth, (float) y / (float) mHeight);
            adjustOuter(mOuterRadius);
            adjustInner(mInnerRadius);
            adjustGradient();
            if (mOnChangeListener != null) {
                mOnChangeListener.onCenterChange(mCenter.x, mCenter.y);
            }
        }
    }

    protected synchronized void adjust(float x, float y, float outerRadius, float innerRadius) {
        adjustCenter(x, y);
        adjustOuter(outerRadius);
        adjustInner(innerRadius);
        adjustGradient();
        invalidate();
    }

    protected void adjustCenter(float x, float y) {
        Preconditions.checkArgument(x >= 0 && x <= 1);
        Preconditions.checkArgument(y >= 0 && y <= 1);
        mCenter.set(x, y);
        mCenterPoint.set((int) (mWidth * x), (int) (mHeight * y));
    }

    protected void adjustOuter(float radius) {
        Preconditions.checkArgument(radius <= OUTER_RADIUS_MAX && radius >= OUTER_RADIUS_MIN);
        mOuterRadius = radius;
    }

    protected void adjustInner(float radius) {
        Preconditions.checkArgument(radius <= INNER_RADIUS_MAX && radius >= INNER_RADIUS_MIN);
        mInnerRadius = radius;
    }

    protected abstract void adjustGradient();

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mDoesDisplay = true;
        mDidGestureHandle = false;
        mScaleGestureDetector.onTouchEvent(event);
        return mDidGestureHandle;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mDoesDisplay) {
            mCanvas.drawColor(mColor);
            onDrawMask(canvas);
            canvas.drawBitmap(mBitmap, 0, 0, null);

            setStrength(mStrength);
        } else {
            setAlpha(0);
        }
    }

    protected abstract void onDrawMask(Canvas canvas);

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        if (mBitmap != null && !mBitmap.isRecycled()) {
            mBitmap.recycle();
            mBitmap = null;
            mCanvas = null;
        }
    }

    public void setOnChangeListener(OnChangeListener onChangeListener) {
        mOnChangeListener = onChangeListener;
    }

    public interface OnChangeListener {
        void onStartChange();

        void onStopChange();

        void onRadiusChange(float radius);

        void onCenterChange(float x, float y);
    }

    protected class ScaleGestureListener implements ScaleGestureDetector.OnScaleGestureListener {
        private float mLastSpan;

        private boolean scale(ScaleGestureDetector scaleGestureDetector) {
            float radius = mOuterRadius * scaleGestureDetector.getScaleFactor();
            if (radius <= OUTER_RADIUS_MAX && radius >= OUTER_RADIUS_SMALLEST) {
                adjust(mCenter.x, mCenter.y, radius, mInnerRadius);
                return true;
            } else {
                return false;
            }
        }

        private boolean canScale(float span) {
            if (Math.abs(span - mLastSpan) > SCALE_SPAN_LIMIT) {
                mLastSpan = span;
                return true;
            } else {
                return false;
            }
        }

        @Override
        public boolean onScaleBegin(ScaleGestureDetector scaleGestureDetector) {
            mLastSpan = scaleGestureDetector.getCurrentSpan();
            scale(scaleGestureDetector);
            mDidGestureHandle = true;
            return true;
        }

        @Override
        public boolean onScale(ScaleGestureDetector scaleGestureDetector) {
            if (canScale(scaleGestureDetector.getCurrentSpan())) {
                mDidGestureHandle = true;
                if (scale(scaleGestureDetector) && mOnChangeListener != null) {
                    mOnChangeListener.onRadiusChange(mOuterRadius);
                    return true;
                }
            }
            return false;
        }

        @Override
        public void onScaleEnd(ScaleGestureDetector scaleGestureDetector) {
            if (canScale(scaleGestureDetector.getCurrentSpan())) {
                mDidGestureHandle = true;
                scale(scaleGestureDetector);
            }
        }
    }
}
