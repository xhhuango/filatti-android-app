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
import com.filatti.logger.Logger;

public class VignetteAdjustItem extends EffectItem<VignetteAdjust> {
    private static final String TAG = VignetteAdjustItem.class.getSimpleName();

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
        mFeatheringValueBarView.apply();
        mRadiusValueBarView.apply();
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
                                             R.string.strength_title,
                                             0,
                                             100,
                                             getStrengthFromEffect(),
                                             createStrengthOnValueChangeListener());

            mFeatheringValueBarView =
                    (ValueBarView) mViewGroup.findViewById(R.id.featheringValueBar);
            mFeatheringValueBarView.initialize(true,
                                               R.string.feathering_title,
                                               0,
                                               100,
                                               getFeatheringFromEffect(),
                                               createFeatheringOnValueChangeListener());

            mRadiusValueBarView = (ValueBarView) mViewGroup.findViewById(R.id.radiusValueBar);
            mRadiusValueBarView.initialize(true,
                                           R.string.radius_title,
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
        try {
            mEffect.setStrength(barValue / 100.0);
        } catch (EffectException e) {
            Logger.error(TAG, e);
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
        try {
            mEffect.setFeathering(barValue / 100.0);
        } catch (EffectException e) {
            Logger.error(TAG, e);
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
        try {
            mEffect.setRadius(barValue / 100.0);
        } catch (EffectException e) {
            Logger.error(TAG, e);
        }
    }

    private int getRadiusFromEffect() {
        return (int) (mEffect.getRadius() * 100.0);
    }
}
