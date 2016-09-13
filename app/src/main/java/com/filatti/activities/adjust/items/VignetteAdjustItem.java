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
import com.filatti.activities.adjust.ui.ValueBarView;
import com.filatti.effects.EffectException;
import com.filatti.effects.adjusts.VignetteAdjust;

import java.text.DecimalFormat;

import timber.log.Timber;

public class VignetteAdjustItem extends AdjustItem<VignetteAdjust> {
    private RadialPinchMaskView mRadialPinchMaskView;
    private ValueBarView mStrengthValueBarView;
    private ValueBarView mFeatheringValueBarView;
    private ImageButton mColorButton;
    private TextView mRadiusTextView;

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
        mStrengthValueBarView.apply();
        mFeatheringValueBarView.apply();
        mRadialMaskAdapter.apply();
        mColorButtonAdapter.apply();
    }

    @Override
    public void cancel() {
        mStrengthValueBarView.cancel();
        mFeatheringValueBarView.cancel();
        mRadialMaskAdapter.cancel();
        mColorButtonAdapter.cancel();
        if (mOnAdjustListener != null) {
            mOnAdjustListener.onAdjustChange();
        }
    }

    @Override
    public void reset() {
        mStrengthValueBarView.reset();
        mFeatheringValueBarView.reset();
        mRadialMaskAdapter.reset();
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
        mRadialPinchMaskView.setOnRadiusChangeListener(mRadialMaskAdapter);

        return viewGroup;
    }

    public String formatRadius(float radius) {
        return new DecimalFormat("#.##").format(radius);
    }

    @Override
    public View getView(Context context, ViewGroup rootView) {
        LayoutInflater inflater = LayoutInflater.from(context);
        ViewGroup viewGroup =
                (ViewGroup) inflater.inflate(R.layout.vignette_item_view, rootView, false);

        mStrengthValueBarView = (ValueBarView) viewGroup.findViewById(R.id.strengthValueBar);
        ValueBarAdapter strengthValueBarAdapter = createStrengthAdapter();
        mStrengthValueBarView.initialize(true,
                                         R.string.vignette_strength_title,
                                         0,
                                         100,
                                         strengthValueBarAdapter.getInitFromEffect(),
                                         strengthValueBarAdapter.getFromEffect(),
                                         strengthValueBarAdapter);

        mFeatheringValueBarView = (ValueBarView) viewGroup.findViewById(R.id.featheringValueBar);
        ValueBarAdapter featheringValueBarAdapter = createFeatheringAdapter();
        mFeatheringValueBarView.initialize(true,
                                           R.string.vignette_feathering_title,
                                           0,
                                           100,
                                           featheringValueBarAdapter.getInitFromEffect(),
                                           featheringValueBarAdapter.getFromEffect(),
                                           featheringValueBarAdapter);

        mColorButton = (ImageButton) viewGroup.findViewById(R.id.colorButton);
        mColorButton.setBackgroundColor(mEffect.getColor());
        mColorButtonAdapter = new ColorButtonAdapter();
        mColorButton.setOnClickListener(mColorButtonAdapter);

        mRadiusTextView = (TextView) viewGroup.findViewById(R.id.radiusTextView);
        mRadiusTextView.setText(formatRadius((float) mEffect.getRadius()));

        return viewGroup;
    }

    private ValueBarAdapter createStrengthAdapter() {
        return new ValueBarAdapter() {
            @Override
            protected void setToEffect(int value) {
                double strength = convertToEffect(value);
                mRadialPinchMaskView.setStrength((float) strength);
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

            protected int convertFromEffect(double value) {
                return (int) (value * 100.0);
            }

            protected double convertToEffect(int value) {
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

            protected int convertFromEffect(double value) {
                return (int) (value * 100.0);
            }

            protected double convertToEffect(int value) {
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
        }

        @Override
        public void onStop(int value) {
            if (mOnAdjustListener != null) {
                mOnAdjustListener.onAdjustStop();
            }
            mRadialPinchMaskView.display(false);
        }

        @Override
        public void onChange(int value) {
            setToEffect(value);
        }
    }

    private class RadialMaskAdapter
            implements RadialPinchMaskView.OnRadiusChangeListener, AdjustAction {
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
        public void onStartRadiusChange(float radius) {
            if (mOnAdjustListener != null) {
                mOnAdjustListener.onAdjustStart();
            }
        }

        @Override
        public void onRadiusChange(float radius) {
            mRadiusTextView.setText(formatRadius(radius));
        }

        @Override
        public void onStopRadiusChange(float radius) {
            setToEffect(radius);
            if (mOnAdjustListener != null) {
                mOnAdjustListener.onAdjustStop();
            }
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
            mRadiusTextView.setText(formatRadius(mInitRadius));
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

                    try {
                        mEffect.setColor(color);
                    } catch (EffectException e) {
                        Timber.e(e, "Failed to set color");
                    }

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
