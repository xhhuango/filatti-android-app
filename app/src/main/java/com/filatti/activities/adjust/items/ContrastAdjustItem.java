package com.filatti.activities.adjust.items;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.filatti.R;
import com.filatti.activities.adjust.ui.OnAffectListener;
import com.filatti.activities.adjust.ui.ValueBarView;
import com.filatti.effects.EffectException;
import com.filatti.effects.adjusts.ContrastAdjust;

import timber.log.Timber;

public class ContrastAdjustItem extends AdjustItem<ContrastAdjust> {
    private ValueBarView mValueBarView;

    public ContrastAdjustItem(ContrastAdjust effect) {
        super(effect);
    }

    @Override
    public int getDisplayName() {
        return R.string.contrast;
    }

    @Override
    public int getIcon() {
        return R.drawable.contrast;
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
                (ViewGroup) inflater.inflate(R.layout.contrast_item_view, rootView, false);
        mValueBarView = (ValueBarView) viewGroup.findViewById(R.id.valueBar);
        mValueBarView.initialize(false,
                                 0,
                                 -100,
                                 100,
                                 getInitFromEffect(),
                                 getFromEffect(),
                                 createOnAffectListener(),
                                 createOnValueChangeListener());
        return viewGroup;
    }

    private OnAffectListener createOnAffectListener() {
        return new OnAffectListener() {
            @Override
            public void onStartAffect() {
                mOnAdjustListener.onAdjustStart();
            }

            @Override
            public void onStopAffect() {
                mOnAdjustListener.onAdjustStop();
            }
        };
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
        double contrast = (barValue / 2.0 + 100.0) / 100.0;
        try {
            mEffect.setContrast(contrast);
        } catch (EffectException e) {
            Timber.e(e, "Failed to set contrast %f", contrast);
        }
    }

    private int convertFromEffect(double valueFromEffect) {
        return (int) (((valueFromEffect * 100.0) - 100.0) * 2.0);
    }

    private int getFromEffect() {
        return convertFromEffect(mEffect.getContrast());
    }

    private int getInitFromEffect() {
        return convertFromEffect(mEffect.getInitContrast());
    }
}
