package com.filatti.activities.adjust.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class LinearPinchMaskView extends AbstractPinchMaskView {
    private int mRectWidth;
    private int mRectStartX;
    private int mRectEndX;

    private float mAngle = 0;

    private final Paint mLayoutPaint = new Paint();
    private final Paint mGradientUpperPaint = new Paint();
    private final Paint mGradientDownerPaint = new Paint();

    private int mOuterUpperY;
    private int mOuterDownerY;

    private int mInnerUpperY;
    private int mInnerDownerY;

    private final Point mLastPoint = new Point(mCenterPoint);
    private int mLastPointId;
    private float mStartAngle;

    private OnAngleChangeListener mOnAngleChangeListener;

    public LinearPinchMaskView(Context context) {
        super(context);
        init();
    }

    public LinearPinchMaskView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LinearPinchMaskView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mLayoutPaint.setColor(Color.WHITE);
        mGradientUpperPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        mGradientDownerPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
    }

    public float getAngle() {
        return mAngle;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mRectWidth = (int) Math.sqrt(Math.pow(w, 2) + Math.pow(h, 2));
        adjust(mCenter.x, mCenter.y, mOuterRadius, mInnerRadius);
    }

    @Override
    protected void adjustCenter(float x, float y) {
        super.adjustCenter(x, y);
        mRectStartX = mCenterPoint.x - mRectWidth;
        mRectEndX = mCenterPoint.y + mRectWidth;
    }

    @Override
    protected void adjustOuter(float radius) {
        super.adjustOuter(radius);
        int rectOuterHeightHalf = (int) (mWidth / 2 * radius);
        mOuterUpperY = mCenterPoint.y - rectOuterHeightHalf;
        mOuterDownerY = mCenterPoint.y + rectOuterHeightHalf;
    }

    @Override
    protected void adjustInner(float radius) {
        super.adjustInner(radius);
        int rectInnerHeightHalf = (int) (mWidth / 2 * mOuterRadius * radius);
        mInnerUpperY = mCenterPoint.y - rectInnerHeightHalf;
        mInnerDownerY = mCenterPoint.y + rectInnerHeightHalf;
    }

    @Override
    protected void adjustGradient() {
        LinearGradient upperGradient = new LinearGradient(0, mOuterUpperY,
                                                          0, mInnerUpperY,
                                                          0xffffffff, 0x00ffffff,
                                                          Shader.TileMode.CLAMP);
        mGradientUpperPaint.setShader(upperGradient);

        LinearGradient downerGradient = new LinearGradient(0, mInnerDownerY,
                                                           0, mOuterDownerY,
                                                           0x00ffffff, 0xffffffff,
                                                           Shader.TileMode.CLAMP);
        mGradientDownerPaint.setShader(downerGradient);
    }

    @Override
    protected void onDrawMask(Canvas canvas) {
        mCanvas.save();
        mCanvas.rotate(-mAngle, mCenterPoint.x, mCenterPoint.y);
        mCanvas.drawRect(mRectStartX, mOuterUpperY, mRectEndX, mCenterPoint.y, mGradientUpperPaint);
        mCanvas.drawRect(mRectStartX, mCenterPoint.y, mRectEndX, mOuterDownerY, mGradientDownerPaint);
        mCanvas.restore();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!super.onTouchEvent(event)) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    handleDown(event);
                    break;

                case MotionEvent.ACTION_MOVE:
                    handleMove(event);
                    break;

                case MotionEvent.ACTION_UP:
                    if (mOnChangeListener != null) {
                        mOnChangeListener.onStopChange();
                    }
                    mDoesDisplay = false;
                    break;

                default:
            }
        }

        invalidate();
        return true;
    }

    private void handleDown(MotionEvent event) {
        int touchedX = bound((int) event.getX(), 0, mWidth);
        int touchedY = bound((int) event.getY(), 0, mHeight);
        int x = unrotateX(touchedX, touchedY);
        int y = unrotateY(touchedX, touchedY);

        mStartAngle = calculateAngle(x, y);
        mLastPointId = event.getPointerId(0);
        mLastPoint.set((int) event.getX(), (int) event.getY());
        if (mOnChangeListener != null) {
            mOnChangeListener.onStartChange();
        }
    }

    private boolean handleMove(MotionEvent event) {
        int pointIndex = event.findPointerIndex(mLastPointId);
        if (pointIndex < 0) {
            return false;
        }

        if (mCenterMoveEnabled && event.getPointerCount() == 1) {
            int x = (int) event.getX();
            int y = (int) event.getY();
            adjustCenterWithOffset(x - mLastPoint.x, y - mLastPoint.y);
            mLastPoint.set(x, y);
            return true;
        } else if (event.getPointerCount() == 2) {
            int touchedX = bound((int) event.getX(pointIndex), 0, mWidth);
            int touchedY = bound((int) event.getY(pointIndex), 0, mHeight);
            int x = unrotateX(touchedX, touchedY);
            int y = unrotateY(touchedX, touchedY);
            mAngle = formatAngle(mAngle += calculateAngle(x, y) - mStartAngle);
            if (mOnAngleChangeListener != null) {
                mOnAngleChangeListener.onAngleChange(mAngle);
            }
            return true;
        }

        return false;
    }

    private int unrotateX(int x, int y) {
        double radians = Math.toRadians(mAngle);
        return (int) (Math.cos(radians) * (x - mCenterPoint.x)
                - Math.sin(radians) * (y - mCenterPoint.y)
                + mCenterPoint.x);
    }

    private int unrotateY(int x, int y) {
        double radians = Math.toRadians(mAngle);
        return (int) (Math.sin(radians) * (x - mCenterPoint.x)
                + Math.cos(radians) * (y - mCenterPoint.y)
                + mCenterPoint.y);
    }

    private int bound(int value, int min, int max) {
        if (value <= min) {
            return min;
        } else if (value >= max) {
            return max;
        } else {
            return value;
        }
    }

    private float calculateAngle(int x, int y) {
        float angle = (float) Math.toDegrees(Math.atan2(y - mCenterPoint.y, x - mCenterPoint.x));
        if (angle < 0) {
            angle += 360;
        }
        return -angle + 360;
    }

    private float formatAngle(float angle) {
        while (angle < 0) {
            angle += 360;
        }
        while (angle > 360) {
            angle -= 360;
        }
        return angle;
    }

    public void setOnAngleChangeListener(OnAngleChangeListener onAngleChangeListener) {
        mOnAngleChangeListener = onAngleChangeListener;
    }

    public interface OnAngleChangeListener {
        void onAngleChange(float angle);
    }
}
