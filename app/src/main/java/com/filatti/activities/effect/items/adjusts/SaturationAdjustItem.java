package com.filatti.activities.effect.items.adjusts;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.filatti.R;
import com.filatti.activities.effect.items.EffectItem;
import com.filatti.activities.effect.ui.ValueBarView;
import com.filatti.effects.EffectException;
import com.filatti.effects.adjusts.HsvAdjust;

import timber.log.Timber;

public class SaturationAdjustItem extends EffectItem<HsvAdjust> {
    private ViewGroup mViewGroup;
    private ValueBarView mValueBarView;

    public SaturationAdjustItem(HsvAdjust effect, OnEffectChangeListener listener) {
        super(effect, listener);
    }

    @Override
    public int getDisplayName() {
        return R.string.saturation;
    }

    @Override
    public int getIcon() {
        return R.drawable.saturation;
    }

    @Override
    public void apply() {
        mValueBarView.apply();
    }

    @Override
    public void cancel() {
        mValueBarView.cancel();
        mOnEffectChangeListener.onEffectChanged();
    }

    @Override
    public void reset() {
        mValueBarView.reset();
        mOnEffectChangeListener.onEffectChanged();
    }

    @Override
    public View getView(Context context, ViewGroup rootView) {
        if (mViewGroup == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            mViewGroup =
                    (ViewGroup) inflater.inflate(R.layout.saturation_item_view, rootView, false);
            mValueBarView = (ValueBarView) mViewGroup.findViewById(R.id.valueBar);
            mValueBarView
                    .initialize(false, 0, -100, 100, getFromEffect(), createOnValueChangeListener());
        }
        return mViewGroup;
    }

    private ValueBarView.OnValueChangeListener createOnValueChangeListener() {
        return new ValueBarView.OnValueChangeListener() {
            @Override
            public void onValueChanged(int value) {
                setToEffect(value);
                mOnEffectChangeListener.onEffectChanged();
            }
        };
    }

    private void setToEffect(int barValue) {
        double saturation = barValue / 100.0;
        Timber.d("Set barValue=" + barValue + " -> saturation=" + saturation);
        try {
            mEffect.setSaturation(saturation);
        } catch (EffectException e) {
            Timber.e(e, "Failed to set saturation " + saturation);
        }
    }

    private int getFromEffect() {
        return (int) (mEffect.getSaturation() * 100.0);
    }
}
