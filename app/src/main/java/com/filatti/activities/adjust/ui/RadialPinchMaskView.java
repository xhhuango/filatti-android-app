package com.filatti.activities.adjust.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RadialGradient;
import android.graphics.RectF;
import android.graphics.Shader;
import android.support.annotation.ColorInt;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import com.google.common.base.Preconditions;

public class RadialPinchMaskView extends View {
    private static final float OUTER_RADIUS_MAX = 2.0f;
    private static final float OUTER_RADIUS_MIN = 0;
    private static final float INNER_RADIUS_MAX = 1.0f;
    private static final float INNER_RADIUS_MIN = 0;
    private static final float OUTER_RADIUS_SMALLEST = 0.05f;
    private static final int SCALE_SPAN_LIMIT = 10;
    private static final int POINT_RADIUS = 10;

    private int mWidth;
    private int mHeight;

    private float mOuterRadius = 1f;
    private int mOuterRadiusX;
    private int mOuterRadiusY;

    private float mInnerRadius = 0.5f;
    private int mInnerRadiusX;
    private int mInnerRadiusY;

    private final Paint mLayoutPaint = new Paint();
    private final RectF mOuterRect = new RectF();
    private final RectF mInnerRect = new RectF();

    private final Paint mPointPaint = new Paint();

    private boolean mIsCircle = false;
    private int mPointRadius = POINT_RADIUS;

    private final Point mCenterPoint = new Point();

    private float mStrength = 1;
    private final Paint mGradientPaint = new Paint();
    private final RectF mGradientRect = new RectF();

    @ColorInt
    private int mColor = Color.BLACK;
    private Bitmap mMaskBitmap;
    private Bitmap mBitmap;
    private Canvas mCanvas;

    private boolean mDoesDisplay = false;

    private ScaleGestureDetector mScaleGestureDetector;

    private OnRadiusChangeListener mOnRadiusChangeListener;

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
        mScaleGestureDetector = new ScaleGestureDetector(getContext(), new ScaleGestureListener());

        mLayoutPaint.setColor(Color.WHITE);
        mLayoutPaint.setStyle(Paint.Style.STROKE);

        mPointPaint.setColor(Color.WHITE);

        mGradientPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
    }

    public void display(boolean doesDisplay) {
        mDoesDisplay = doesDisplay;
        invalidate();
    }

    public void setColor(int color) {
        mColor = color;
        if (mMaskBitmap != null) {
            Canvas canvas = new Canvas(mMaskBitmap);
            canvas.drawColor(color);
        }
    }

    public void setCircle(boolean circle) {
        mIsCircle = circle;
    }

    public void setStrength(float strength) {
        Preconditions.checkArgument(strength >= 0 && strength <= 1);
        mStrength = strength;
        setAlpha(strength);
    }

    public void setOuterRadius(float outerRadius) {
        adjust(outerRadius, mInnerRadius);
    }

    public void setInnerRadius(float innerRadius) {
        adjust(mOuterRadius, innerRadius);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mWidth = w;
        mHeight = h;

        mCenterPoint.set(w / 2, h / 2);
        adjust(mOuterRadius, mInnerRadius);

        mMaskBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        setColor(mColor);

        mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
    }

    private void adjust(float outerRadius, float innerRadius) {
        adjustOuter(outerRadius);
        adjustInner(innerRadius);
        adjustGradient();
        invalidate();
    }

    private void adjustOuter(float radius) {
        Preconditions.checkArgument(radius <= OUTER_RADIUS_MAX && radius >= OUTER_RADIUS_MIN);

        mOuterRadius = radius;
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

    private void adjustInner(float radius) {
        Preconditions.checkArgument(radius <= INNER_RADIUS_MAX && radius >= INNER_RADIUS_MIN);

        mInnerRadius = radius;
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

    private void adjustGradient() {
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
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mDoesDisplay) {
            mCanvas.drawBitmap(mMaskBitmap, 0, 0, null);

            mCanvas.drawOval(mOuterRect, mLayoutPaint);
            mCanvas.drawOval(mGradientRect, mGradientPaint);
            mCanvas.drawCircle(mCenterPoint.x, mCenterPoint.y, mPointRadius, mPointPaint);
            mCanvas.drawOval(mInnerRect, mLayoutPaint);
            mCanvas.drawOval(mOuterRect, mLayoutPaint);

            canvas.drawBitmap(mBitmap, 0, 0, null);

            setStrength(mStrength);
        } else {
            setAlpha(0);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mDoesDisplay = true;
        if (event.getPointerCount() == 1) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (mOnRadiusChangeListener != null) {
                        mOnRadiusChangeListener.onStartRadiusChange(mOuterRadius);
                    }
                    break;

                case MotionEvent.ACTION_MOVE:
                    break;

                case MotionEvent.ACTION_UP:
                    if (mOnRadiusChangeListener != null) {
                        mOnRadiusChangeListener.onStopRadiusChange(mOuterRadius);
                    }
                    mDoesDisplay = false;
                    break;

                default:
            }
        } else if (event.getPointerCount() == 2) {
            mScaleGestureDetector.onTouchEvent(event);
        }
        invalidate();
        return true;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        if (mBitmap != null && !mBitmap.isRecycled()) {
            mBitmap.recycle();
            mBitmap = null;
            mCanvas = null;
        }
    }

    public void setOnRadiusChangeListener(OnRadiusChangeListener onRadiusChangeListener) {
        mOnRadiusChangeListener = onRadiusChangeListener;
    }

    public interface OnRadiusChangeListener {
        void onStartRadiusChange(float radius);

        void onRadiusChange(float radius);

        void onStopRadiusChange(float radius);
    }

    private class ScaleGestureListener implements ScaleGestureDetector.OnScaleGestureListener {
        private float mLastSpan;

        private boolean scale(ScaleGestureDetector scaleGestureDetector) {
            float radius = mOuterRadius * scaleGestureDetector.getScaleFactor();
            if (radius <= OUTER_RADIUS_MAX && radius >= OUTER_RADIUS_SMALLEST) {
                adjust(radius, mInnerRadius);
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
            return true;
        }

        @Override
        public boolean onScale(ScaleGestureDetector scaleGestureDetector) {
            if (canScale(scaleGestureDetector.getCurrentSpan())) {
                if (scale(scaleGestureDetector) && mOnRadiusChangeListener != null) {
                    mOnRadiusChangeListener.onRadiusChange(mOuterRadius);
                }
            }
            return true;
        }

        @Override
        public void onScaleEnd(ScaleGestureDetector scaleGestureDetector) {
            if (canScale(scaleGestureDetector.getCurrentSpan())) {
                scale(scaleGestureDetector);
            }
        }
    }
}
