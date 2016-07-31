package com.filatti.activities.effect.items.adjusts;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.filatti.R;
import com.filatti.activities.effect.items.EffectItem;
import com.filatti.activities.effect.ui.ValueBarView;
import com.filatti.effects.EffectException;
import com.filatti.effects.adjusts.VignetteAdjust;

import timber.log.Timber;

public class VignetteAdjustItem extends EffectItem<VignetteAdjust> {
    private ViewGroup mViewGroup;
    private ValueBarView mStrengthValueBarView;
    private ValueBarView mFeatheringValueBarView;
    private ValueBarView mRadiusValueBarView;

    public VignetteAdjustItem(VignetteAdjust effect, OnEffectChangeListener listener) {
        super(effect, listener);
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
        mRadiusValueBarView.apply();
    }

    @Override
    public void cancel() {
        mStrengthValueBarView.cancel();
        mFeatheringValueBarView.cancel();
        mRadiusValueBarView.cancel();
        mOnEffectChangeListener.onEffectChanged();
    }

    @Override
    public void reset() {
        mStrengthValueBarView.reset();
        mFeatheringValueBarView.reset();
        mRadiusValueBarView.reset();
        mOnEffectChangeListener.onEffectChanged();
    }

    @Override
    public View getView(Context context, ViewGroup rootView) {
        if (mViewGroup == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            mViewGroup =
                    (ViewGroup) inflater.inflate(R.layout.vignette_item_view, rootView, false);

            mStrengthValueBarView = (ValueBarView) mViewGroup.findViewById(R.id.strengthValueBar);
            mStrengthValueBarView.initialize(true,
                                             R.string.vignette_strength_title,
                                             0,
                                             100,
                                             getStrengthFromEffect(),
                                             createStrengthOnValueChangeListener());

            mFeatheringValueBarView =
                    (ValueBarView) mViewGroup.findViewById(R.id.featheringValueBar);
            mFeatheringValueBarView.initialize(true,
                                               R.string.vignette_feathering_title,
                                               0,
                                               100,
                                               getFeatheringFromEffect(),
                                               createFeatheringOnValueChangeListener());

            mRadiusValueBarView = (ValueBarView) mViewGroup.findViewById(R.id.radiusValueBar);
            mRadiusValueBarView.initialize(true,
                                           R.string.vignette_radius_title,
                                           0,
                                           100,
                                           getRadiusFromEffect(),
                                           createRadiusOnValueChangeListener());
        }
        return mViewGroup;
    }

    private ValueBarView.OnValueChangeListener createStrengthOnValueChangeListener() {
        return new ValueBarView.OnValueChangeListener() {
            @Override
            public void onValueChanged(int value) {
                setStrengthToEffect(value);
                mOnEffectChangeListener.onEffectChanged();
            }
        };
    }

    private void setStrengthToEffect(int barValue) {
        double strength = barValue / 100.0;
        Timber.d("Set barValue=" + barValue + " -> strength=" + strength);
        try {
            mEffect.setStrength(strength);
        } catch (EffectException e) {
            Timber.e(e, "Failed to set strength " + strength);
        }
    }

    private int getStrengthFromEffect() {
        return (int) (mEffect.getStrength() * 100.0);
    }

    private ValueBarView.OnValueChangeListener createFeatheringOnValueChangeListener() {
        return new ValueBarView.OnValueChangeListener() {
            @Override
            public void onValueChanged(int value) {
                setFeatheringToEffect(value);
                mOnEffectChangeListener.onEffectChanged();
            }
        };
    }

    private void setFeatheringToEffect(int barValue) {
        double feathering = barValue / 100.0;
        Timber.d("Set barValue=" + barValue + " -> feathering=" + feathering);
        try {
            mEffect.setFeathering(feathering);
        } catch (EffectException e) {
            Timber.e(e, "Failed to set feathering " + feathering);
        }
    }

    private int getFeatheringFromEffect() {
        return (int) (mEffect.getFeathering() * 100.0);
    }

    private ValueBarView.OnValueChangeListener createRadiusOnValueChangeListener() {
        return new ValueBarView.OnValueChangeListener() {
            @Override
            public void onValueChanged(int value) {
                setRadiusToEffect(value);
                mOnEffectChangeListener.onEffectChanged();
            }
        };
    }

    private void setRadiusToEffect(int barValue) {
        double radius = barValue / 100.0;
        Timber.d("Set barValue=" + barValue + " -> radius=" + radius);
        try {
            mEffect.setRadius(radius);
        } catch (EffectException e) {
            Timber.e(e, "Failed to set radius " + radius);
        }
    }

    private int getRadiusFromEffect() {
        return (int) (mEffect.getRadius() * 100.0);
    }
}
