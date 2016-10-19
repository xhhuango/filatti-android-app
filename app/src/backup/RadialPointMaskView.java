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
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.Collection;

public class RadialPointMaskView extends View {
    private int mWidth;
    private int mHeight;
    private float mWidthHalf;
    private float mHeightHalf;

    private int mOuterRadiusX;
    private int mOuterRadiusY;
    private int mInnerRadiusX;
    private int mInnerRadiusY;

    private final Paint mLayoutPaint = new Paint();
    private final RectF mOuterRect = new RectF();
    private final RectF mInnerRect = new RectF();

    private final Paint mPointPaint = new Paint();

    private boolean mIsCircle = false;
    private int mPointRadius = 10;
    private int mTouchableRadius = 30;

    private Collection<Point> mPoints = new ArrayList<>();
    private final Point mPivotPoint = new Point();

    private final Point mOuterTopPoint = new Point();
    private final Point mOuterBottomPoint = new Point();
    private final Point mOuterLeftPoint = new Point();
    private final Point mOuterRightPoint = new Point();

    private final Point mInnerTopPoint = new Point();
    private final Point mInnerBottomPoint = new Point();
    private final Point mInnerLeftPoint = new Point();
    private final Point mInnerRightPoint = new Point();

    private final Paint mGradientPaint = new Paint();
    private final RectF mGradientRect = new RectF();

    private Bitmap mMaskBitmap;
    private Bitmap mBitmap;
    private Canvas mCanvas;

    private Point mSelectedPoint;

    public RadialPointMaskView(Context context) {
        super(context);
        init();
    }

    public RadialPointMaskView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RadialPointMaskView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mLayoutPaint.setColor(Color.WHITE);
        mLayoutPaint.setStyle(Paint.Style.STROKE);

        mPointPaint.setColor(Color.WHITE);

        mGradientPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));

        mPoints.clear();
        mPoints.add(mPivotPoint);
        mPoints.add(mOuterLeftPoint);
        mPoints.add(mOuterTopPoint);
        mPoints.add(mOuterRightPoint);
        mPoints.add(mOuterBottomPoint);
        mPoints.add(mInnerLeftPoint);
        mPoints.add(mInnerTopPoint);
        mPoints.add(mInnerRightPoint);
        mPoints.add(mInnerBottomPoint);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mWidth = w;
        mHeight = h;
        mWidthHalf = w / 2;
        mHeightHalf = h / 2;

        mPivotPoint.set(w / 2, h / 2);

        adjustOuter(w / 3, h / 3);
        adjustInner(mOuterRadiusX / 2, mOuterRadiusY / 2);
        adjustGradient();

        mMaskBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(mMaskBitmap);
        canvas.drawColor(Color.BLUE);

        mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
    }

    private void adjustOuter(int outerRadiusX, int outerRadiusY) {
        if (mIsCircle) {
            outerRadiusX = outerRadiusY = Math.min(outerRadiusX, outerRadiusY);
        }

        mOuterRadiusX = outerRadiusX;
        mOuterRadiusY = outerRadiusY;
        mOuterRect.set(mPivotPoint.x - mOuterRadiusX,
                       mPivotPoint.y - mOuterRadiusY,
                       mPivotPoint.x + mOuterRadiusX,
                       mPivotPoint.y + mOuterRadiusY);

        mOuterLeftPoint.set((int) mOuterRect.left, mPivotPoint.y);
        mOuterRightPoint.set((int) mOuterRect.right, mPivotPoint.y);
        mOuterTopPoint.set(mPivotPoint.x, (int) mOuterRect.top);
        mOuterBottomPoint.set(mPivotPoint.x, (int) mOuterRect.bottom);
    }

    private void adjustInner(int innerRadiusX, int innerRadiusY) {
        if (mIsCircle) {
            innerRadiusX = innerRadiusY = Math.min(innerRadiusX, innerRadiusY);
        }

        mInnerRadiusX = innerRadiusX;
        mInnerRadiusY = innerRadiusY;
        mInnerRect.set(mPivotPoint.x - mInnerRadiusX,
                       mPivotPoint.y - mInnerRadiusY,
                       mPivotPoint.x + mInnerRadiusX,
                       mPivotPoint.y + mInnerRadiusY);

        mInnerLeftPoint.set((int) mInnerRect.left, mPivotPoint.y);
        mInnerRightPoint.set((int) mInnerRect.right, mPivotPoint.y);
        mInnerTopPoint.set(mPivotPoint.x, (int) mInnerRect.top);
        mInnerBottomPoint.set(mPivotPoint.x, (int) mInnerRect.bottom);
    }

    private void adjustGradient() {
        mGradientRect.set(mPivotPoint.x - mOuterRadiusX,
                          mPivotPoint.y - mOuterRadiusY,
                          mPivotPoint.x + mOuterRadiusX,
                          mPivotPoint.y + mOuterRadiusY);

        int[] colors = new int[]{0xff000000, 0xff000000, 0x00000000};
        float[] anchors = new float[]{0, (float) mInnerRadiusX / (float) mOuterRadiusX, 1};
        RadialGradient radialGradient =
                new RadialGradient(0, 0, 1, colors, anchors, Shader.TileMode.CLAMP);

        Matrix matrix = new Matrix();
        matrix.postTranslate(mPivotPoint.x, mPivotPoint.y);
        matrix.postScale(mOuterRadiusX, mOuterRadiusY, mPivotPoint.x, mPivotPoint.y);
        radialGradient.setLocalMatrix(matrix);

        mGradientPaint.setShader(radialGradient);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mCanvas.drawBitmap(mMaskBitmap, 0, 0, null);

        mCanvas.drawOval(mGradientRect, mGradientPaint);

        mCanvas.drawCircle(mPivotPoint.x, mPivotPoint.y, mPointRadius, mPointPaint);

        mCanvas.drawOval(mInnerRect, mLayoutPaint);
        mCanvas.drawCircle(mInnerLeftPoint.x, mInnerLeftPoint.y, mPointRadius, mPointPaint);
        mCanvas.drawCircle(mInnerRightPoint.x, mInnerRightPoint.y, mPointRadius, mPointPaint);
        mCanvas.drawCircle(mInnerTopPoint.x, mInnerTopPoint.y, mPointRadius, mPointPaint);
        mCanvas.drawCircle(mInnerBottomPoint.x, mInnerBottomPoint.y, mPointRadius, mPointPaint);

        mCanvas.drawOval(mOuterRect, mLayoutPaint);
        mCanvas.drawCircle(mOuterLeftPoint.x, mOuterLeftPoint.y, mPointRadius, mPointPaint);
        mCanvas.drawCircle(mOuterRightPoint.x, mOuterRightPoint.y, mPointRadius, mPointPaint);
        mCanvas.drawCircle(mOuterTopPoint.x, mOuterTopPoint.y, mPointRadius, mPointPaint);
        mCanvas.drawCircle(mOuterBottomPoint.x, mOuterBottomPoint.y, mPointRadius, mPointPaint);

        canvas.drawBitmap(mBitmap, 0, 0, null);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean handled = false;

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                handled = handleDown(event);
                break;

            case MotionEvent.ACTION_MOVE:
                handled = handleMove(event);
                invalidate();
                break;

            case MotionEvent.ACTION_UP:
                if (mSelectedPoint != null) {
                    mSelectedPoint = null;
                    handled = true;
                }
                break;

            default:
        }

        return super.onTouchEvent(event) || handled;
    }

    private boolean handleDown(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        int minDistance = mTouchableRadius;
        Point selectedPoint = null;
        for (Point point : mPoints) {
            int distance = distance(point.x, point.y, x, y);
            if (distance < minDistance) {
                selectedPoint = point;
                minDistance = distance;
            }
        }
        if (selectedPoint != null) {
            mSelectedPoint = selectedPoint;
            return true;
        } else {
            return false;
        }
    }

    private int distance(int fromX, int fromY, int toX, int toY) {
        return (int) Math.sqrt(Math.pow(fromX - toX, 2) + Math.pow(fromY - toY, 2));
    }

    private boolean handleMove(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        int radius;

        if (mSelectedPoint == mOuterLeftPoint || mSelectedPoint == mOuterRightPoint) {
            if (mSelectedPoint == mOuterLeftPoint) {
                x = bound(x, 0, mInnerLeftPoint.x);
                radius = mPivotPoint.x - x;
            } else {
                x = bound(x, mInnerRightPoint.x, mWidth);
                radius = x - mPivotPoint.x;
            }
            if (mIsCircle) {
                adjustOuter(radius);
            } else {
                adjustOuter((float) radius / mWidthHalf);
            }
        } else if (mSelectedPoint == mOuterTopPoint || mSelectedPoint == mOuterBottomPoint) {
            if (mSelectedPoint == mOuterTopPoint) {
                y = bound(y, 0, mInnerTopPoint.y);
                radius = mPivotPoint.y - y;
            } else {
                y = bound(y, mInnerBottomPoint.y, mHeight);
                radius = y - mPivotPoint.y;
            }
            if (mIsCircle) {
                adjustOuter(radius);
            } else {
                adjustOuter((float) radius / mHeightHalf);
            }
        } else if (mSelectedPoint == mInnerLeftPoint || mSelectedPoint == mInnerRightPoint) {
            if (mSelectedPoint == mInnerLeftPoint) {
                x = bound(x, mOuterLeftPoint.x, mPivotPoint.x);
                radius = mPivotPoint.x - x;
            } else {
                x = bound(x, mPivotPoint.x, mOuterRightPoint.x);
                radius = x - mPivotPoint.x;
            }
            if (mIsCircle) {
                adjustInner(radius);
            } else {
                adjustInner((float) radius / mWidthHalf);
            }
        } else if (mSelectedPoint == mInnerTopPoint || mSelectedPoint == mInnerBottomPoint) {
            if (mSelectedPoint == mInnerTopPoint) {
                y = bound(y, mOuterTopPoint.y, mPivotPoint.y);
                radius = mPivotPoint.y - y;
            } else {
                y = bound(y, mPivotPoint.y, mOuterBottomPoint.y);
                radius = y - mPivotPoint.y;
            }
            if (mIsCircle) {
                adjustInner(radius);
            } else {
                adjustInner((float) radius / mHeightHalf);
            }
        } else if (mSelectedPoint == mPivotPoint) {
            mPivotPoint.set(x, y);
            adjustOuter(mOuterRadiusX, mOuterRadiusY);
            adjustInner(mInnerRadiusX, mInnerRadiusY);
        } else {
            return false;
        }

        adjustGradient();

        return true;
    }

    private int bound(int value, int min, int max) {
        if (value <= min)
            return min;
        else if (value >= max)
            return max;
        else
            return value;
    }

    private void adjustOuter(float radius) {
        adjustOuter((int) (mWidthHalf * radius), (int) (mHeightHalf * radius));
    }

    private void adjustOuter(int radius) {
        adjustOuter(radius, radius);
    }

    private void adjustInner(float radius) {
        adjustInner((int) (mWidthHalf * radius), (int) (mHeightHalf * radius));
    }

    private void adjustInner(int radius) {
        adjustInner(radius, radius);
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
}
