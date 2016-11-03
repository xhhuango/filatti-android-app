package com.filatti.activities.adjust;

import android.graphics.Bitmap;
import android.support.annotation.StringRes;
import android.view.View;
import android.view.ViewGroup;

import com.filatti.activities.adjust.items.AdjustItem;
import com.filatti.activities.mvp.AbstractPresenter;
import com.filatti.effects.EffectManager;
import com.filatti.utilities.photo.BitmapUtils;

import org.opencv.core.Mat;

import timber.log.Timber;

class AdjustPresenter extends AbstractPresenter<AdjustActivity> {
    private final AdjustItem mAdjustItem;
    private Mat mMat;
    private Bitmap mBitmap;

    AdjustPresenter(AdjustActivity activity) {
        super(activity);

        mAdjustItem = EffectManager.getInstance().getSelectedAdjustItem();
    }

    @Override
    protected void onCreate() {
    }

    @Override
    protected void onResume() {
        mMat = EffectManager.getInstance().getMatAppliedUtilSelectedAdjustItem();

        mBitmap = BitmapUtils.createBitmap(mMat.cols(), mMat.rows());
        BitmapUtils.convertMatToBitmap(mMat, mBitmap);

        if (mAdjustItem.doesApplyUponAdjusting()) {
            applyPhoto();
        }

        showPhoto(EffectManager.getInstance().getAppliedBitmap());

        mAdjustItem.setOnAdjustListener(new AdjustItem.OnAdjustListener() {
            @Override
            public void onAdjustStart() {
                Timber.d("onAdjustStart()");
                showPhoto(mBitmap);
            }

            @Override
            public void onAdjustStop() {
                Timber.d("onAdjustStop()");
                showPhoto(EffectManager.getInstance().applyBitmap());
            }

            @Override
            public void onAdjustChange() {
                Timber.d("onAdjustChange()");
                applyPhoto();
                showPhoto(mBitmap);
            }
        });
    }

    @Override
    protected void onPause() {
        mAdjustItem.setOnAdjustListener(null);

        if (!mBitmap.isRecycled()) {
            mBitmap.recycle();
        }

        if (!mMat.empty()) {
            mMat.release();
        }
    }

    @Override
    protected void onDestroy() {
    }

    @Override
    protected void onBackPressed() {
    }

    @StringRes
    int getAdjustName() {
        return mAdjustItem.getDisplayName();
    }

    View getAdjustOverlyView(ViewGroup viewGroup) {
        return mAdjustItem.getOverlayView(mActivity, viewGroup);
    }

    View getAdjustView(ViewGroup viewGroup) {
        return mAdjustItem.getView(mActivity, viewGroup);
    }

    void applyPhoto() {
        Mat appliedMat = mAdjustItem.getEffect().apply(mMat);
        BitmapUtils.convertMatToBitmap(appliedMat, mBitmap);
        if (appliedMat != mMat) {
            appliedMat.release();
        }
    }

    void showPhoto(Bitmap bitmap) {
        mActivity.setPhoto(bitmap);
    }

    void onApplyEffectItem() {
        mAdjustItem.apply();
        mActivity.finish();
    }

    void onCancelEffectItem() {
        mAdjustItem.cancel();
        mActivity.finish();
    }

    void onResetEffectItem() {
        mAdjustItem.reset();
        showPhoto(EffectManager.getInstance().applyBitmap());
    }
}
