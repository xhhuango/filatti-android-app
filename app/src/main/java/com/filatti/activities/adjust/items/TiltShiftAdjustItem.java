package com.filatti.activities.adjust.items;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PointF;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.filatti.R;
import com.filatti.activities.adjust.AdjustAction;
import com.filatti.activities.adjust.ui.AbstractPinchMaskView;
import com.filatti.activities.adjust.ui.LinearPinchMaskView;
import com.filatti.activities.adjust.ui.RadialPinchMaskView;
import com.filatti.activities.adjust.ui.ValueBarView;
import com.filatti.effects.EffectException;
import com.filatti.utilities.StringUtil;
import com.filatti.effects.adjusts.TiltShiftAdjust;
import com.google.common.base.Preconditions;

import timber.log.Timber;

public class TiltShiftAdjustItem extends AdjustItem<TiltShiftAdjust> {
    private RadialPinchMaskView mRadialPinchMaskView;
    private LinearPinchMaskView mLinearPinchMaskView;

    private ValueBarView mStrengthValueBarView;
    private ValueBarView mFeatheringValueBarView;
    private TextView mRadiusTextView;
    private TextView mAngleTextView;
    private ImageButton mCircleButton;
    private ImageButton mEllipseButton;
    private ImageButton mLinearButton;

    private PinchMaskAdapter mPinchMaskAdapter;
    private MaskTypeButtonAdapter mMaskTypeButtonAdapter;

    public TiltShiftAdjustItem(TiltShiftAdjust effect) {
        super(effect);
    }

    @Override
    public int getDisplayName() {
        return R.string.tilt_shift;
    }

    @Override
    public int getIcon() {
        return R.drawable.tilt_shift;
    }

    @Override
    public void apply() {
        mStrengthValueBarView.apply();
        mFeatheringValueBarView.apply();
        mPinchMaskAdapter.apply();
    }

    @Override
    public void cancel() {
        mStrengthValueBarView.cancel();
        mFeatheringValueBarView.cancel();
        mPinchMaskAdapter.cancel();
        if (mOnAdjustListener != null) {
            mOnAdjustListener.onAdjustChange();
        }
    }

    @Override
    public void reset() {
        mStrengthValueBarView.reset();
        mFeatheringValueBarView.reset();
        mPinchMaskAdapter.reset();
        if (mOnAdjustListener != null) {
            mOnAdjustListener.onAdjustChange();
        }
    }

    @Override
    public boolean doesApplyUponAdjusting() {
        return false;
    }

    @Override
    public View getOverlayView(Context context, ViewGroup rootView) {
        LayoutInflater inflater = LayoutInflater.from(context);
        ViewGroup viewGroup =
                (ViewGroup) inflater.inflate(R.layout.tilt_shift_item_overlay_view, rootView, false);
        mPinchMaskAdapter = new PinchMaskAdapter();

        mRadialPinchMaskView = (RadialPinchMaskView) viewGroup.findViewById(R.id.radialOverlayView);
        mRadialPinchMaskView.setCircle(false);
        mRadialPinchMaskView.setColor(Color.WHITE);
        mRadialPinchMaskView.enableCenterMove(true);
        mRadialPinchMaskView.setOnChangeListener(mPinchMaskAdapter);

        mLinearPinchMaskView = (LinearPinchMaskView) viewGroup.findViewById(R.id.linearOverlayView);
        mLinearPinchMaskView.setColor(Color.WHITE);
        mLinearPinchMaskView.enableCenterMove(true);
        mLinearPinchMaskView.setOnChangeListener(mPinchMaskAdapter);
        mLinearPinchMaskView.setOnAngleChangeListener(mPinchMaskAdapter);

        mPinchMaskAdapter.setupRadialPinchMaskView();
        mPinchMaskAdapter.setupLinearPinchMaskView();

        return viewGroup;
    }

    @Override
    public View getView(Context context, ViewGroup rootView) {
        LayoutInflater inflater = LayoutInflater.from(context);
        ViewGroup viewGroup =
                (ViewGroup) inflater.inflate(R.layout.tilt_shit_item_view, rootView, false);

        mStrengthValueBarView = (ValueBarView) viewGroup.findViewById(R.id.strengthValueBar);
        ValueBarAdapter strengthValueBarAdapter = createStrengthAdapter();
        mStrengthValueBarView.initialize(true,
                                         R.string.tilt_shift_strength_title,
                                         0,
                                         100,
                                         strengthValueBarAdapter.getInitFromEffect(),
                                         strengthValueBarAdapter.getFromEffect(),
                                         strengthValueBarAdapter);

        mFeatheringValueBarView = (ValueBarView) viewGroup.findViewById(R.id.featheringValueBar);
        ValueBarAdapter featheringValueBarAdapter = createFeatheringAdapter();
        mFeatheringValueBarView.initialize(true,
                                           R.string.tilt_shift_feathering_title,
                                           0,
                                           100,
                                           featheringValueBarAdapter.getInitFromEffect(),
                                           featheringValueBarAdapter.getFromEffect(),
                                           featheringValueBarAdapter);

        mRadiusTextView = (TextView) viewGroup.findViewById(R.id.radiusTextView);
        mRadiusTextView.setText(StringUtil.valueToString(mEffect.getRadius()));

        mAngleTextView = (TextView) viewGroup.findViewById(R.id.angleTextView);
        mAngleTextView.setText(StringUtil.valueToString(mEffect.getAngle()));

        mMaskTypeButtonAdapter = new MaskTypeButtonAdapter();
        mMaskTypeButtonAdapter.setMaskType(mEffect.getMaskType());

        mCircleButton = (ImageButton) viewGroup.findViewById(R.id.circleButton);
        mCircleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMaskTypeButtonAdapter.setMaskType(TiltShiftAdjust.MaskType.CIRCULAR);
            }
        });

        mEllipseButton = (ImageButton) viewGroup.findViewById(R.id.ellipseButton);
        mEllipseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMaskTypeButtonAdapter.setMaskType(TiltShiftAdjust.MaskType.ELLIPTIC);
            }
        });

        mLinearButton = (ImageButton) viewGroup.findViewById(R.id.linearButton);
        mLinearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMaskTypeButtonAdapter.setMaskType(TiltShiftAdjust.MaskType.LINEAR);
            }
        });

        return viewGroup;
    }

    private ValueBarAdapter createStrengthAdapter() {
        return new ValueBarAdapter() {
            @Override
            protected void setToEffect(int value) {
                double strength = convertToEffect(value);
                mRadialPinchMaskView.setStrength((float) strength);
                mLinearPinchMaskView.setStrength((float) strength);
                try {
                    mEffect.setStrength(strength);
                } catch (EffectException e) {
                    Timber.e(e, "Failed to set strength %f", strength);
                }
            }

            @Override
            protected int getFromEffect() {
                return convertFromEffect(mEffect.getStrength());
            }

            @Override
            protected int getInitFromEffect() {
                return convertFromEffect(mEffect.getInitStrength());
            }

            private int convertFromEffect(double value) {
                return (int) (value * 100.0);
            }

            private double convertToEffect(int value) {
                return value / 100.0;
            }
        };
    }

    private ValueBarAdapter createFeatheringAdapter() {
        return new ValueBarAdapter() {
            @Override
            protected void setToEffect(int value) {
                double feathering = convertToEffect(value);
                mRadialPinchMaskView.setInnerRadius((float) (1.0 - feathering));
                mLinearPinchMaskView.setInnerRadius((float) (1.0 - feathering));
                try {
                    mEffect.setFeathering(feathering);
                } catch (EffectException e) {
                    Timber.e(e, "Failed to set feathering %f", feathering);
                }
            }

            @Override
            protected int getFromEffect() {
                return convertFromEffect(mEffect.getFeathering());
            }

            @Override
            protected int getInitFromEffect() {
                return convertFromEffect(mEffect.getInitFeathering());
            }

            private int convertFromEffect(double value) {
                return (int) (value * 100.0);
            }

            private double convertToEffect(int value) {
                return value / 100.0;
            }
        };
    }

    private abstract class ValueBarAdapter implements ValueBarView.OnValueChangeListener {
        abstract protected void setToEffect(int value);

        abstract protected int getFromEffect();

        abstract protected int getInitFromEffect();

        @Override
        public void onStart(int value) {
            if (mOnAdjustListener != null) {
                mOnAdjustListener.onAdjustStart();
            }
            mRadialPinchMaskView.display(true);
            mLinearPinchMaskView.display(true);
        }

        @Override
        public void onStop(int value) {
            if (mOnAdjustListener != null) {
                mOnAdjustListener.onAdjustStop();
            }
            mRadialPinchMaskView.display(false);
            mLinearPinchMaskView.display(false);
        }

        @Override
        public void onChange(int value) {
            setToEffect(value);
        }
    }

    private class MaskTypeButtonAdapter implements AdjustAction {
        private TiltShiftAdjust.MaskType mInitMaskType;
        private TiltShiftAdjust.MaskType mAppliedMaskType;
        private TiltShiftAdjust.MaskType mTemporaryMaskType;

        private MaskTypeButtonAdapter() {
            mInitMaskType = getInitFromEffect();
            mAppliedMaskType = getFromEffect();
            mTemporaryMaskType = mAppliedMaskType;
        }

        private TiltShiftAdjust.MaskType getInitFromEffect() {
            return mEffect.getInitMaskType();
        }

        private TiltShiftAdjust.MaskType getFromEffect() {
            return mEffect.getMaskType();
        }

        private void setToEffect(TiltShiftAdjust.MaskType maskType) {
            mTemporaryMaskType = maskType;
            mEffect.setMaskType(maskType);
        }

        private void setMaskType(TiltShiftAdjust.MaskType maskType) {
            Preconditions.checkNotNull(maskType);

            switch (maskType) {
                case CIRCULAR:
                    setToEffect(TiltShiftAdjust.MaskType.CIRCULAR);
                    mRadialPinchMaskView.setVisibility(View.VISIBLE);
                    mRadialPinchMaskView.setCircle(true);
                    mLinearPinchMaskView.setVisibility(View.GONE);
                    break;

                case ELLIPTIC:
                    setToEffect(TiltShiftAdjust.MaskType.ELLIPTIC);
                    mRadialPinchMaskView.setVisibility(View.VISIBLE);
                    mRadialPinchMaskView.setCircle(false);
                    mLinearPinchMaskView.setVisibility(View.GONE);
                    break;

                case LINEAR:
                    setToEffect(TiltShiftAdjust.MaskType.LINEAR);
                    mLinearPinchMaskView.setVisibility(View.VISIBLE);
                    mRadialPinchMaskView.setVisibility(View.GONE);
                    break;
            }

            if (mOnAdjustListener != null) {
                mOnAdjustListener.onAdjustChange();
            }
        }

        @Override
        public void apply() {
            mAppliedMaskType = mTemporaryMaskType;
        }

        @Override
        public void cancel() {
            setToEffect(mAppliedMaskType);
        }

        @Override
        public void reset() {
            setToEffect(mInitMaskType);
        }
    }

    private class PinchMaskAdapter
            implements AbstractPinchMaskView.OnChangeListener,
            LinearPinchMaskView.OnAngleChangeListener,
            AdjustAction {
        private float mInitRadius;
        private float mAppliedRadius;
        private float mTemporaryRadius;

        private double[] mInitCenter;
        private double[] mAppliedCenter;
        private double[] mTemporaryCenter;

        PinchMaskAdapter() {
            mInitRadius = getInitRadiusFromEffect();
            mAppliedRadius = getRadiusFromEffect();
            mTemporaryRadius = mAppliedRadius;

            mInitCenter = getInitCenterFromEffect();
            mAppliedCenter = getCenterFromEffect();
            mTemporaryCenter = mAppliedCenter;
        }

        private void setupRadialPinchMaskView() {
            mRadialPinchMaskView.setOuterRadius(mTemporaryRadius);
        }

        private void setupLinearPinchMaskView() {
            mLinearPinchMaskView.setOuterRadius(mTemporaryRadius);
        }

        private float getRadiusFromEffect() {
            return (float) mEffect.getRadius();
        }

        private float getInitRadiusFromEffect() {
            return (float) mEffect.getInitRadius();
        }

        private void setRadiusToEffect(float radius) {
            try {
                mEffect.setRadius(radius);
            } catch (EffectException e) {
                Timber.e(e, "Failed to set radius %f", radius);
            }
        }

        private double[] getCenterFromEffect() {
            return mEffect.getCenter();
        }

        private double[] getInitCenterFromEffect() {
            return mEffect.getInitCenter();
        }

        private void setCenterToEffect(double x, double y) {
            try {
                mTemporaryCenter[0] = x;
                mTemporaryCenter[1] = y;
                mEffect.setCenter(x, y);
            } catch (EffectException e) {
                Timber.e(e, "Failed to set center (%f, %f)", x, y);
            }
        }

        private float getInitAngleFromEffect() {
            return (float) mEffect.getInitAngle();
        }

        private float getAngleFromEffect() {
            return (float) mEffect.getAngle();
        }

        private void setAngleToEffect(float angle) {
            try {
                mEffect.setAngle(angle);
            } catch (EffectException e) {
                Timber.e(e, "Failed to set angle %f", angle);
            }
        }

        @Override
        public void onStartChange() {
            if (mOnAdjustListener != null) {
                mEffect.setRebuildBlurred(false);
                mOnAdjustListener.onAdjustStart();
            }
        }

        @Override
        public void onStopChange() {
            float radius;
            if (mRadialPinchMaskView.getVisibility() == View.VISIBLE) {
                radius = mRadialPinchMaskView.getOuterRadius();
                mLinearPinchMaskView.setOuterRadius(radius);
            } else {
                radius = mLinearPinchMaskView.getOuterRadius();
                mRadialPinchMaskView.setOuterRadius(radius);
            }
            setRadiusToEffect(radius);

            PointF center;
            if (mRadialPinchMaskView.getVisibility() == View.VISIBLE) {
                center = mRadialPinchMaskView.getCenter();
                mLinearPinchMaskView.setCenter(center.x, center.y);
            } else {
                center = mLinearPinchMaskView.getCenter();
                mRadialPinchMaskView.setCenter(center.x, center.y);
            }
            setCenterToEffect(center.x, center.y);

            setAngleToEffect(mLinearPinchMaskView.getAngle());

            if (mOnAdjustListener != null) {
                mEffect.setRebuildBlurred(true);
                mOnAdjustListener.onAdjustStop();
            }
        }

        @Override
        public void onRadiusChange(float radius) {
            mRadiusTextView.setText(StringUtil.valueToString(radius));
        }

        @Override
        public void onCenterChange(float x, float y) {
        }

        @Override
        public void onAngleChange(float angle) {
            mAngleTextView.setText(StringUtil.valueToString(angle));
        }

        @Override
        public void apply() {
            mAppliedRadius = mTemporaryRadius;
            mAppliedCenter = mTemporaryCenter;
        }

        @Override
        public void cancel() {
            setRadiusToEffect(mAppliedRadius);
            setCenterToEffect(mAppliedCenter[0], mAppliedCenter[1]);
        }

        @Override
        public void reset() {
            setCenterToEffect(mInitCenter[0], mInitCenter[1]);
            mRadialPinchMaskView.setCenter((float) mInitCenter[0], (float) mInitCenter[1]);
            mLinearPinchMaskView.setCenter((float) mInitCenter[0], (float) mInitCenter[1]);

            setRadiusToEffect(mInitRadius);
            mRadialPinchMaskView.setOuterRadius(mInitRadius);
            mLinearPinchMaskView.setOuterRadius(mInitRadius);

            mRadiusTextView.setText(StringUtil.valueToString(mInitRadius));
        }
    }
}
