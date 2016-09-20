package com.filatti.activities.adjust.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RadialGradient;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class RadialPinchMaskView extends AbstractPinchMaskView {
    private int mOuterRadiusX;
    private int mOuterRadiusY;

    private int mInnerRadiusX;
    private int mInnerRadiusY;

    private final RectF mOuterRect = new RectF();
    private final RectF mInnerRect = new RectF();

    private boolean mIsCircle = false;

    private final Point mLastPoint = new Point(mCenterPoint);
    private int mLastPointId;

    private final Paint mGradientPaint = new Paint();
    private final RectF mGradientRect = new RectF();

    public RadialPinchMaskView(Context context) {
        super(context);
        init();
    }

    public RadialPinchMaskView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RadialPinchMaskView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mGradientPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
    }

    public void setCircle(boolean circle) {
        mIsCircle = circle;
    }

    @Override
    protected void adjustOuter(float radius) {
        super.adjustOuter(radius);
        int outerRadiusX = (int) (mWidth / 2 * radius);
        int outerRadiusY = (int) (mHeight / 2 * radius);
        if (mIsCircle) {
            outerRadiusX = outerRadiusY = Math.min(outerRadiusX, outerRadiusY);
        }

        mOuterRadiusX = outerRadiusX;
        mOuterRadiusY = outerRadiusY;
        mOuterRect.set(mCenterPoint.x - outerRadiusX,
                       mCenterPoint.y - outerRadiusY,
                       mCenterPoint.x + outerRadiusX,
                       mCenterPoint.y + outerRadiusY);
    }

    @Override
    protected void adjustInner(float radius) {
        super.adjustInner(radius);
        int innerRadiusX = (int) (mWidth / 2 * mOuterRadius * radius);
        int innerRadiusY = (int) (mHeight / 2 * mOuterRadius * radius);
        if (mIsCircle) {
            innerRadiusX = innerRadiusY = Math.min(innerRadiusX, innerRadiusY);
        }

        mInnerRadiusX = innerRadiusX;
        mInnerRadiusY = innerRadiusY;
        mInnerRect.set(mCenterPoint.x - innerRadiusX,
                       mCenterPoint.y - innerRadiusY,
                       mCenterPoint.x + innerRadiusX,
                       mCenterPoint.y + innerRadiusY);
    }

    @Override
    protected void adjustGradient() {
        mGradientRect.set(mCenterPoint.x - mOuterRadiusX,
                          mCenterPoint.y - mOuterRadiusY,
                          mCenterPoint.x + mOuterRadiusX,
                          mCenterPoint.y + mOuterRadiusY);

        int[] colors = new int[]{0xff000000, 0xff000000, 0x00000000};
        float[] anchors = new float[]{0, (float) mInnerRadiusX / (float) mOuterRadiusX, 1};
        RadialGradient radialGradient =
                new RadialGradient(0, 0, 1, colors, anchors, Shader.TileMode.CLAMP);

        Matrix matrix = new Matrix();
        matrix.postTranslate(mCenterPoint.x, mCenterPoint.y);
        matrix.postScale(mOuterRadiusX, mOuterRadiusY, mCenterPoint.x, mCenterPoint.y);
        radialGradient.setLocalMatrix(matrix);

        mGradientPaint.setShader(radialGradient);
    }

    @Override
    protected void onDrawMask(Canvas canvas) {
        mCanvas.drawOval(mGradientRect, mGradientPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!super.onTouchEvent(event)) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (mCenterMoveEnabled) {
                        mLastPointId = event.getPointerId(0);
                        mLastPoint.set((int) event.getX(), (int) event.getY());
                    }
                    if (mOnChangeListener != null) {
                        mOnChangeListener.onStartChange();
                    }
                    break;

                case MotionEvent.ACTION_MOVE:
                    if (mCenterMoveEnabled) {
                        int pointIndex = event.findPointerIndex(mLastPointId);
                        if (pointIndex >= 0) {
                            int x = (int) event.getX(pointIndex);
                            int y = (int) event.getY(pointIndex);
                            adjustCenterWithOffset(x - mLastPoint.x, y - mLastPoint.y);
                            mLastPoint.set(x, y);
                        }
                    }
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
}
