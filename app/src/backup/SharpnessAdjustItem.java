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
        mOnAdjustListener.onAdjustChange();
    }

    @Override
    public void reset() {
        mValueBarView.reset();
        mOnAdjustListener.onAdjustChange();
    }

    @Override
    public View getView(Context context, ViewGroup rootView) {
        LayoutInflater inflater = LayoutInflater.from(context);
        ViewGroup viewGroup =
                (ViewGroup) inflater.inflate(R.layout.sharpness_item_view, rootView, false);
        mValueBarView = (ValueBarView) viewGroup.findViewById(R.id.valueBar);
        mValueBarView
                .initialize(false, 0, 0, 100, getFromEffect(), createOnValueChangeListener());
        return viewGroup;
    }

    private ValueBarView.OnValueChangeListener createOnValueChangeListener() {
        return new ValueBarView.OnValueChangeListener() {
            @Override
            public void onValueChanged(int value) {
                setToEffect(value);
                mOnAdjustListener.onAdjustChange();
            }
        };
    }

    private void setToEffect(int barValue) {
        double sharpness = barValue / 100.0;
        Timber.d("Set barValue=%d -> sharpness=%f", barValue, sharpness);
        try {
            mEffect.setSharpness(sharpness);
        } catch (EffectException e) {
            Timber.e(e, "Failed to set sharpness %f", sharpness);
        }
    }

    private int getFromEffect() {
        return (int) (mEffect.getSharpness() * 100.0);
    }
}
