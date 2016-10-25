package com.filatti.activities.effect;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;

import com.filatti.activities.adjust.AdjustActivity;
import com.filatti.activities.adjust.items.AdjustItem;
import com.filatti.activities.adjust.items.ColorBalanceAdjustItem;
import com.filatti.activities.adjust.items.ContrastAdjustItem;
import com.filatti.activities.adjust.items.CurvesAdjustItem;
import com.filatti.activities.adjust.items.ExposureAdjustItem;
import com.filatti.activities.adjust.items.GrayscaleAdjustItem;
import com.filatti.activities.adjust.items.HighlightShadowAdjustItem;
import com.filatti.activities.adjust.items.HlsAdjustItem;
import com.filatti.activities.adjust.items.SharpnessAdjustItem;
import com.filatti.activities.adjust.items.TemperatureAdjustItem;
import com.filatti.activities.adjust.items.TiltShiftAdjustItem;
import com.filatti.activities.adjust.items.VibranceAdjustItem;
import com.filatti.activities.adjust.items.VignetteAdjustItem;
import com.filatti.activities.adjust.items.WhiteBalanceAdjustItem;
import com.filatti.activities.gallery.GalleryActivity;
import com.filatti.activities.share.ShareActivity;
import com.filatti.effects.AdjustComposite;
import com.filatti.effects.adjusts.ColorBalanceAdjust;
import com.filatti.effects.adjusts.ContrastAdjust;
import com.filatti.effects.adjusts.CurvesAdjust;
import com.filatti.effects.adjusts.ExposureAdjust;
import com.filatti.effects.adjusts.GrayscaleAdjust;
import com.filatti.effects.adjusts.HighlightShadowAdjust;
import com.filatti.effects.adjusts.HlsAdjust;
import com.filatti.effects.adjusts.SharpnessAdjust;
import com.filatti.effects.adjusts.TemperatureAdjust;
import com.filatti.effects.adjusts.TiltShiftAdjust;
import com.filatti.effects.adjusts.VibranceAdjust;
import com.filatti.effects.adjusts.VignetteAdjust;
import com.filatti.effects.adjusts.WhiteBalanceAdjust;
import com.filatti.managers.EffectManager;
import com.filatti.activities.mvp.AbstractPresenter;
import com.google.common.base.Preconditions;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import timber.log.Timber;

class EffectPresenter extends AbstractPresenter<EffectActivity> {
    private AdjustComposite mAdjustComposite = new AdjustComposite();
    private List<AdjustItem> mAdjustItemList;

    private AtomicBoolean mLock = new AtomicBoolean(false);

    EffectPresenter(EffectActivity activity) {
        super(activity);
        EffectManager.getInstance().setAdjustComposite(mAdjustComposite);
    }

    @Override
    protected EffectActivity getActivity() {
        return super.getActivity();
    }

    @Override
    protected void onCreate() {
        mActivity.setPhoto(EffectManager.getInstance().getOriginalBitmap());
    }

    @Override
    protected void onResume() {
        applyEffects();
        mActivity.setEffectItemList(getAdjustItemList());
    }

    @Override
    protected void onPause() {
    }

    @Override
    protected void onDestroy() {
    }

    @Override
    protected void onBackPressed() {
        onBackClick();
    }

    void onBackClick() {
        EffectManager.destroyInstance();

        Intent intent = new Intent(mActivity, GalleryActivity.class);
        mActivity.startActivity(intent);
        mActivity.finish();
    }

    void onNextClick() {
        Intent intent = new Intent(mActivity, ShareActivity.class);
        mActivity.startActivity(intent);
        mActivity.finish();
    }

    void onFilterClick() {
        // TODO: switch the grid view to list filters
    }

    void onAdjustClick() {
        mActivity.setEffectItemList(mAdjustItemList);
    }

    private synchronized List<AdjustItem> getAdjustItemList() {
        if (mAdjustItemList == null) {
            mAdjustItemList = new ArrayList<>();

            GrayscaleAdjustItem grayscaleAdjustItem =
                    new GrayscaleAdjustItem(mAdjustComposite.getEffect(GrayscaleAdjust.class));
            mAdjustItemList.add(grayscaleAdjustItem);

            WhiteBalanceAdjustItem whiteBalanceAdjustItem =
                    new WhiteBalanceAdjustItem(mAdjustComposite.getEffect(WhiteBalanceAdjust.class));
            mAdjustItemList.add(whiteBalanceAdjustItem);

            ExposureAdjustItem exposureAdjustItem =
                    new ExposureAdjustItem(mAdjustComposite.getEffect(ExposureAdjust.class));
            mAdjustItemList.add(exposureAdjustItem);

            VibranceAdjustItem vibranceAdjustItem =
                    new VibranceAdjustItem(mAdjustComposite.getEffect(VibranceAdjust.class));
            mAdjustItemList.add(vibranceAdjustItem);

            CurvesAdjustItem curvesAdjustItem =
                    new CurvesAdjustItem(mAdjustComposite.getEffect(CurvesAdjust.class));
            mAdjustItemList.add(curvesAdjustItem);

            ColorBalanceAdjustItem colorBalanceAdjustItem =
                    new ColorBalanceAdjustItem(mAdjustComposite.getEffect(ColorBalanceAdjust.class));
            mAdjustItemList.add(colorBalanceAdjustItem);

            HlsAdjustItem hlsAdjustItem =
                    new HlsAdjustItem(mAdjustComposite.getEffect(HlsAdjust.class));
            mAdjustItemList.add(hlsAdjustItem);

            ContrastAdjustItem contrastAdjustItem =
                    new ContrastAdjustItem(mAdjustComposite.getEffect(ContrastAdjust.class));
            mAdjustItemList.add(contrastAdjustItem);

            HighlightShadowAdjustItem highlightShadowAdjustItem =
                    new HighlightShadowAdjustItem(mAdjustComposite.getEffect(HighlightShadowAdjust.class));
            mAdjustItemList.add(highlightShadowAdjustItem);

            TemperatureAdjustItem temperatureAdjustItem =
                    new TemperatureAdjustItem(mAdjustComposite.getEffect(TemperatureAdjust.class));
            mAdjustItemList.add(temperatureAdjustItem);

            SharpnessAdjustItem sharpnessAdjustItem =
                    new SharpnessAdjustItem(mAdjustComposite.getEffect(SharpnessAdjust.class));
            mAdjustItemList.add(sharpnessAdjustItem);

            VignetteAdjustItem vignetteAdjustItem =
                    new VignetteAdjustItem(mAdjustComposite.getEffect(VignetteAdjust.class));
            mAdjustItemList.add(vignetteAdjustItem);

            TiltShiftAdjustItem tiltShiftAdjustItem =
                    new TiltShiftAdjustItem(mAdjustComposite.getEffect(TiltShiftAdjust.class));
            mAdjustItemList.add(tiltShiftAdjustItem);
        }
        return mAdjustItemList;
    }

    void onSelectAdjustItem(AdjustItem adjustItem) {
        Preconditions.checkNotNull(adjustItem);

        EffectManager.getInstance().setSelectedAdjustItem(adjustItem);

        Intent intent = new Intent(mActivity, AdjustActivity.class);
        mActivity.startActivity(intent);
    }

    private void applyEffects() {
        if (mLock.compareAndSet(false, true)) {
            new AsyncTask<Void, Void, Bitmap>() {
                @Override
                protected Bitmap doInBackground(Void... voids) {
                    return EffectManager.getInstance().applyBitmap();
                }

                @Override
                protected void onPostExecute(Bitmap result) {
                    mActivity.setPhoto(result);
                    mLock.set(false);
                }
            }.execute();
        } else {
            Timber.d("Skip");
        }
    }
}
