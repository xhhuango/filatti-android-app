package com.filatti.activities.adjust.items;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.filatti.R;
import com.filatti.activities.adjust.ui.OnAffectListener;
import com.filatti.activities.adjust.ui.ValueBarView;
import com.filatti.effects.EffectException;
import com.filatti.effects.adjusts.HlsAdjust;

import timber.log.Timber;

public class HlsAdjustItem extends AdjustItem<HlsAdjust> {
    private ValueBarView mHueValueBarView;
    private ValueBarView mLightnessValueBarView;
    private ValueBarView mSaturationValueBarView;

    public HlsAdjustItem(HlsAdjust effect) {
        super(effect);
    }

    @Override
    public int getDisplayName() {
        return R.string.hls;
    }

    @Override
    public int getIcon() {
        return R.drawable.hls;
    }

    @Override
    public void apply() {
        mHueValueBarView.apply();
        mLightnessValueBarView.apply();
        mSaturationValueBarView.apply();
    }

    @Override
    public void cancel() {
        mHueValueBarView.cancel();
        mLightnessValueBarView.cancel();
        mSaturationValueBarView.cancel();
        if (mOnAdjustListener != null) {
            mOnAdjustListener.onAdjustChange();
        }
    }

    @Override
    public void reset() {
        mHueValueBarView.reset();
        mLightnessValueBarView.reset();
        mSaturationValueBarView.reset();
        if (mOnAdjustListener != null) {
            mOnAdjustListener.onAdjustChange();
        }
    }

    @Override
    public View getView(Context context, ViewGroup rootView) {
        LayoutInflater inflater = LayoutInflater.from(context);
        ViewGroup viewGroup =
                (ViewGroup) inflater.inflate(R.layout.hls_item_view, rootView, false);

        mHueValueBarView = (ValueBarView) viewGroup.findViewById(R.id.hueValueBar);
        Adapter hueAdapter = createHeuAdapter();
        mHueValueBarView.initialize(true,
                                    R.string.hls_hue,
                                    -180,
                                    180,
                                    hueAdapter.getInitFromEffect(),
                                    hueAdapter.getFromEffect(),
                                    hueAdapter,
                                    hueAdapter);

        mLightnessValueBarView = (ValueBarView) viewGroup.findViewById(R.id.lightnessValueBar);
        Adapter lightnessAdapter = createLightnessAdapter();
        mLightnessValueBarView.initialize(true,
                                          R.string.hls_lightness,
                                          -100,
                                          100,
                                          lightnessAdapter.getInitFromEffect(),
                                          lightnessAdapter.getFromEffect(),
                                          lightnessAdapter,
                                          lightnessAdapter);

        mSaturationValueBarView = (ValueBarView) viewGroup.findViewById(R.id.saturationValueBar);
        Adapter saturationAdapter = createSaturationAdapter();
        mSaturationValueBarView.initialize(true,
                                           R.string.hls_saturation,
                                           -100,
                                           100,
                                           saturationAdapter.getInitFromEffect(),
                                           saturationAdapter.getFromEffect(),
                                           saturationAdapter,
                                           saturationAdapter);
        return viewGroup;
    }

    private Adapter createHeuAdapter() {
        return new Adapter() {
            @Override
            public void setToEffect(int value) {
                try {
                    mEffect.setHue(value);
                } catch (EffectException e) {
                    Timber.e(e, "Failed to set hue %d", value);
                }
            }

            @Override
            public int getFromEffect() {
                return mEffect.getHue();
            }

            @Override
            public int getInitFromEffect() {
                return mEffect.getInitHue();
            }
        };
    }

    private Adapter createLightnessAdapter() {
        return new Adapter() {
            @Override
            void setToEffect(int value) {
                double brightness = value / 200.0;
                try {
                    mEffect.setLightness(brightness);
                } catch (EffectException e) {
                    Timber.e(e, "Failed to set brightness %f", brightness);
                }
            }

            private int convertFromEffect(double value) {
                return (int) (value * 200.0);
            }

            @Override
            int getFromEffect() {
                return convertFromEffect(mEffect.getLightness());
            }

            @Override
            int getInitFromEffect() {
                return convertFromEffect(mEffect.getInitLightness());
            }
        };
    }

    private Adapter createSaturationAdapter() {
        return new Adapter() {
            @Override
            void setToEffect(int value) {
                double saturation = value / 100.0;
                try {
                    mEffect.setSaturation(saturation);
                } catch (EffectException e) {
                    Timber.e(e, "Failed to set saturation %f", saturation);
                }
            }

            private int convertFromEffect(double value) {
                return (int) (value * 100.0);
            }

            @Override
            int getFromEffect() {
                return convertFromEffect(mEffect.getSaturation());
            }

            @Override
            int getInitFromEffect() {
                return convertFromEffect(mEffect.getInitSaturation());
            }
        };
    }

    private abstract class Adapter implements OnAffectListener, ValueBarView.OnValueChangeListener {
        abstract void setToEffect(int value);

        abstract int getFromEffect();

        abstract int getInitFromEffect();

        @Override
        public void onStartAffect() {
            if (mOnAdjustListener != null) {
                mOnAdjustListener.onAdjustStart();
            }
        }

        @Override
        public void onStopAffect() {
            if (mOnAdjustListener != null) {
                mOnAdjustListener.onAdjustStop();
            }
        }

        @Override
        public void onValueChanged(int value) {
            setToEffect(value);
            if (mOnAdjustListener != null) {
                mOnAdjustListener.onAdjustChange();
            }
        }
    }
}
