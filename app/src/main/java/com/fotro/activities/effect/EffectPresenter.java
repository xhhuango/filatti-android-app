package com.fotro.activities.effect;

import android.content.Intent;

import com.fotro.activities.gallery.GalleryActivity;
import com.fotro.activities.share.ShareActivity;
import com.fotro.photo.PhotoManager;
import com.fotro.activities.mvp.AbstractPresenter;

class EffectPresenter extends AbstractPresenter<EffectActivity> {
    EffectPresenter(EffectActivity activity) {
        super(activity);
    }

    @Override
    protected void onCreate() {
    }

    @Override
    protected void onResume() {
        getActivity().setPhoto(PhotoManager.getInstance().getPhoto());
    }

    @Override
    protected void onPause() {
    }

    @Override
    protected void onDestroy() {
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
        // TODO: switch the grid view to list adjustments
    }
}
