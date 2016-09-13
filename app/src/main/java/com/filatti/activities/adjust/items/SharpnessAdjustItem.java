package com.filatti.activities.adjust.items;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.filatti.R;
import com.filatti.activities.adjust.ui.ValueBarView;
import com.filatti.effects.EffectException;
import com.filatti.effects.adjusts.SharpnessAdjust;

import timber.log.Timber;

public class SharpnessAdjustItem extends AdjustItem<SharpnessAdjust> {
    private ValueBarView mValueBarView;

    public SharpnessAdjustItem(SharpnessAdjust effect) {
        super(effect);
    }

    @Override
    public int getDisplayName() {
        return R.string.sharpen;
    }

    @Override
    public int getIcon() {
        return R.drawable.sharpen;
    }

    @Override
    public void apply() {
        mValueBarView.apply();
    }

    @Override
    public void cancel() {
        mValueBarView.cancel();
        if (mOnAdjustListener != null) {
            mOnAdjustListener.onAdjustChange();
        }
    }

    @Override
    public void reset() {
        mValueBarView.reset();
        if (mOnAdjustListener != null) {
            mOnAdjustListener.onAdjustChange();
        }
    }

    @Override
    public View getOverlayView(Context context, ViewGroup rootView) {
        return null;
    }

    @Override
    public View getView(Context context, ViewGroup rootView) {
        LayoutInflater inflater = LayoutInflater.from(context);
        ViewGroup viewGroup =
                (ViewGroup) inflater.inflate(R.layout.sharpness_item_view, rootView, false);
        mValueBarView = (ValueBarView) viewGroup.findViewById(R.id.valueBar);
        mValueBarView.initialize(false,
                                 0,
                                 0,
                                 200,
                                 getInitFromEffect(),
                                 getFromEffect(),
                                 createOnValueChangeListener());
        return viewGroup;
    }

    private ValueBarView.OnValueChangeListener createOnValueChangeListener() {
        return new ValueBarView.OnValueChangeListener() {
            @Override
            public void onStart(int value) {
                if (mOnAdjustListener != null) {
                    mEffect.setRebuildBlurred(false);
                    mOnAdjustListener.onAdjustStart();
                }
            }

            @Override
            public void onStop(int value) {
                if (mOnAdjustListener != null) {
                    mEffect.setRebuildBlurred(true);
                    mOnAdjustListener.onAdjustStop();
                }
            }

            @Override
            public void onChange(int value) {
                setToEffect(value);
                if (mOnAdjustListener != null) {
                    mOnAdjustListener.onAdjustChange();
                }
            }
        };
    }

    private void setToEffect(int barValue) {
        double sharpness = barValue / 10.0;
        try {
            mEffect.setSharpness(sharpness);
        } catch (EffectException e) {
            Timber.e(e, "Failed to set sharpness %f", sharpness);
        }
    }

    private int convertFromEffect(double value) {
        return (int) (value * 10.0);
    }

    private int getFromEffect() {
        return convertFromEffect(mEffect.getSharpness());
    }

    private int getInitFromEffect() {
        return convertFromEffect(mEffect.getInitSharpness());
    }
}
