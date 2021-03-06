package com.filatti.activities.adjust.items;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.filatti.R;
import com.filatti.activities.adjust.AdjustAction;
import com.filatti.activities.adjust.ui.XyPlotView;
import com.filatti.effects.EffectException;
import com.filatti.effects.adjusts.CurvesAdjust;
import com.filatti.utilities.photo.DisplayUtils;

import java.util.Arrays;
import java.util.List;

import timber.log.Timber;

public class CurvesAdjustItem extends AdjustItem<CurvesAdjust> {
    private static final int PLOT_PAINT_STROKE_WIDTH = DisplayUtils.dipToPixel(1);
    private static final int CURVES_PAINT_STROKE_WIDTH = DisplayUtils.dipToPixel(2);
    private static final int POINT_RADIUS = DisplayUtils.dipToPixel(5);
    private static final int POINT_TOUCHABLE_RADIUS = DisplayUtils.dipToPixel(12);
    private static final float UNSELECTED_BUTTON_ALPHA = 1;
    private static final float SELECTED_BUTTON_ALPHA = 0.5f;

    private XyPlotView mXyPlotView;

    private ImageButton mValueButton;
    private ImageButton mBlueButton;
    private ImageButton mGreenButton;
    private ImageButton mRedButton;

    private ImageButton mPointButton;
    private ImageButton mRemoveButton;

    private Adapter mValueAdapter;
    private Adapter mRedAdapter;
    private Adapter mGreenAdapter;
    private Adapter mBlueAdapter;

    private ImageButton mSelectedButton;
    private Adapter mSelectedAdapter;

    private View.OnClickListener mButtonOnClickListener;

    private int[] mCurvesBuffer = new int[256];

    public CurvesAdjustItem(CurvesAdjust effect) {
        super(effect);
    }

    @Override
    public int getDisplayName() {
        return R.string.curves;
    }

    @Override
    public int getIcon() {
        return R.drawable.curves;
    }

    @Override
    public void apply() {
        mValueAdapter.apply();
        mRedAdapter.apply();
        mGreenAdapter.apply();
        mBlueAdapter.apply();
    }

    @Override
    public void cancel() {
        mValueAdapter.cancel();
        mRedAdapter.cancel();
        mGreenAdapter.cancel();
        mBlueAdapter.cancel();

        if (mOnAdjustListener != null) {
            mOnAdjustListener.onAdjustChange();
        }
        mSelectedButton.callOnClick();
    }

    @Override
    public void reset() {
        mValueAdapter.reset();
        mRedAdapter.reset();
        mGreenAdapter.reset();
        mBlueAdapter.reset();

        if (mOnAdjustListener != null) {
            mOnAdjustListener.onAdjustChange();
        }
        mSelectedButton.callOnClick();
    }

    @Override
    public View getOverlayView(Context context, ViewGroup rootView) {
        return null;
    }

    @Override
    public View getView(Context context, ViewGroup rootView) {
        LayoutInflater inflater = LayoutInflater.from(context);
        ViewGroup viewGroup =
                (ViewGroup) inflater.inflate(R.layout.curves_item_view, rootView, false);

        initXyPlotView(viewGroup);

        initButtonOnClickListener();
        initValueButton(viewGroup);
        initBlueButton(viewGroup);
        initGreenButton(viewGroup);
        initRedButton(viewGroup);

        initOnAddListener();
        initOnRemoveListener();
        initOnMoveListener();
        initOnAffectListener();

        initPointButton(viewGroup);
        initRemoveButton(viewGroup);

        mValueButton.callOnClick();

        return viewGroup;
    }

    private void initXyPlotView(ViewGroup viewGroup) {
        mXyPlotView = (XyPlotView) viewGroup.findViewById(R.id.xyPlotView);
        mXyPlotView.setPointPaint(Color.BLACK);
        mXyPlotView.setPlotPaint(PLOT_PAINT_STROKE_WIDTH, Color.LTGRAY);
        mXyPlotView.setMax(255, 255);
        mXyPlotView.setPointRadius(POINT_RADIUS, POINT_TOUCHABLE_RADIUS);
    }

    private void initButtonOnClickListener() {
        mButtonOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectRgbButton((ImageButton) view);

                mXyPlotView.setCurvesPaint(CURVES_PAINT_STROKE_WIDTH,
                                           mSelectedAdapter.getCurvesColor());
                mXyPlotView.setCurves(mSelectedAdapter.getCurves());

                mXyPlotView.clearPoints();
                int[] pointArray = mSelectedAdapter.getPoints();
                if (pointArray != null && pointArray.length > 0) {
                    for (int i = 0, j = pointArray.length; i < j; i += 2) {
                        mXyPlotView.addPoint(pointArray[i], pointArray[i + 1]);
                    }
                }
            }
        };
    }

    private void initValueButton(ViewGroup viewGroup) {
        mValueButton = (ImageButton) viewGroup.findViewById(R.id.valueButton);
        mValueAdapter = new Adapter() {
            @Override
            protected int[] getCurves() {
                mEffect.getValueCurves(mCurvesBuffer);
                return mCurvesBuffer;
            }

            @Override
            protected int getCurvesColor() {
                return Color.BLACK;
            }

            @Override
            protected int[] getInitPoints() {
                return mEffect.getInitValuePoints();
            }

            @Override
            protected int[] getPoints() {
                return mEffect.getValuePoints();
            }

            @Override
            protected void setPoints(int[] points) {
                super.setPoints(points);
                try {
                    mEffect.setValuePoints(points);
                } catch (EffectException e) {
                    Timber.e(e, "Failed to set value points: %s", Arrays.toString(points));
                }
            }
        };

        mValueButton.setOnClickListener(mButtonOnClickListener);
    }

    private void initBlueButton(ViewGroup viewGroup) {
        mBlueButton = (ImageButton) viewGroup.findViewById(R.id.blueButton);
        mBlueAdapter = new Adapter() {
            @Override
            protected int[] getCurves() {
                mEffect.getBlueCurves(mCurvesBuffer);
                return mCurvesBuffer;
            }

            @Override
            protected int getCurvesColor() {
                return Color.BLUE;
            }

            @Override
            protected int[] getInitPoints() {
                return mEffect.getInitBluePoints();
            }

            @Override
            protected int[] getPoints() {
                return mEffect.getBluePoints();
            }

            @Override
            protected void setPoints(int[] points) {
                super.setPoints(points);
                try {
                    mEffect.setBluePoints(points);
                } catch (EffectException e) {
                    Timber.e(e, "Failed to set blue points: %s", Arrays.toString(points));
                }
            }
        };

        mBlueButton.setOnClickListener(mButtonOnClickListener);
    }

    private void initGreenButton(ViewGroup viewGroup) {
        mGreenButton = (ImageButton) viewGroup.findViewById(R.id.greenButton);
        mGreenAdapter = new Adapter() {
            @Override
            protected int[] getCurves() {
                mEffect.getGreenCurves(mCurvesBuffer);
                return mCurvesBuffer;
            }

            @Override
            protected int getCurvesColor() {
                return Color.GREEN;
            }

            @Override
            protected int[] getInitPoints() {
                return mEffect.getInitGreenPoints();
            }

            @Override
            protected int[] getPoints() {
                return mEffect.getGreenPoints();
            }

            @Override
            protected void setPoints(int[] points) {
                super.setPoints(points);
                try {
                    mEffect.setGreenPoints(points);
                } catch (EffectException e) {
                    Timber.e(e, "Failed to set green points: %s", Arrays.toString(points));
                }
            }
        };

        mGreenButton.setOnClickListener(mButtonOnClickListener);
    }

    private void initRedButton(ViewGroup viewGroup) {
        mRedButton = (ImageButton) viewGroup.findViewById(R.id.redButton);
        mRedAdapter = new Adapter() {
            @Override
            protected int[] getCurves() {
                mEffect.getRedCurves(mCurvesBuffer);
                return mCurvesBuffer;
            }

            @Override
            protected int getCurvesColor() {
                return Color.RED;
            }

            @Override
            protected int[] getInitPoints() {
                return mEffect.getInitRedPoints();
            }

            @Override
            protected int[] getPoints() {
                return mEffect.getRedPoints();
            }

            @Override
            protected void setPoints(int[] points) {
                super.setPoints(points);
                try {
                    mEffect.setRedPoints(points);
                } catch (EffectException e) {
                    Timber.e(e, "Failed to set red points: %s", Arrays.toString(points));
                }
            }
        };

        mRedButton.setOnClickListener(mButtonOnClickListener);
    }

    private int[] pointListToArray(List<XyPlotView.Point> pointList) {
        int[] pointArray = new int[pointList.size() * 2];
        for (int i = 0, j = pointList.size(); i < j; ++i) {
            XyPlotView.Point p = pointList.get(i);
            int index = i * 2;
            pointArray[index] = p.getPlotX();
            pointArray[index + 1] = p.getPlotY();
        }
        return pointArray;
    }

    private void initOnAddListener() {
        mXyPlotView.setOnAddPointListener(new XyPlotView.OnAddPointListener() {
            @Override
            public void onPointAdded(XyPlotView.Point point) {
                mSelectedAdapter.setPoints(pointListToArray(mXyPlotView.getPointList()));
                mXyPlotView.setCurves(mSelectedAdapter.getCurves());
                setButtonUnselected(mPointButton);
                if (mOnAdjustListener != null) {
                    mOnAdjustListener.onAdjustStart();
                    mOnAdjustListener.onAdjustChange();
                    mOnAdjustListener.onAdjustStop();
                }
            }
        });
    }

    private void initOnRemoveListener() {
        mXyPlotView.setOnRemovePointListener(new XyPlotView.OnRemovePointListener() {
            @Override
            public void onPointRemoved(XyPlotView.Point point) {
                mSelectedAdapter.setPoints(pointListToArray(mXyPlotView.getPointList()));
                mXyPlotView.setCurves(mSelectedAdapter.getCurves());
                setButtonUnselected(mRemoveButton);
                if (mOnAdjustListener != null) {
                    mOnAdjustListener.onAdjustStart();
                    mOnAdjustListener.onAdjustChange();
                    mOnAdjustListener.onAdjustStop();
                }
            }
        });
    }

    private void initOnMoveListener() {
        mXyPlotView.setOnMovePointListener(new XyPlotView.OnMovePointListener() {
            @Override
            public void onPointMoved(XyPlotView.Point point, int oldX, int oldY) {
                mSelectedAdapter.setPoints(pointListToArray(mXyPlotView.getPointList()));
                mXyPlotView.setCurves(mSelectedAdapter.getCurves());
                if (mOnAdjustListener != null) {
                    mOnAdjustListener.onAdjustChange();
                }
            }
        });
    }

    private void initOnAffectListener() {
        mXyPlotView.setOnChangeListener(new XyPlotView.OnChangeListener() {
            @Override
            public void onStart() {
                if (mOnAdjustListener != null) {
                    mOnAdjustListener.onAdjustStart();
                }
            }

            @Override
            public void onStop() {
                if (mOnAdjustListener != null) {
                    mOnAdjustListener.onAdjustStop();
                }
            }
        });
    }

    private void initPointButton(ViewGroup viewGroup) {
        mPointButton = (ImageButton) viewGroup.findViewById(R.id.pointButton);
        mPointButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectAddOrRemoveButton(mPointButton);
            }
        });
    }

    private void initRemoveButton(ViewGroup viewGroup) {
        mRemoveButton = (ImageButton) viewGroup.findViewById(R.id.removeButton);
        mRemoveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectAddOrRemoveButton(mRemoveButton);
            }
        });
    }

    private void setButtonSelected(ImageButton button) {
        button.setAlpha(SELECTED_BUTTON_ALPHA);
    }

    private void setButtonUnselected(ImageButton button) {
        button.setAlpha(UNSELECTED_BUTTON_ALPHA);
    }

    private void selectAddOrRemoveButton(ImageButton button) {
        if (button == mPointButton) {
            if (mXyPlotView.getMode() == XyPlotView.Mode.ADD_POINT) {
                mXyPlotView.setMode(XyPlotView.Mode.NORMAL);
                setButtonUnselected(mPointButton);
            } else {
                mXyPlotView.setMode(XyPlotView.Mode.ADD_POINT);
                setButtonSelected(mPointButton);
            }
        } else {
            setButtonUnselected(mPointButton);
        }

        if (button == mRemoveButton) {
            if (mXyPlotView.getMode() == XyPlotView.Mode.REMOVE_POINT) {
                mXyPlotView.setMode(XyPlotView.Mode.NORMAL);
                setButtonUnselected(mRemoveButton);
            } else {
                mXyPlotView.setMode(XyPlotView.Mode.REMOVE_POINT);
                setButtonSelected(mRemoveButton);
            }
        } else {
            setButtonUnselected(mRemoveButton);
        }
    }

    private void selectRgbButton(ImageButton button) {
        mSelectedButton = button;

        if (button == mValueButton) {
            mSelectedAdapter = mValueAdapter;
            setButtonSelected(mValueButton);
        } else {
            setButtonUnselected(mValueButton);
        }

        if (button == mBlueButton) {
            mSelectedAdapter = mBlueAdapter;
            setButtonSelected(mBlueButton);
        } else {
            setButtonUnselected(mBlueButton);
        }

        if (button == mGreenButton) {
            mSelectedAdapter = mGreenAdapter;
            setButtonSelected(mGreenButton);
        } else {
            setButtonUnselected(mGreenButton);
        }

        if (button == mRedButton) {
            mSelectedAdapter = mRedAdapter;
            setButtonSelected(mRedButton);
        } else {
            setButtonUnselected(mRedButton);
        }
    }

    private abstract class Adapter implements AdjustAction {
        private int[] mInitPoints;
        private int[] mTemporaryPoints;
        private int[] mAppliedPoints;

        Adapter() {
            mInitPoints = getInitPoints();
            mTemporaryPoints = getPoints();
            mAppliedPoints = mTemporaryPoints;
        }

        protected abstract int[] getCurves();

        @ColorInt
        protected abstract int getCurvesColor();

        protected abstract int[] getInitPoints();

        protected abstract int[] getPoints();

        protected void setPoints(int[] points) {
            mTemporaryPoints = points;
        }

        @Override
        public void apply() {
            mAppliedPoints = mTemporaryPoints;
        }

        @Override
        public void reset() {
            setPoints(mInitPoints);
        }

        @Override
        public void cancel() {
            setPoints(mAppliedPoints);
        }
    }
}
