package com.filatti.activities.adjust.items;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.azeesoft.lib.colorpicker.ColorPickerDialog;
import com.filatti.R;
import com.filatti.activities.adjust.AdjustAction;
import com.filatti.activities.adjust.ui.RadialPinchMaskView;
import com.filatti.activities.adjust.ui.SliderView;
import com.filatti.effects.EffectException;
import com.filatti.utilities.StringUtil;
import com.filatti.effects.adjusts.VignetteAdjust;

import timber.log.Timber;

public class VignetteAdjustItem extends AdjustItem<VignetteAdjust> {
    private RadialPinchMaskView mRadialPinchMaskView;

    private ImageButton mColorButton;
    private TextView mRadiusTextView;

    private StrengthSliderActionAdapter mStrengthSliderActionAdapter;
    private FeatheringSliderActionAdapter mFeatheringSliderActionAdapter;
    private RadialMaskAdapter mRadialMaskAdapter;
    private ColorButtonAdapter mColorButtonAdapter;

    public VignetteAdjustItem(VignetteAdjust effect) {
        super(effect);
    }

    @Override
    public int getDisplayName() {
        return R.string.vignette;
    }

    @Override
    public int getIcon() {
        return R.drawable.vignette;
    }

    @Override
    public void apply() {
        mRadialMaskAdapter.apply();
        mStrengthSliderActionAdapter.apply();
        mFeatheringSliderActionAdapter.apply();
        mColorButtonAdapter.apply();
    }

    @Override
    public void cancel() {
        mRadialMaskAdapter.cancel();
        mStrengthSliderActionAdapter.cancel();
        mFeatheringSliderActionAdapter.cancel();
        mColorButtonAdapter.cancel();
        if (mOnAdjustListener != null) {
            mOnAdjustListener.onAdjustChange();
        }
    }

    @Override
    public void reset() {
        mRadialMaskAdapter.reset();
        mStrengthSliderActionAdapter.reset();
        mFeatheringSliderActionAdapter.reset();
        mColorButtonAdapter.reset();
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
                (ViewGroup) inflater.inflate(R.layout.vignette_item_overlay_view, rootView, false);
        mRadialPinchMaskView = (RadialPinchMaskView) viewGroup.findViewById(R.id.overlayView);
        mRadialPinchMaskView.setCircle(false);
        mRadialMaskAdapter = new RadialMaskAdapter();
        mRadialPinchMaskView.setOnChangeListener(mRadialMaskAdapter);
        mRadialPinchMaskView.enableCenterMove(false);

        return viewGroup;
    }

    @Override
    public View getView(Context context, ViewGroup rootView) {
        LayoutInflater inflater = LayoutInflater.from(context);
        ViewGroup viewGroup =
                (ViewGroup) inflater.inflate(R.layout.vignette_item_view, rootView, false);

        initStrengthView(viewGroup);
        initFeatheringView(viewGroup);

        mColorButton = (ImageButton) viewGroup.findViewById(R.id.colorButton);
        mColorButton.setBackgroundColor(mEffect.getColor());
        mColorButtonAdapter = new ColorButtonAdapter();
        mColorButton.setOnClickListener(mColorButtonAdapter);

        mRadiusTextView = (TextView) viewGroup.findViewById(R.id.radiusTextView);
        mRadiusTextView.setText(StringUtil.valueToString(mEffect.getRadius()));

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
            double strength = value / 100.0;
            mRadialPinchMaskView.setStrength((float) strength);
            try {
                mEffect.setStrength(strength);
            } catch (EffectException e) {
                Timber.e(e, "Failed to set strength %f", strength);
            }
        }

        private int convertFromEffect(double value) {
            return (int) (value * 100.0);
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
            double feathering = value / 100.0;
            mRadialPinchMaskView.setInnerRadius((float) (1.0 - feathering));
            try {
                mEffect.setFeathering(feathering);
            } catch (EffectException e) {
                Timber.e(e, "Failed to set feathering %f", feathering);
            }
        }

        private int convertFromEffect(double value) {
            return (int) (value * 100.0);
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

    private class RadialMaskAdapter
            implements RadialPinchMaskView.OnChangeListener, AdjustAction {
        private float mInitRadius;
        private float mAppliedRadius;
        private float mTemporaryRadius;

        RadialMaskAdapter() {
            mInitRadius = getInitFromEffect();
            mAppliedRadius = getFromEffect();
            mTemporaryRadius = mAppliedRadius;

            mRadialPinchMaskView.setOuterRadius(mAppliedRadius);
        }

        private float getFromEffect() {
            return (float) mEffect.getRadius();
        }

        private float getInitFromEffect() {
            return (float) mEffect.getInitRadius();
        }

        private void setToEffect(float value) {
            try {
                mEffect.setRadius(value);
            } catch (EffectException e) {
                Timber.e(e, "Failed to set radius %f", value);
            }
        }

        @Override
        public void onStartChange() {
            if (mOnAdjustListener != null) {
                mOnAdjustListener.onAdjustStart();
            }
        }

        @Override
        public void onStopChange() {
            setToEffect(mRadialPinchMaskView.getOuterRadius());
            if (mOnAdjustListener != null) {
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
        public void apply() {
            mAppliedRadius = mTemporaryRadius;
        }

        @Override
        public void cancel() {
            setToEffect(mAppliedRadius);
        }

        @Override
        public void reset() {
            setToEffect(mInitRadius);
            mRadialPinchMaskView.setOuterRadius(mInitRadius);
            mRadiusTextView.setText(StringUtil.valueToString(mInitRadius));
        }
    }

    private class ColorButtonAdapter implements View.OnClickListener, AdjustAction {
        @ColorInt
        private int mInitColor;
        @ColorInt
        private int mAppliedColor;
        @ColorInt
        private int mTemporaryColor;

        ColorButtonAdapter() {
            mInitColor = mEffect.getInitColor();
            mAppliedColor = mEffect.getColor();
            setTemporaryColor(mAppliedColor);
        }

        private void setToEffect(@ColorInt int color) {
            try {
                mEffect.setColor(color);
            } catch (EffectException e) {
                Timber.e(e, "Failed to set color");
            }
        }

        private void setTemporaryColor(@ColorInt int color) {
            mTemporaryColor = color;
            mRadialPinchMaskView.setColor(color);
            mColorButton.setBackgroundColor(color);
        }

        @Override
        public void apply() {
            mAppliedColor = mTemporaryColor;
        }

        @Override
        public void cancel() {
            setToEffect(mAppliedColor);
        }

        @Override
        public void reset() {
            setToEffect(mInitColor);
            setTemporaryColor(mInitColor);
        }

        @Override
        public void onClick(View view) {
            ColorPickerDialog dialog =
                    ColorPickerDialog.createColorPickerDialog(view.getContext());
            dialog.setOnColorPickedListener(new ColorPickerDialog.OnColorPickedListener() {
                @Override
                public void onColorPicked(int color, String hexVal) {
                    setTemporaryColor(color);

                    setToEffect(color);

                    if (mOnAdjustListener != null) {
                        mOnAdjustListener.onAdjustStop();
                    }
                }
            });
            dialog.setLastColor(mTemporaryColor);
            dialog.hideOpacityBar();
            dialog.setHexaDecimalTextColor(Color.BLACK);
            dialog.show();
        }
    }
}
