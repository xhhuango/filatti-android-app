package com.fotro.activities.effect;

import android.content.Intent;

import com.fotro.activities.effect.adjusts.AdjustItem;
import com.fotro.activities.effect.adjusts.BrightnessAdjustItem;
import com.fotro.activities.effect.adjusts.ContrastAdjustItem;
import com.fotro.activities.gallery.GalleryActivity;
import com.fotro.activities.share.ShareActivity;
import com.fotro.effects.Effect;
import com.fotro.effects.adjusts.ContrastBrightnessAdjust;
import com.fotro.photo.PhotoManager;
import com.fotro.activities.mvp.AbstractPresenter;

import java.util.ArrayList;
import java.util.List;

class EffectPresenter extends AbstractPresenter<EffectActivity> {
    private List<EffectItem> mAdjustItemList;

    EffectPresenter(EffectActivity activity) {
        super(activity);
    }

    @Override
    protected void onCreate() {
    }

    @Override
    protected void onResume() {
        mActivity.setPhoto(PhotoManager.getInstance().getPhoto());
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

            ContrastBrightnessAdjust contrastBrightnessAdjust = new ContrastBrightnessAdjust();
            mAdjustItemList.add(new BrightnessAdjustItem(contrastBrightnessAdjust));
            mAdjustItemList.add(new ContrastAdjustItem(contrastBrightnessAdjust));
        }
        return mAdjustItemList;
    }
}
