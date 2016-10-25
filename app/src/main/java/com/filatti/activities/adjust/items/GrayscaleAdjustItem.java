package com.filatti.activities.adjust.items;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.filatti.R;
import com.filatti.activities.adjust.AdjustAction;
import com.filatti.effects.EffectException;
import com.filatti.effects.adjusts.GrayscaleAdjust;
import com.google.common.base.Preconditions;

import timber.log.Timber;

public class GrayscaleAdjustItem extends AdjustItem<GrayscaleAdjust> {
    private ButtonAdjustAction mButtonAdjustAction;

    public GrayscaleAdjustItem(GrayscaleAdjust effect) {
        super(effect);
    }

    @Override
    public int getDisplayName() {
        return R.string.grayscale;
    }

    @Override
    public int getIcon() {
        return R.drawable.grayscale;
    }

    @Override
    public void apply() {
        mButtonAdjustAction.apply();
    }

    @Override
    public void cancel() {
        mButtonAdjustAction.cancel();
        if (mOnAdjustListener != null) {
            mOnAdjustListener.onAdjustChange();
        }
    }

    @Override
    public void reset() {
        mButtonAdjustAction.reset();
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
                (ViewGroup) inflater.inflate(R.layout.grayscale_item_view, rootView, false);

        mButtonAdjustAction = new ButtonAdjustAction();

        initButtons(viewGroup);
        initPencilSketchButtons(viewGroup);

        return viewGroup;
    }

    private void initButtons(ViewGroup viewGroup) {
        ImageButton noneButton = (ImageButton) viewGroup.findViewById(R.id.noneButton);
        noneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mButtonAdjustAction.setToEffect(GrayscaleAdjust.Mode.NONE);
            }
        });

        ImageButton grayButton = (ImageButton) viewGroup.findViewById(R.id.grayButton);
        grayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mButtonAdjustAction.setToEffect(GrayscaleAdjust.Mode.GRAY);
            }
        });

        ImageButton blackAndWhiteButton =
                (ImageButton) viewGroup.findViewById(R.id.blackAndWhiteButton);
        blackAndWhiteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mButtonAdjustAction.setToEffect(GrayscaleAdjust.Mode.BLACK_AND_WHITE);
            }
        });
    }

    private void initPencilSketchButtons(ViewGroup viewGroup) {
        ImageButton button1 = (ImageButton) viewGroup.findViewById(R.id.pencilSketch1Button);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mButtonAdjustAction.setToEffect(GrayscaleAdjust.Mode.PENCIL_SKETCH, 1);
            }
        });

        ImageButton button2 = (ImageButton) viewGroup.findViewById(R.id.pencilSketch2Button);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mButtonAdjustAction.setToEffect(GrayscaleAdjust.Mode.PENCIL_SKETCH, 2);
            }
        });

        ImageButton button3 = (ImageButton) viewGroup.findViewById(R.id.pencilSketch3Button);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mButtonAdjustAction.setToEffect(GrayscaleAdjust.Mode.PENCIL_SKETCH, 3);
            }
        });

        ImageButton button4 = (ImageButton) viewGroup.findViewById(R.id.pencilSketch4Button);
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mButtonAdjustAction.setToEffect(GrayscaleAdjust.Mode.PENCIL_SKETCH, 5);
            }
        });

        ImageButton button5 = (ImageButton) viewGroup.findViewById(R.id.pencilSketch5Button);
        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mButtonAdjustAction.setToEffect(GrayscaleAdjust.Mode.PENCIL_SKETCH, 8);
            }
        });

        ImageButton button6 = (ImageButton) viewGroup.findViewById(R.id.pencilSketch6Button);
        button6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mButtonAdjustAction.setToEffect(GrayscaleAdjust.Mode.PENCIL_SKETCH, 13);
            }
        });

        ImageButton button7 = (ImageButton) viewGroup.findViewById(R.id.pencilSketch7Button);
        button7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mButtonAdjustAction.setToEffect(GrayscaleAdjust.Mode.PENCIL_SKETCH, 21);
            }
        });
    }

    private class ButtonAdjustAction implements AdjustAction {
        private final GrayscaleAdjust.Mode mInitMode;
        private GrayscaleAdjust.Mode mAppliedMode;
        private GrayscaleAdjust.Mode mTemporaryMode;

        private final int mInitBlurKernelSize;
        private int mAppliedBlurKernelSize;
        private int mTemporaryBlurKernelSize;

        private ButtonAdjustAction() {
            mInitMode = mEffect.getInitMode();
            mAppliedMode = mEffect.getMode();
            mTemporaryMode = mAppliedMode;

            mInitBlurKernelSize = mEffect.getInitBlurKernelSize();
            mAppliedBlurKernelSize = mEffect.getBlurKernelSize();
            mTemporaryBlurKernelSize = mAppliedBlurKernelSize;
        }

        private void setToEffect(GrayscaleAdjust.Mode mode) {
            Preconditions.checkArgument(mode != GrayscaleAdjust.Mode.PENCIL_SKETCH);
            setToEffect(mode, mInitBlurKernelSize);
        }

        private void setToEffect(GrayscaleAdjust.Mode mode, int blurKernelSize) {
            if (mode != GrayscaleAdjust.Mode.PENCIL_SKETCH) {
                blurKernelSize = mInitBlurKernelSize;
            }

            mTemporaryMode = mode;
            mEffect.setMode(mode);

            mTemporaryBlurKernelSize = blurKernelSize;
            try {
                mEffect.setBlurKernelSize(blurKernelSize);
            } catch (EffectException e) {
                Timber.e(e, "Failed to set blur kernel size %d", blurKernelSize);
            }

            if (mOnAdjustListener != null) {
                mOnAdjustListener.onAdjustChange();
            }
        }

        @Override
        public void apply() {
            mAppliedMode = mTemporaryMode;
            mAppliedBlurKernelSize = mTemporaryBlurKernelSize;
        }

        @Override
        public void cancel() {
            setToEffect(mAppliedMode, mAppliedBlurKernelSize);
        }

        @Override
        public void reset() {
            setToEffect(mInitMode, mInitBlurKernelSize);
        }
    }
}
