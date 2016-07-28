package com.filatti.activities.effect.items.adjusts;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.filatti.R;
import com.filatti.activities.effect.items.EffectItem;
import com.filatti.activities.effect.ui.ValueBarView;
import com.filatti.effects.EffectException;
import com.filatti.effects.adjusts.ContrastAdjust;
import com.filatti.logger.Logger;

public class ContrastAdjustItem extends EffectItem<ContrastAdjust> {
    private static final String TAG = ContrastAdjustItem.class.getSimpleName();

    private ViewGroup mViewGroup;
    private ValueBarView mValueBarView;

    public ContrastAdjustItem(ContrastAdjust effect, OnEffectChangeListener listener) {
        super(effect, listener);
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
                    (ViewGroup) inflater.inflate(R.layout.contrast_item_view, rootView, false);
            mValueBarView = (ValueBarView) mViewGroup.findViewById(R.id.valueBar);
            mValueBarView.initialize(false,
                                     0,
                                     -100,
                                     100,
                                     getFromEffect(),
                                     createOnValueChangeListener());
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
        try {
            mEffect.setContrast((barValue / 2.0 + 100.0) / 100.0);
        } catch (EffectException e) {
            Logger.error(TAG, e);
        }
    }

    private int getFromEffect() {
        return (int) (((mEffect.getContrast() * 100.0) - 100.0) * 2.0);
    }
}
