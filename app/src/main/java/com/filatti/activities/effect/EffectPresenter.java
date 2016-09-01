package com.filatti.activities.effect;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Debug;

import com.filatti.activities.effect.items.EffectItem;
import com.filatti.activities.effect.items.adjusts.ContrastAdjustItem;
import com.filatti.activities.effect.items.adjusts.CurvesAdjustItem;
import com.filatti.activities.gallery.GalleryActivity;
import com.filatti.activities.share.ShareActivity;
import com.filatti.effects.Effect;
import com.filatti.effects.EffectManager;
import com.filatti.effects.adjusts.ContrastAdjust;
import com.filatti.effects.adjusts.CurvesAdjust;
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
    private EffectManager mEffectManager = new EffectManager();
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
        if (mAdjustItemList == null) {
            mAdjustItemList = new ArrayList<>();

            EffectItem.OnEffectChangeListener listener = new EffectItem.OnEffectChangeListener() {
                @Override
                public void onEffectChanged() {
                    onChangeEffect();
                }
            };

            CurvesAdjustItem curvesAdjustItem =
                    new CurvesAdjustItem(mEffectManager.getEffect(CurvesAdjust.class), listener);
            mAdjustItemList.add(curvesAdjustItem);

            ContrastAdjustItem contrastAdjustItem =
                    new ContrastAdjustItem(mEffectManager.getEffect(ContrastAdjust.class), listener);
            mAdjustItemList.add(contrastAdjustItem);

//            mAdjustItemList.add(new HlsAdjustItem(hlsAdjust, listener));
//            mAdjustItemList.add(new TemperatureAdjustItem(temperatureAdjust, listener));
//            mAdjustItemList.add(new SharpnessAdjustItem(sharpnessAdjust, listener));
//            mAdjustItemList.add(new VignetteAdjustItem(vignetteAdjust, listener));
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
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
                    Mat src = new Mat();
                    Utils.bitmapToMat(mPhoto, src);
                    if (src.channels() == 4) {
                        Mat tmp = new Mat();
                        Imgproc.cvtColor(src, tmp, Imgproc.COLOR_RGBA2BGR);
                        src.release();
                        src = tmp;
                    }

                    for (Effect effect : mEffectManager.list()) {
                        long before = Millis.now();
                        Mat dst = effect.apply(src);
                        long after = Millis.now();
                        Timber.d("Spent %d ms, native heap=%d KB",
                                 after - before,
                                 Debug.getNativeHeapAllocatedSize() / 1000);
                        Timber.d(effect.toString());
                        if (src != dst) {
                            src.release();
                            src = dst;
                        }
                    }

                    if (mAppliedBitmap == null) {
                        mAppliedBitmap = Bitmap.createBitmap(src.cols(),
                                                             src.rows(),
                                                             Bitmap.Config.ARGB_8888);
                    }
                    Imgproc.cvtColor(src, src, Imgproc.COLOR_BGR2RGB);
                    Utils.matToBitmap(src, mAppliedBitmap);
                    src.release();

                    return null;
                }

                @Override
                protected void onPostExecute(Void result) {
                    mActivity.setPhoto(mAppliedBitmap);
                    mLock.set(false);
                }
            }.execute();
        } else {
            Timber.d("Skip");
        }
    }
}
