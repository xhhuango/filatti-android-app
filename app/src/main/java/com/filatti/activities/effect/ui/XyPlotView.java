package com.filatti.activities.effect.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.ColorInt;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.google.common.base.Preconditions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class XyPlotView extends View {
    private static final int DEFAULT_MAX = 255;

    public enum Mode {
        NORMAL,
        ADD_POINT,
        REMOVE_POINT,
    }

    private int mWidth;
    private int mHeight;
    private int mStartX;
    private int mStartY;
    private int mAvailableWidth;
    private int mAvailableHeight;
    private int mMaxX = DEFAULT_MAX;
    private int mMaxY = DEFAULT_MAX;
    private double mIntervalSizeX;
    private double mIntervalSizeY;

    private final Paint mPlotPaint = new Paint();

    private final Paint mCurvesPaint = new Paint();
    private int[] mCurves;
    private Path mCurvesPath;

    private final Paint mPointPaint = new Paint();
    private final List<Point> mPointList = new ArrayList<>();
    private int mPointRadius = 15;
    private int mPointTouchableRadius = 20;

    private Mode mMode = Mode.NORMAL;
    private Point mSelectedPoint;
    private Point mSelectedPrecedingPoint;
    private Point mSelectedSucceedingPoint;

    private OnAddPointListener mOnAddPointListener;
    private onRemovePointListener mOnRemovePointListener;
    private onMovePointListener mOnMovePointListener;

    public XyPlotView(Context context) {
        super(context);
        init();
    }

    public XyPlotView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public XyPlotView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        setPointPaint(Color.BLACK);
        setPlotPaint(3, Color.LTGRAY);
        setCurvesPaint(3, Color.BLACK);
    }

    @Override
    protected void onSizeChanged(int width, int height, int oldWidth, int oldHeight) {
        mWidth = width;
        mHeight = height;
        mStartX = getPaddingStart() + mPointRadius;
        mStartY = getPaddingTop() + mPointRadius;
        mAvailableWidth = mWidth - mStartX - (getPaddingEnd() + mPointRadius);
        mAvailableHeight = mHeight - mStartY - (getPaddingBottom() + mPointRadius);
        mIntervalSizeX = (double) mAvailableWidth / (double) mMaxX;
        mIntervalSizeY = (double) mAvailableHeight / (double) mMaxY;
    }

    private int plotToViewX(int x) {
        if (x > mMaxX)
            x = mMaxX;
        else if (x < 0)
            x = 0;
        return (int) (mStartX + (x * mIntervalSizeX));
    }

    private int plotToViewY(int y) {
        if (y > mMaxY)
            y = mMaxY;
        else if (y < 0)
            y = 0;
        return (int) (mStartY + (mAvailableHeight - (y * mIntervalSizeY)));
    }

    private int viewToPlotX(int x) {
        int viewX = (int) ((x - mStartX) / mIntervalSizeX);
        if (viewX < 0)
            return 0;
        else if (viewX > mMaxX)
            return mMaxX;
        else
            return viewX;
    }

    private int viewToPlotY(int y) {
        int viewY = (int) ((mStartY + mAvailableHeight - y) / mIntervalSizeY);
        if (viewY < 0)
            return 0;
        else if (viewY > mMaxY)
            return mMaxY;
        else
            return viewY;
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawGrid(canvas);

        if (mCurves != null)
            drawCurves(canvas);

        for (Point point : mPointList)
            canvas.drawCircle(point.getViewX(), point.getViewY(), mPointRadius, mPointPaint);
    }

    private void drawLine(Canvas canvas, int fromX, int fromY, int toX, int toY, Paint paint) {
        canvas.drawLine(plotToViewX(fromX),
                        plotToViewY(fromY),
                        plotToViewX(toX),
                        plotToViewY(toY),
                        paint);
    }

    private void drawGrid(Canvas canvas) {
        for (int i = 0, j = 4; i <= j; ++i) {
            int y = (mMaxY + 1) / j * i;
            drawLine(canvas, 0, y, mMaxX, y, mPlotPaint);
        }

        for (int i = 0, j = 4; i <= j; ++i) {
            int x = (mMaxX + 1) / j * i;
            drawLine(canvas, x, 0, x, mMaxY, mPlotPaint);
        }

        drawLine(canvas, 0, 0, mMaxX, mMaxY, mPlotPaint);
    }

    private void drawCurves(Canvas canvas) {
        if (mCurvesPath == null) {
            Path path = new Path();
            path.moveTo(plotToViewX(0), plotToViewY(mCurves[0]));
            for (int i = 1; i <= mMaxX; ++i)
                path.lineTo(plotToViewX(i), plotToViewY(mCurves[i]));
            mCurvesPath = path;
        }
        canvas.drawPath(mCurvesPath, mCurvesPaint);
    }

    @Override
    public boolean onTouchEvent(final MotionEvent event) {
        boolean handled = false;

        int touchX = (int) event.getX(0);
        int touchY = (int) event.getY(0);

        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                handleDownAction(touchX, touchY);
                handled = true;
                break;

            case MotionEvent.ACTION_MOVE:
                if (mSelectedPoint != null) {
                    handleMoveAction(touchX, touchY);
                }
                handled = true;
                break;

            case MotionEvent.ACTION_UP:
                mSelectedPoint = null;
                mSelectedPrecedingPoint = null;
                mSelectedSucceedingPoint = null;
                handled = true;
                break;

            case MotionEvent.ACTION_CANCEL:
                handled = true;
                break;

            default:
                break;
        }

        return super.onTouchEvent(event) || handled;
    }

    private void handleDownAction(int touchX, int touchY) {
        Point touchedPoint = findPoint(touchX, touchY, mPointTouchableRadius);
        if (touchedPoint == null
                && mMode == Mode.ADD_POINT
                && findPointByPlot(viewToPlotX(touchX)) == null) {
            Point point = addPoint(viewToPlotX(touchX), viewToPlotY(touchY));
            mMode = Mode.NORMAL;
            if (mOnAddPointListener != null)
                mOnAddPointListener.onPointAdded(point);

            invalidate();
        } else if (touchedPoint != null && mMode == Mode.REMOVE_POINT) {
            if (touchedPoint.mPlotX == 0 || touchedPoint.mPlotX == mMaxX)
                return;

            mPointList.remove(touchedPoint);
            mMode = Mode.NORMAL;
            if (mOnRemovePointListener != null)
                mOnRemovePointListener.onPointRemoved(touchedPoint);

            invalidate();
        } else {
            mSelectedPoint = touchedPoint;
            int index = mPointList.indexOf(touchedPoint);
            if (index > 0)
                mSelectedPrecedingPoint = mPointList.get(index - 1);
            if (index < mPointList.size() - 1)
                mSelectedSucceedingPoint = mPointList.get(index + 1);
        }
    }

    private void handleMoveAction(int touchX, int touchY) {
        int plotX;
        int plotY = viewToPlotY(touchY);

        if (mSelectedPoint.mPlotX == 0) {
            plotX = 0;
        } else if (mSelectedPoint.mPlotX == mMaxX) {
            plotX = mMaxX;
        } else {
            plotX = viewToPlotX(touchX);

            if (mSelectedPrecedingPoint != null && mSelectedPrecedingPoint.mPlotX >= plotX)
                plotX = mSelectedPrecedingPoint.mPlotX + 1;
            if (mSelectedSucceedingPoint != null && mSelectedSucceedingPoint.mPlotX <= plotX)
                plotX = mSelectedSucceedingPoint.mPlotX - 1;
        }

        if (mSelectedPoint.mPlotX == plotX && mSelectedPoint.mPlotY == plotY)
            return;

        int oldPlotX = mSelectedPoint.mPlotX;
        int oldPlotY = mSelectedPoint.mPlotY;
        mSelectedPoint.setPlot(plotX, plotY);
        if (mOnMovePointListener != null)
            mOnMovePointListener.onPointMoved(mSelectedPoint, oldPlotX, oldPlotY);

        invalidate();
    }

    private Point findPoint(int x, int y, int radius) {
        Point nearestPoint = null;
        long nearestPointDistance = (long) radius * (long) radius + 1;
        for (Point point : mPointList) {
            long diffX = point.getViewX() - x;
            long diffY = point.getViewY() - y;
            long distance = diffX * diffX + diffY * diffY;
            if (distance < nearestPointDistance) {
                nearestPoint = point;
                nearestPointDistance = distance;
            }
        }
        return nearestPoint;
    }

    private Point findPointByPlot(int x) {
        for (Point point : mPointList) {
            if (point.mPlotX == x)
                return point;
        }
        return null;
    }

    public void setPointPaint(@ColorInt int color) {
        mPointPaint.setColor(color);
    }

    public void setPlotPaint(int strokeWidth, @ColorInt int color) {
        Preconditions.checkArgument(strokeWidth > 0);
        mPlotPaint.setStrokeWidth(strokeWidth);
        mPlotPaint.setColor(color);
    }

    public void setCurvesPaint(int strokeWidth, @ColorInt int color) {
        Preconditions.checkArgument(strokeWidth > 0);
        mCurvesPaint.setStrokeWidth(strokeWidth);
        mCurvesPaint.setColor(color);
        mCurvesPaint.setStyle(Paint.Style.STROKE);
    }

    public void setMax(int maxX, int maxY) {
        Preconditions.checkArgument(maxX > 0);
        Preconditions.checkArgument(maxY > 0);
        mMaxX = maxX;
        mMaxY = maxY;
    }

    public void setCurves(int[] curves) {
        Preconditions.checkNotNull(curves);
        Preconditions.checkArgument(curves.length >= mMaxX);
        mCurves = curves;
        mCurvesPath = null;
        invalidate();
    }

    public void setPointRadius(int radius, int touchableRadius) {
        Preconditions.checkArgument(radius > 0);
        Preconditions.checkArgument(touchableRadius > 0);
        mPointRadius = radius;
        mPointTouchableRadius = touchableRadius;
    }

    public List<Point> getPointList() {
        return mPointList;
    }

    public Point addPoint(int x, int y) {
        Preconditions.checkArgument(x >= 0 && x <= mMaxX, "x=" + x);
        Preconditions.checkArgument(y >= 0 && y <= mMaxY, "y=" + y);

        return addPoint(new Point(x, y));
    }

    public Point addPoint(Point point) {
        Preconditions.checkNotNull(point);

        mPointList.add(point);
        Collections.sort(mPointList, new Comparator<Point>() {
            @Override
            public int compare(Point point1, Point point2) {
                return point1.mPlotX - point2.mPlotX;
            }
        });

        return point;
    }

    public void clearPoints() {
        mPointList.clear();
    }

    public void setMode(Mode mode) {
        Preconditions.checkNotNull(mode);
        mMode = mode;
    }

    public Mode getMode() {
        return mMode;
    }

    public void setOnAddPointListener(OnAddPointListener onAddPointListener) {
        mOnAddPointListener = onAddPointListener;
    }

    public void setOnRemovePointListener(onRemovePointListener onRemovePointListener) {
        mOnRemovePointListener = onRemovePointListener;
    }

    public void setOnMovePointListener(onMovePointListener onMovePointListener) {
        mOnMovePointListener = onMovePointListener;
    }

    public interface OnAddPointListener {
        void onPointAdded(Point point);
    }

    public interface onRemovePointListener {
        void onPointRemoved(Point point);
    }

    public interface onMovePointListener {
        void onPointMoved(Point point, int oldX, int oldY);
    }

    public class Point {
        private int mPlotX;
        private int mPlotY;
        private int mViewX = -1;
        private int mViewY = -1;

        private Point(int plotX, int plotY) {
            setPlot(plotX, plotY);
        }

        private void setPlot(int x, int y) {
            Preconditions.checkArgument(x >= 0 && x <= mMaxX, "x=" + x);
            Preconditions.checkArgument(y >= 0 && y <= mMaxY, "y=" + y);

            mPlotX = x;
            mPlotY = y;
            mViewX = -1;
            mViewY = -1;
        }

        public int getPlotX() {
            return mPlotX;
        }

        public int getPlotY() {
            return mPlotY;
        }

        private int getViewX() {
            if (mViewX < 0)
                mViewX = plotToViewX(mPlotX);
            return mViewX;
        }

        private int getViewY() {
            if (mViewY < 0)
                mViewY = plotToViewY(mPlotY);
            return mViewY;
        }

        @Override
        public String toString() {
            return "plotX=" + mPlotX + ", plotY=" + mPlotY
                    + ", viewX=" + mViewX + ", viewY=" + mViewY;
        }
    }
}
