package com.filatti.activities.effect;

import android.content.Intent;
import android.graphics.Bitmap;

import com.filatti.activities.effect.items.adjusts.BrightnessAdjustItem;
import com.filatti.activities.effect.items.adjusts.ContrastAdjustItem;
import com.filatti.activities.effect.items.EffectItem;
import com.filatti.activities.effect.items.adjusts.SaturationAdjustItem;
import com.filatti.activities.effect.items.adjusts.SharpenAdjustItem;
import com.filatti.activities.gallery.GalleryActivity;
import com.filatti.activities.share.ShareActivity;
import com.filatti.effects.Effect;
import com.filatti.effects.adjusts.ContrastBrightnessAdjust;
import com.filatti.effects.adjusts.SaturationAdjust;
import com.filatti.effects.adjusts.SharpenAdjust;
import com.filatti.logger.Logger;
import com.filatti.photo.PhotoManager;
import com.filatti.activities.mvp.AbstractPresenter;
import com.google.common.base.Preconditions;

import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

class EffectPresenter extends AbstractPresenter<EffectActivity> {
    private static final String TAG = EffectPresenter.class.getSimpleName();

    private Bitmap mPhoto;
    private List<Effect> mAdjustList;
    private List<EffectItem> mAdjustItemList;
    private EffectItem mSelectedEffectItem;

    private Bitmap mAppliedBitmap;

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

            SharpenAdjust sharpenAdjust = new SharpenAdjust();
            mAdjustList.add(sharpenAdjust);
            mAdjustItemList.add(new SharpenAdjustItem(sharpenAdjust, listener));

            ContrastBrightnessAdjust contrastBrightnessAdjust = new ContrastBrightnessAdjust();
            mAdjustList.add(contrastBrightnessAdjust);
            mAdjustItemList.add(new BrightnessAdjustItem(contrastBrightnessAdjust, listener));
            mAdjustItemList.add(new ContrastAdjustItem(contrastBrightnessAdjust, listener));

            SaturationAdjust saturationAdjust = new SaturationAdjust();
            mAdjustList.add(saturationAdjust);
            mAdjustItemList.add(new SaturationAdjustItem(saturationAdjust, listener));
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
        mActivity.showEffectSettingView(effectItem.getView(mActivity),
                                        mActivity.getString(effectItem.getDisplayName()));
    }

    private void onChangeEffect() {
        Logger.debug(TAG, "Changed");
        applyEffects();
    }

    private void applyEffects() {
        Mat src = new Mat();
        Utils.bitmapToMat(mPhoto, src);

        if (src.channels() == 4) {
            Mat tmp = new Mat();
            Imgproc.cvtColor(src, tmp, Imgproc.COLOR_RGBA2RGB);
            src.release();
            src = tmp;
        }

        for (Effect effect : mAdjustList) {
            Mat dst = effect.apply(src);
            if (src != dst) {
                src.release();
            }
            src = dst;
        }

        if (mAppliedBitmap == null)
            mAppliedBitmap = Bitmap.createBitmap(src.cols(), src.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(src, mAppliedBitmap);
        mActivity.setPhoto(mAppliedBitmap);
    }
}
