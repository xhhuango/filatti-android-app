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
import com.filatti.activities.adjust.ui.SliderView;
import com.filatti.effects.EffectException;
import com.filatti.utilities.StringUtil;
import com.filatti.effects.adjusts.TiltShiftAdjust;
import com.google.common.base.Preconditions;

import timber.log.Timber;

public class TiltShiftAdjustItem extends AdjustItem<TiltShiftAdjust> {
    private RadialPinchMaskView mRadialPinchMaskView;
    private LinearPinchMaskView mLinearPinchMaskView;

    private TextView mRadiusTextView;
    private TextView mAngleTextView;

    private PinchMaskAdapter mPinchMaskAdapter;
    private MaskTypeButtonAdapter mMaskTypeButtonAdapter;

    private StrengthSliderActionAdapter mStrengthSliderActionAdapter;
    private FeatheringSliderActionAdapter mFeatheringSliderActionAdapter;

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
        mPinchMaskAdapter.apply();
        mStrengthSliderActionAdapter.apply();
        mFeatheringSliderActionAdapter.apply();
    }

    @Override
    public void cancel() {
        mPinchMaskAdapter.cancel();
        mStrengthSliderActionAdapter.cancel();
        mFeatheringSliderActionAdapter.cancel();
        if (mOnAdjustListener != null) {
            mOnAdjustListener.onAdjustChange();
        }
    }

    @Override
    public void reset() {
        mPinchMaskAdapter.reset();
        mStrengthSliderActionAdapter.reset();
        mFeatheringSliderActionAdapter.reset();
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

        initStrengthView(viewGroup);
        initFeatheringView(viewGroup);

        mRadiusTextView = (TextView) viewGroup.findViewById(R.id.radiusTextView);
        mRadiusTextView.setText(StringUtil.valueToString(mEffect.getRadius()));

        mAngleTextView = (TextView) viewGroup.findViewById(R.id.angleTextView);
        mAngleTextView.setText(StringUtil.valueToString(mEffect.getAngle()));

        initMaskTypeButtons(viewGroup);

        return viewGroup;
    }

    private void initStrengthView(ViewGroup rootView) {
        ViewGroup viewGroup = (ViewGroup) rootView.findViewById(R.id.strengthTextSlider);
        TextView textView = (TextView) viewGroup.findViewById(R.id.sliderTextView);
        SliderView sliderView = (SliderView) viewGroup.findViewById(R.id.sliderView);
        mStrengthSliderActionAdapter = new StrengthSliderActionAdapter(textView, sliderView, 0, 100);
    }

    private void initFeatheringView(ViewGroup rootView) {
        ViewGroup viewGroup = (ViewGroup) rootView.findViewById(R.id.featheringTextSlider);
        TextView textView = (TextView) viewGroup.findViewById(R.id.sliderTextView);
        SliderView sliderView = (SliderView) viewGroup.findViewById(R.id.sliderView);
        mFeatheringSliderActionAdapter =
                new FeatheringSliderActionAdapter(textView, sliderView, 0, 100);
    }

    private void initMaskTypeButtons(ViewGroup viewGroup) {
        mMaskTypeButtonAdapter = new MaskTypeButtonAdapter();
        mMaskTypeButtonAdapter.setMaskType(mEffect.getMaskType());

        ImageButton circleButton = (ImageButton) viewGroup.findViewById(R.id.circleButton);
        circleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMaskTypeButtonAdapter.setMaskType(TiltShiftAdjust.MaskType.CIRCULAR);
            }
        });

        ImageButton ellipseButton = (ImageButton) viewGroup.findViewById(R.id.ellipseButton);
        ellipseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMaskTypeButtonAdapter.setMaskType(TiltShiftAdjust.MaskType.ELLIPTIC);
            }
        });

        ImageButton linearButton = (ImageButton) viewGroup.findViewById(R.id.linearButton);
        linearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMaskTypeButtonAdapter.setMaskType(TiltShiftAdjust.MaskType.LINEAR);
            }
        });
    }

    private class StrengthSliderActionAdapter extends SliderActionAdapter {
        StrengthSliderActionAdapter(TextView textView,
                                    SliderView sliderView,
                                    int minSliderView,
                                    int maxSliderView) {
            super(textView, sliderView, minSliderView, maxSliderView);
            setToEffect(getFromEffect());
        }

        @Override
        protected void setToEffect(int value) {
            float strength = value / 100.0f;
            mRadialPinchMaskView.setStrength(strength);
            mLinearPinchMaskView.setStrength(strength);
            try {
                mEffect.setStrength(strength);
            } catch (EffectException e) {
                Timber.e(e, "Failed to set strength %f", strength);
            }
        }

        private int convertFromEffect(float value) {
            return (int) (value * 100.0f);
        }

        @Override
        protected int getFromEffect() {
            return convertFromEffect(mEffect.getStrength());
        }

        @Override
        protected int getInitFromEffect() {
            return convertFromEffect(mEffect.getInitStrength());
        }

        @Override
        public void onStartTouch() {
            super.onStartTouch();
            mRadialPinchMaskView.display(true);
        }

        @Override
        public void onStopTouch() {
            super.onStopTouch();
            mRadialPinchMaskView.display(false);
        }

        @Override
        public void onSliderChange(int value, boolean fromUser) {
            setToEffect(value);
            mTextView.setText(String.valueOf(value));
        }

        @Override
        protected OnAdjustListener getOnAdjustListener() {
            return mOnAdjustListener;
        }
    }

    private class FeatheringSliderActionAdapter extends SliderActionAdapter {
        FeatheringSliderActionAdapter(TextView textView,
                                      SliderView sliderView,
                                      int minSliderView,
                                      int maxSliderView) {
            super(textView, sliderView, minSliderView, maxSliderView);
            setToEffect(getFromEffect());
        }

        @Override
        protected void setToEffect(int value) {
            float feathering = value / 100.0f;
            mRadialPinchMaskView.setInnerRadius(1.0f - feathering);
            mLinearPinchMaskView.setInnerRadius(1.0f - feathering);
            try {
                mEffect.setFeathering(feathering);
            } catch (EffectException e) {
                Timber.e(e, "Failed to set feathering %f", feathering);
            }
        }

        private int convertFromEffect(float value) {
            return (int) (value * 100.0f);
        }

        @Override
        protected int getFromEffect() {
            return convertFromEffect(mEffect.getFeathering());
        }

        @Override
        protected int getInitFromEffect() {
            return convertFromEffect(mEffect.getInitFeathering());
        }

        @Override
        public void onStartTouch() {
            super.onStartTouch();
            mRadialPinchMaskView.display(true);
        }

        @Override
        public void onStopTouch() {
            super.onStopTouch();
            mRadialPinchMaskView.display(false);
        }

        @Override
        public void onSliderChange(int value, boolean fromUser) {
            setToEffect(value);
            mTextView.setText(String.valueOf(value));
        }

        @Override
        protected OnAdjustListener getOnAdjustListener() {
            return mOnAdjustListener;
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

        private float[] mInitCenter;
        private float[] mAppliedCenter;
        private float[] mTemporaryCenter;

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
            return mEffect.getRadius();
        }

        private float getInitRadiusFromEffect() {
            return mEffect.getInitRadius();
        }

        private void setRadiusToEffect(float radius) {
            try {
                mEffect.setRadius(radius);
            } catch (EffectException e) {
                Timber.e(e, "Failed to set radius %f", radius);
            }
        }

        private float[] getCenterFromEffect() {
            return mEffect.getCenter();
        }

        private float[] getInitCenterFromEffect() {
            return mEffect.getInitCenter();
        }

        private void setCenterToEffect(float x, float y) {
            try {
                mTemporaryCenter[0] = x;
                mTemporaryCenter[1] = y;
                mEffect.setCenter(x, y);
            } catch (EffectException e) {
                Timber.e(e, "Failed to set center (%f, %f)", x, y);
            }
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
            mRadialPinchMaskView.setCenter(mInitCenter[0], mInitCenter[1]);
            mLinearPinchMaskView.setCenter(mInitCenter[0], mInitCenter[1]);

            setRadiusToEffect(mInitRadius);
            mRadialPinchMaskView.setOuterRadius(mInitRadius);
            mLinearPinchMaskView.setOuterRadius(mInitRadius);

            mRadiusTextView.setText(StringUtil.valueToString(mInitRadius));
        }
    }
}
