package com.filatti.activities.effect.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class LinearMaskView extends View {
    private int mWidth;
    private int mHeight;
    private int mPivotX;
    private int mPivotY;
    private int mRectWidth;
    private int mRectStartX;
    private int mRectEndX;
    private int mRectOuterHeightHalf = 200;
    private int mRectInnerHeightHalf = 100;
    private float mAngle = 30;

    private final Paint mLayoutPaint = new Paint();
    private final Paint mGradientPaint = new Paint();

    private Bitmap mMaskBitmap;
    private Bitmap mBitmap;
    private Canvas mCanvas;

    private int mOuterUpperY;
    private int mInnerUpperY;
    private int mInnerDownerY;
    private int mOuterDownerY;

    private int mPivotRadius = 10;
    private int mTouchableRadius = 30;
    private int mPreservedDistance = 20;

    private Selection mSelection = null;

    private float mStartAngle;

    public LinearMaskView(Context context) {
        super(context);
        init();
    }

    public LinearMaskView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LinearMaskView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mLayoutPaint.setColor(Color.WHITE);

        mGradientPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mWidth = w;
        mHeight = h;
        mPivotX = w / 2;
        mPivotY = h / 2;
        mRectWidth = (int) Math.sqrt(Math.pow(w, 2) + Math.pow(h, 2));
        mRectStartX = mPivotX - mRectWidth;
        mRectEndX = mPivotX + mRectWidth;
        calculateRectangle();

        mMaskBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(mMaskBitmap);
        canvas.drawColor(Color.BLUE);

        mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
    }

    private void calculateRectangle() {
        mOuterUpperY = mPivotY - mRectOuterHeightHalf;
        mInnerUpperY = mPivotY - mRectInnerHeightHalf;
        mInnerDownerY = mPivotY + mRectInnerHeightHalf;
        mOuterDownerY = mPivotY + mRectOuterHeightHalf;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mCanvas.drawBitmap(mMaskBitmap, 0, 0, null);

        mCanvas.save();
        mCanvas.rotate(-mAngle, mPivotX, mPivotY);

        drawMask();
        mCanvas.drawLine(mRectStartX, mOuterUpperY, mRectEndX, mOuterUpperY, mLayoutPaint);
        mCanvas.drawLine(mRectStartX, mInnerUpperY, mRectEndX, mInnerUpperY, mLayoutPaint);
        mCanvas.drawLine(mRectStartX, mPivotY, mRectEndX, mPivotY, mLayoutPaint);
        mCanvas.drawLine(mRectStartX, mInnerDownerY, mRectEndX, mInnerDownerY, mLayoutPaint);
        mCanvas.drawLine(mRectStartX, mOuterDownerY, mRectEndX, mOuterDownerY, mLayoutPaint);

        mCanvas.restore();

        mCanvas.drawCircle(mPivotX, mPivotY, mPivotRadius, mLayoutPaint);

        canvas.drawBitmap(mBitmap, 0, 0, null);
    }

    private void drawMask() {
        LinearGradient upperGradient = new LinearGradient(0, mOuterUpperY,
                                                          0, mInnerUpperY,
                                                          0xffffffff, 0x00ffffff,
                                                          Shader.TileMode.CLAMP);
        mGradientPaint.setShader(upperGradient);
        mCanvas.drawRect(mRectStartX, mOuterUpperY, mRectEndX, mPivotY, mGradientPaint);

        LinearGradient downerGradient = new LinearGradient(0, mInnerDownerY,
                                                           0, mOuterDownerY,
                                                           0x00ffffff, 0xffffffff,
                                                           Shader.TileMode.CLAMP);
        mGradientPaint.setShader(downerGradient);
        mCanvas.drawRect(mRectStartX, mPivotY, mRectEndX, mOuterDownerY, mGradientPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                handleDown(event);
                break;

            case MotionEvent.ACTION_MOVE:
                handleMove(event);
                break;

            case MotionEvent.ACTION_UP:
                mSelection = null;
                break;

            default:
        }

        calculateRectangle();
        invalidate();

        super.onTouchEvent(event);
        return true;
    }

    private int unrotateX(int x, int y) {
        double radians = Math.toRadians(mAngle);
        return (int) (Math.cos(radians) * (x - mPivotX) - Math.sin(radians) * (y - mPivotY) + mPivotX);
    }

    private int unrotateY(int x, int y) {
        double radians = Math.toRadians(mAngle);
        return (int) (Math.sin(radians) * (x - mPivotX) + Math.cos(radians) * (y - mPivotY) + mPivotY);
    }

    private Selection canSelectPivot(int x, int y) {
        return touchable(distance(mPivotX, mPivotY, x, y)) ? Selection.PIVOT : null;
    }

    private Selection canSelectInner(int y) {
        int upperDistance = distance(mInnerUpperY, y);
        int downerDistance = distance(mInnerDownerY, y);
        int distance = Math.min(upperDistance, downerDistance);
        if (touchable(distance)) {
            return (distance == upperDistance) ? Selection.INNER_UPPER : Selection.INNER_DOWNER;
        } else {
            return null;
        }
    }

    private Selection canSelectOuter(int y) {
        int upperDistance = distance(mOuterUpperY, y);
        int downerDistance = distance(mOuterDownerY, y);
        int distance = Math.min(upperDistance, downerDistance);
        if (touchable(distance)) {
            return (distance == upperDistance) ? Selection.OUTER_UPPER : Selection.OUTER_DOWNER;
        } else {
            return null;
        }
    }

    private boolean touchable(int distance) {
        return distance <= mTouchableRadius;
    }

    private int distance(int fromX, int fromY, int toX, int toY) {
        return (int) Math.sqrt(Math.pow(fromX - toX, 2) + Math.pow(fromY - toY, 2));
    }

    private int distance(int from, int to) {
        return Math.abs(from - to);
    }

    private int bound(int value, int min, int max) {
        if (value <= min)
            return min;
        else if (value >= max)
            return max;
        else
            return value;
    }

    private float getAngle(int x, int y) {
        float angle = (float) Math.toDegrees(Math.atan2(y - mPivotY, x - mPivotX));
        if (angle < 0) {
            angle += 360;
        }
        return -angle + 360;
    }

    private void handleDown(MotionEvent event) {
        int touchedX = bound((int) event.getX(), 0, mWidth);
        int touchedY = bound((int) event.getY(), 0, mHeight);
        int x = unrotateX(touchedX, touchedY);
        int y = unrotateY(touchedX, touchedY);

        if ((mSelection = canSelectPivot(x, y)) != null) {
        } else if ((mSelection = canSelectInner(y)) != null) {
        } else if ((mSelection = canSelectOuter(y)) != null) {
        } else {
            mStartAngle = getAngle(x, y);
        }
    }

    private void handleMove(MotionEvent event) {
        if (mSelection == Selection.PIVOT || mSelection == null) {
            int touchedX = bound((int) event.getX(), 0, mWidth);
            int touchedY = bound((int) event.getY(), 0, mHeight);
            int x = unrotateX(touchedX, touchedY);
            int y = unrotateY(touchedX, touchedY);

            if (mSelection == Selection.PIVOT) {
                mPivotX = x;
                mPivotY = y;
            } else {
                mAngle += getAngle(x, y) - mStartAngle;
            }
        } else {
            int y = unrotateY((int) event.getX(), (int) event.getY());
            switch (mSelection) {
                case INNER_UPPER:
                    y = bound(y, mOuterUpperY + mPreservedDistance, mPivotY - mPreservedDistance);
                    mRectInnerHeightHalf = distance(mPivotY, y);
                    break;

                case INNER_DOWNER:
                    y = bound(y, mPivotY + mPreservedDistance, mOuterDownerY - mPreservedDistance);
                    mRectInnerHeightHalf = distance(mPivotY, y);
                    break;

                case OUTER_UPPER:
                    y = bound(y, mPivotY - mRectWidth, mInnerUpperY - mPreservedDistance);
                    mRectOuterHeightHalf = distance(mPivotY, y);
                    break;

                case OUTER_DOWNER:
                    y = bound(y, mInnerDownerY + mPreservedDistance, mPivotY + mRectWidth);
                    mRectOuterHeightHalf = distance(mPivotY, y);
                    break;

                default:
            }
        }
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

    private enum Selection {
        PIVOT,
        INNER_UPPER,
        INNER_DOWNER,
        OUTER_UPPER,
        OUTER_DOWNER,
    }
}
