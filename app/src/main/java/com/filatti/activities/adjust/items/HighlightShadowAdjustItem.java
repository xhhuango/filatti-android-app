package com.filatti.activities.adjust.items;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.filatti.R;
import com.filatti.activities.adjust.ui.SliderView;
import com.filatti.effects.EffectException;
import com.filatti.effects.adjusts.HighlightShadowAdjust;
import com.filatti.effects.adjusts.Tone;

import timber.log.Timber;

public class HighlightShadowAdjustItem extends AdjustItem<HighlightShadowAdjust> {
    private AmountSliderActionAdapter mHighlightAmountSliderActionAdapter;
    private ToneWidthSliderActionAdapter mHighlightToneWidthSliderActionAdapter;
    private AmountSliderActionAdapter mShadowAmountSliderActionAdapter;
    private ToneWidthSliderActionAdapter mShadowToneWidthSliderActionAdapter;

    public HighlightShadowAdjustItem(HighlightShadowAdjust effect) {
        super(effect);
    }

    @Override
    public int getDisplayName() {
        return R.string.highlight_shadow;
    }

    @Override
    public int getIcon() {
        return R.drawable.highlight_shadow;
    }

    @Override
    public void apply() {
        mHighlightAmountSliderActionAdapter.apply();
        mHighlightToneWidthSliderActionAdapter.apply();
        mShadowAmountSliderActionAdapter.apply();
        mShadowToneWidthSliderActionAdapter.apply();
    }

    @Override
    public void cancel() {
        mHighlightAmountSliderActionAdapter.cancel();
        mHighlightToneWidthSliderActionAdapter.cancel();
        mShadowAmountSliderActionAdapter.cancel();
        mShadowToneWidthSliderActionAdapter.cancel();
        if (mOnAdjustListener != null) {
            mOnAdjustListener.onAdjustChange();
        }
    }

    @Override
    public void reset() {
        mHighlightAmountSliderActionAdapter.reset();
        mHighlightToneWidthSliderActionAdapter.reset();
        mShadowAmountSliderActionAdapter.reset();
        mShadowToneWidthSliderActionAdapter.reset();
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
                (ViewGroup) inflater.inflate(R.layout.highlight_shadow_item_view, rootView, false);

        initHighlightAmountView(viewGroup);
        initHighlightToneWidthview(viewGroup);
        initShadowAmountView(viewGroup);
        initShadowToneWidthView(viewGroup);

        return viewGroup;
    }

    private void initHighlightAmountView(ViewGroup rootView) {
        ViewGroup viewGroup = (ViewGroup) rootView.findViewById(R.id.highlightAmountTextSlider);
        TextView textView = (TextView) viewGroup.findViewById(R.id.sliderTextView);
        SliderView sliderView = (SliderView) viewGroup.findViewById(R.id.sliderView);
        mHighlightAmountSliderActionAdapter =
                new AmountSliderActionAdapter(textView, sliderView, -100, 100) {
                    @Override
                    Tone getTone() {
                        return Tone.HIGHLIGHTS;
                    }
                };
    }

    private void initHighlightToneWidthview(ViewGroup rootView) {
        ViewGroup viewGroup = (ViewGroup) rootView.findViewById(R.id.highlightToneWidthTextSlider);
        TextView textView = (TextView) viewGroup.findViewById(R.id.sliderTextView);
        SliderView sliderView = (SliderView) viewGroup.findViewById(R.id.sliderView);
        mHighlightToneWidthSliderActionAdapter =
                new ToneWidthSliderActionAdapter(textView, sliderView, 0, 100) {
                    @Override
                    Tone getTone() {
                        return Tone.HIGHLIGHTS;
                    }
                };
    }

    private void initShadowAmountView(ViewGroup rootView) {
        ViewGroup viewGroup = (ViewGroup) rootView.findViewById(R.id.shadowAmountTextSlider);
        TextView textView = (TextView) viewGroup.findViewById(R.id.sliderTextView);
        SliderView sliderView = (SliderView) viewGroup.findViewById(R.id.sliderView);
        mShadowAmountSliderActionAdapter =
                new AmountSliderActionAdapter(textView, sliderView, -100, 100) {
                    @Override
                    Tone getTone() {
                        return Tone.SHADOWS;
                    }
                };
    }

    private void initShadowToneWidthView(ViewGroup rootView) {
        ViewGroup viewGroup = (ViewGroup) rootView.findViewById(R.id.shadowToneWidthTextSlider);
        TextView textView = (TextView) viewGroup.findViewById(R.id.sliderTextView);
        SliderView sliderView = (SliderView) viewGroup.findViewById(R.id.sliderView);
        mShadowToneWidthSliderActionAdapter =
                new ToneWidthSliderActionAdapter(textView, sliderView, 0, 100) {
                    @Override
                    Tone getTone() {
                        return Tone.SHADOWS;
                    }
                };
    }

    private abstract class AmountSliderActionAdapter extends SliderActionAdapter {
        AmountSliderActionAdapter(TextView textView,
                                  SliderView sliderView,
                                  int minSliderView,
                                  int maxSliderView) {
            super(textView, sliderView, minSliderView, maxSliderView);
        }

        abstract Tone getTone();

        @Override
        protected void setToEffect(int value) {
            float amount = value / 100.0f;
            try {
                mEffect.setAmount(getTone(), amount);
            } catch (EffectException e) {
                Timber.e(e, "Failed to set amount %f", amount);
            }
        }

        private int convertFromEffect(float value) {
            return (int) (value * 100.0f);
        }

        @Override
        protected int getFromEffect() {
            return convertFromEffect(mEffect.getAmount(getTone()));
        }

        @Override
        protected int getInitFromEffect() {
            return convertFromEffect(mEffect.getInitAmount(getTone()));
        }

        @Override
        protected OnAdjustListener getOnAdjustListener() {
            return mOnAdjustListener;
        }
    }

    private abstract class ToneWidthSliderActionAdapter extends SliderActionAdapter {
        ToneWidthSliderActionAdapter(TextView textView,
                                     SliderView sliderView,
                                     int minSliderView,
                                     int maxSliderView) {
            super(textView, sliderView, minSliderView, maxSliderView);
        }

        abstract Tone getTone();

        @Override
        protected void setToEffect(int value) {
            float width = value / 100.0f / 2.0f;
            try {
                mEffect.setToneWidth(getTone(), width);
            } catch (EffectException e) {
                Timber.e(e, "Failed to set tone width %f", width);
            }
        }

        private int convertFromEffect(float value) {
            return (int) (value * 2.0f * 100.0f);
        }

        @Override
        protected int getFromEffect() {
            return convertFromEffect(mEffect.getToneWidth(getTone()));
        }

        @Override
        protected int getInitFromEffect() {
            return convertFromEffect(mEffect.getInitToneWidth(getTone()));
        }

        @Override
        protected OnAdjustListener getOnAdjustListener() {
            return mOnAdjustListener;
        }
    }
}
