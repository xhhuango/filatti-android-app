package com.filatti.activities.adjust.items;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.filatti.R;
import com.filatti.activities.adjust.AdjustAction;
import com.filatti.activities.adjust.ui.SliderView;
import com.filatti.effects.EffectException;
import com.filatti.effects.adjusts.ColorBalanceAdjust;
import com.filatti.effects.adjusts.Tone;
import com.google.common.base.Preconditions;

import java.util.Arrays;

import timber.log.Timber;

public class ColorBalanceAdjustItem extends AdjustItem<ColorBalanceAdjust> {
    private RedCyanSliderActionAdapter mRedCyanSliderActionAdapter;
    private GreenMagentaSliderActionAdapter mGreenMagentaSliderActionAdapter;
    private BlueYellowSliderActionAdapter mBlueYellowSliderActionAdapter;

    private Tone mSelectedTone;

    public ColorBalanceAdjustItem(ColorBalanceAdjust effect) {
        super(effect);
    }

    @Override
    public int getDisplayName() {
        return R.string.color_balance;
    }

    @Override
    public int getIcon() {
        return R.drawable.color_balance;
    }

    @Override
    public void apply() {
        mRedCyanSliderActionAdapter.apply();
        mGreenMagentaSliderActionAdapter.apply();
        mBlueYellowSliderActionAdapter.apply();
    }

    @Override
    public void cancel() {
        mRedCyanSliderActionAdapter.cancel();
        mGreenMagentaSliderActionAdapter.cancel();
        mBlueYellowSliderActionAdapter.cancel();
        if (mOnAdjustListener != null) {
            mOnAdjustListener.onAdjustChange();
        }
    }

    @Override
    public void reset() {
        mRedCyanSliderActionAdapter.reset();
        mGreenMagentaSliderActionAdapter.reset();
        mBlueYellowSliderActionAdapter.reset();
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
                (ViewGroup) inflater.inflate(R.layout.color_balance_item_view, rootView, false);

        initRedCyanView(viewGroup);
        initGreenMagentaView(viewGroup);
        initBlueYellowView(viewGroup);
        initButtons(viewGroup);

        setTone(Tone.SHADOWS);

        return viewGroup;
    }

    private void initRedCyanView(ViewGroup rootView) {
        ViewGroup viewGroup = (ViewGroup) rootView.findViewById(R.id.redCyanTextSlider);
        TextView textView = (TextView) viewGroup.findViewById(R.id.sliderTextView);
        SliderView sliderView = (SliderView) viewGroup.findViewById(R.id.sliderView);
        mRedCyanSliderActionAdapter =
                new RedCyanSliderActionAdapter(textView, sliderView, -100, 100);
    }

    private void initGreenMagentaView(ViewGroup rootView) {
        ViewGroup viewGroup = (ViewGroup) rootView.findViewById(R.id.greenMagentaTextSlider);
        TextView textView = (TextView) viewGroup.findViewById(R.id.sliderTextView);
        SliderView sliderView = (SliderView) viewGroup.findViewById(R.id.sliderView);
        mGreenMagentaSliderActionAdapter =
                new GreenMagentaSliderActionAdapter(textView, sliderView, -100, 100);
    }

    private void initBlueYellowView(ViewGroup rootView) {
        ViewGroup viewGroup = (ViewGroup) rootView.findViewById(R.id.blueYellowTextSlider);
        TextView textView = (TextView) viewGroup.findViewById(R.id.sliderTextView);
        SliderView sliderView = (SliderView) viewGroup.findViewById(R.id.sliderView);
        mBlueYellowSliderActionAdapter =
                new BlueYellowSliderActionAdapter(textView, sliderView, -100, 100);
    }

    private void initButtons(ViewGroup rootView) {
        Button shadows = (Button) rootView.findViewById(R.id.shadowsButton);
        shadows.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTone(Tone.SHADOWS);
            }
        });

        Button midtones = (Button) rootView.findViewById(R.id.midtonesButton);
        midtones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTone(Tone.MIDTONES);
            }
        });

        Button highlight = (Button) rootView.findViewById(R.id.highlightButton);
        highlight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTone(Tone.HIGHLIGHTS);
            }
        });
    }

    private void setTone(Tone tone) {
        Preconditions.checkNotNull(tone);
        if (tone != mSelectedTone) {
            mSelectedTone = tone;
            mRedCyanSliderActionAdapter.setTone(mSelectedTone);
            mGreenMagentaSliderActionAdapter.setTone(mSelectedTone);
            mBlueYellowSliderActionAdapter.setTone(mSelectedTone);
        }
    }

    private class RedCyanSliderActionAdapter extends ColorBalanceSliderActionAdapter {
        RedCyanSliderActionAdapter(TextView textView,
                                   SliderView sliderView,
                                   int minSliderView,
                                   int maxSliderView) {
            super(textView, sliderView, minSliderView, maxSliderView);
        }

        @Override
        protected int[] getFromEffect() {
            return new int[]{
                    mEffect.getRedCyan(Tone.SHADOWS),
                    mEffect.getRedCyan(Tone.MIDTONES),
                    mEffect.getRedCyan(Tone.HIGHLIGHTS)};
        }

        @Override
        protected int[] getInitFromEffect() {
            return new int[]{
                    mEffect.getInitRedCyan(Tone.SHADOWS),
                    mEffect.getInitRedCyan(Tone.MIDTONES),
                    mEffect.getInitRedCyan(Tone.HIGHLIGHTS)};
        }

        @Override
        protected void setToEffect(Tone tone, int value) {
            super.setToEffect(tone, value);
            try {
                mEffect.setRedCyan(tone, value);
            } catch (EffectException e) {
                Timber.e(e, "Failed to set red/cyan %d", value);
            }
        }
    }

    private class GreenMagentaSliderActionAdapter extends ColorBalanceSliderActionAdapter {
        GreenMagentaSliderActionAdapter(TextView textView,
                                        SliderView sliderView,
                                        int minSliderView,
                                        int maxSliderView) {
            super(textView, sliderView, minSliderView, maxSliderView);
        }

        @Override
        protected int[] getFromEffect() {
            return new int[]{
                    mEffect.getGreenMagenta(Tone.SHADOWS),
                    mEffect.getGreenMagenta(Tone.MIDTONES),
                    mEffect.getGreenMagenta(Tone.HIGHLIGHTS)};
        }

        @Override
        protected int[] getInitFromEffect() {
            return new int[]{
                    mEffect.getInitGreenMagenta(Tone.SHADOWS),
                    mEffect.getInitGreenMagenta(Tone.MIDTONES),
                    mEffect.getInitGreenMagenta(Tone.HIGHLIGHTS)};
        }

        @Override
        protected void setToEffect(Tone tone, int value) {
            super.setToEffect(tone, value);
            try {
                mEffect.setGreenMagenta(tone, value);
            } catch (EffectException e) {
                Timber.e(e, "Failed to set green/magenta %d", value);
            }
        }
    }

    private class BlueYellowSliderActionAdapter extends ColorBalanceSliderActionAdapter {
        BlueYellowSliderActionAdapter(TextView textView,
                                      SliderView sliderView,
                                      int minSliderView,
                                      int maxSliderView) {
            super(textView, sliderView, minSliderView, maxSliderView);
        }

        @Override
        protected int[] getFromEffect() {
            return new int[]{
                    mEffect.getBlueYellow(Tone.SHADOWS),
                    mEffect.getBlueYellow(Tone.MIDTONES),
                    mEffect.getBlueYellow(Tone.HIGHLIGHTS)};
        }

        @Override
        protected int[] getInitFromEffect() {
            return new int[]{
                    mEffect.getInitBlueYellow(Tone.SHADOWS),
                    mEffect.getInitBlueYellow(Tone.MIDTONES),
                    mEffect.getInitBlueYellow(Tone.HIGHLIGHTS)};
        }

        @Override
        protected void setToEffect(Tone tone, int value) {
            super.setToEffect(tone, value);
            try {
                mEffect.setBlueYellow(tone, value);
            } catch (EffectException e) {
                Timber.e(e, "Failed to set blue/yellow %d", value);
            }
        }
    }

    private abstract class ColorBalanceSliderActionAdapter
            implements SliderView.OnSliderChangeListener, AdjustAction {
        private int[] mInit;
        private int[] mApplied;
        private int[] mTemporary;

        TextView mTextView;
        private SliderView mSliderView;

        private Tone mTone;

        ColorBalanceSliderActionAdapter(TextView textView,
                                        SliderView sliderView,
                                        int minSliderView,
                                        int maxSliderView) {
            Preconditions.checkNotNull(textView);
            Preconditions.checkNotNull(sliderView);

            mTextView = textView;
            mSliderView = sliderView;

            mInit = getInitFromEffect();
            mApplied = getFromEffect();
            mTemporary = Arrays.copyOf(mApplied, mApplied.length);

            sliderView.setOnSliderChangeListener(this);
            mSliderView.setValueRange(minSliderView, maxSliderView);
        }

        protected abstract int[] getFromEffect();

        protected abstract int[] getInitFromEffect();

        protected void setToEffect(Tone tone, int value) {
            mTemporary[tone.toInt()] = value;
        }

        void setTone(Tone tone) {
            Preconditions.checkNotNull(tone);

            mTone = tone;

            mTextView.setText(String.valueOf(mTemporary[tone.toInt()]));
            mSliderView.setValue(mTemporary[tone.toInt()]);
        }

        @Override
        public void apply() {
            mApplied = Arrays.copyOf(mTemporary, mTemporary.length);
        }

        @Override
        public void cancel() {
            setToEffect(Tone.SHADOWS, mApplied[Tone.SHADOWS.toInt()]);
            setToEffect(Tone.MIDTONES, mApplied[Tone.MIDTONES.toInt()]);
            setToEffect(Tone.HIGHLIGHTS, mApplied[Tone.HIGHLIGHTS.toInt()]);
            mSliderView.setValue(mApplied[mTone.toInt()]);
        }

        @Override
        public void reset() {
            setToEffect(Tone.SHADOWS, mInit[Tone.SHADOWS.toInt()]);
            setToEffect(Tone.MIDTONES, mInit[Tone.MIDTONES.toInt()]);
            setToEffect(Tone.HIGHLIGHTS, mInit[Tone.HIGHLIGHTS.toInt()]);
            mSliderView.setValue(mInit[mTone.toInt()]);
        }

        @Override
        public void onStartTouch() {
            if (mOnAdjustListener != null) {
                mOnAdjustListener.onAdjustStart();
            }
        }

        @Override
        public void onStopTouch() {
            if (mOnAdjustListener != null) {
                mOnAdjustListener.onAdjustStop();
            }
        }

        @Override
        public void onSliderChange(int value, boolean fromUser) {
            setToEffect(mTone, value);
            mTextView.setText(String.valueOf(value));

            if (mOnAdjustListener != null) {
                mOnAdjustListener.onAdjustChange();
            }
        }
    }
}
