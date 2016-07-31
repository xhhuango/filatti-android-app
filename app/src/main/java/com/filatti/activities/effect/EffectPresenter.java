package com.filatti.activities.effect;

import android.content.Intent;
import android.graphics.Bitmap;

import com.filatti.activities.effect.items.adjusts.BrightnessAdjustItem;
import com.filatti.activities.effect.items.EffectItem;
import com.filatti.activities.effect.items.adjusts.ContrastAdjustItem;
import com.filatti.activities.effect.items.adjusts.SaturationAdjustItem;
import com.filatti.activities.effect.items.adjusts.SharpnessAdjustItem;
import com.filatti.activities.effect.items.adjusts.TemperatureAdjustItem;
import com.filatti.activities.effect.items.adjusts.VignetteAdjustItem;
import com.filatti.activities.gallery.GalleryActivity;
import com.filatti.activities.share.ShareActivity;
import com.filatti.effects.Effect;
import com.filatti.effects.adjusts.BrightnessAdjust;
import com.filatti.effects.adjusts.ContrastAdjust;
import com.filatti.effects.adjusts.SaturationAdjust;
import com.filatti.effects.adjusts.SharpnessAdjust;
import com.filatti.effects.adjusts.TemperatureAdjust;
import com.filatti.effects.adjusts.VignetteAdjust;
import com.filatti.photo.PhotoManager;
import com.filatti.activities.mvp.AbstractPresenter;
import com.filatti.utils.Millis;
import com.google.common.base.Preconditions;

import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import timber.log.Timber;

class EffectPresenter extends AbstractPresenter<EffectActivity> {
    private Bitmap mPhoto;
    private List<Effect> mAdjustList;
    private List<EffectItem> mAdjustItemList;
    private EffectItem mSelectedEffectItem;

    private Bitmap mAppliedBitmap;

    private AtomicBoolean mLock = new AtomicBoolean(false);

    EffectPresenter(EffectActivity activity) {
        super(activity);
        mPhoto = PhotoManager.getInstance().getPhoto();
    }

    @Override
    protected EffectActivity getActivity() {
        return super.getActivity();
    }

    @Override
    protected void onCreate() {
    }

    @Override
    protected void onResume() {
        mActivity.setPhoto(mPhoto);
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
        PhotoManager.getInstance().clear();

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

    private synchronized List<EffectItem> getAdjustItemList() {
        if (mAdjustList == null || mAdjustItemList == null) {
            mAdjustList = new ArrayList<>();
            mAdjustItemList = new ArrayList<>();

            EffectItem.OnEffectChangeListener listener = new EffectItem.OnEffectChangeListener() {
                @Override
                public void onEffectChanged() {
                    onChangeEffect();
                }
            };

            BrightnessAdjust brightnessAdjust = new BrightnessAdjust();
            mAdjustList.add(brightnessAdjust);
            mAdjustItemList.add(new BrightnessAdjustItem(brightnessAdjust, listener));

            ContrastAdjust contrastAdjust = new ContrastAdjust();
            mAdjustList.add(contrastAdjust);
            mAdjustItemList.add(new ContrastAdjustItem(contrastAdjust, listener));

            SaturationAdjust saturationAdjust = new SaturationAdjust();
            mAdjustList.add(saturationAdjust);
            mAdjustItemList.add(new SaturationAdjustItem(saturationAdjust, listener));

            TemperatureAdjust temperatureAdjust = new TemperatureAdjust();
            mAdjustList.add(temperatureAdjust);
            mAdjustItemList.add(new TemperatureAdjustItem(temperatureAdjust, listener));

            SharpnessAdjust sharpnessAdjust = new SharpnessAdjust();
            mAdjustList.add(sharpnessAdjust);
            mAdjustItemList.add(new SharpnessAdjustItem(sharpnessAdjust, listener));

            VignetteAdjust vignetteAdjust = new VignetteAdjust();
            mAdjustList.add(vignetteAdjust);
            mAdjustItemList.add(new VignetteAdjustItem(vignetteAdjust, listener));
        }
        return mAdjustItemList;
    }

    void onApplyEffectItem() {
        Preconditions.checkState(mSelectedEffectItem != null);

        mSelectedEffectItem.apply();
        mSelectedEffectItem = null;
        mActivity.dismissEffectSettingView();
    }

    void onCancelEffectItem() {
        Preconditions.checkState(mSelectedEffectItem != null);

        mSelectedEffectItem.cancel();
        mSelectedEffectItem = null;
        mActivity.dismissEffectSettingView();
    }

    void onResetEffectItem() {
        Preconditions.checkState(mSelectedEffectItem != null);

        mSelectedEffectItem.reset();
    }

    void onSelectEffectItem(EffectItem effectItem) {
        Preconditions.checkNotNull(effectItem);
        Preconditions.checkState(mSelectedEffectItem == null);

        mSelectedEffectItem = effectItem;
        mActivity.showEffectSettingView(
                effectItem.getView(mActivity, mActivity.getEffectSettingViewContainer()),
                mActivity.getString(effectItem.getDisplayName()));
    }

    private void onChangeEffect() {
        applyEffects();
    }

    private void applyEffects() {
        if (mLock.compareAndSet(false, true)) {
            Mat src = new Mat();
            Utils.bitmapToMat(mPhoto, src);
            if (src.channels() == 4) {
                Mat tmp = new Mat();
                Imgproc.cvtColor(src, tmp, Imgproc.COLOR_RGBA2BGR);
                src.release();
                src = tmp;
            }

            for (Effect effect : mAdjustList) {
                long before = Millis.now();
                Mat dst = effect.apply(src);
                long after = Millis.now();
                Timber.d(effect.getClass().getSimpleName() + " spent " + (after - before) + " ms");
                if (src != dst)
                    src.release();
                src = dst;
            }

            if (mAppliedBitmap == null)
                mAppliedBitmap =
                        Bitmap.createBitmap(src.cols(), src.rows(), Bitmap.Config.ARGB_8888);
            Imgproc.cvtColor(src, src, Imgproc.COLOR_BGR2RGB);
            Utils.matToBitmap(src, mAppliedBitmap);
            src.release();

            mActivity.setPhoto(mAppliedBitmap);

            mLock.set(false);
        }
    }
}
