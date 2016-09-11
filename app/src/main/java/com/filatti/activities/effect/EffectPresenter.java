package com.filatti.activities.effect;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;

import com.filatti.activities.adjust.AdjustActivity;
import com.filatti.activities.adjust.items.AdjustItem;
import com.filatti.activities.adjust.items.ContrastAdjustItem;
import com.filatti.activities.adjust.items.CurvesAdjustItem;
import com.filatti.activities.adjust.items.HlsAdjustItem;
import com.filatti.activities.adjust.items.TemperatureAdjustItem;
import com.filatti.activities.gallery.GalleryActivity;
import com.filatti.activities.share.ShareActivity;
import com.filatti.effects.AdjustComposite;
import com.filatti.effects.adjusts.ContrastAdjust;
import com.filatti.effects.adjusts.CurvesAdjust;
import com.filatti.effects.adjusts.HlsAdjust;
import com.filatti.effects.adjusts.TemperatureAdjust;
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

            CurvesAdjustItem curvesAdjustItem =
                    new CurvesAdjustItem(mAdjustComposite.getEffect(CurvesAdjust.class));
            mAdjustItemList.add(curvesAdjustItem);

            ContrastAdjustItem contrastAdjustItem =
                    new ContrastAdjustItem(mAdjustComposite.getEffect(ContrastAdjust.class));
            mAdjustItemList.add(contrastAdjustItem);

            TemperatureAdjustItem temperatureAdjustItem =
                    new TemperatureAdjustItem(mAdjustComposite.getEffect(TemperatureAdjust.class));
            mAdjustItemList.add(temperatureAdjustItem);

            HlsAdjustItem hlsAdjustItem =
                    new HlsAdjustItem(mAdjustComposite.getEffect(HlsAdjust.class));
            mAdjustItemList.add(hlsAdjustItem);
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
