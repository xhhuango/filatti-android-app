package com.fotro.activities.effect;

import android.content.Intent;
import android.graphics.Bitmap;

import com.fotro.activities.effect.adjusts.BrightnessAdjustItem;
import com.fotro.activities.effect.adjusts.ContrastAdjustItem;
import com.fotro.activities.gallery.GalleryActivity;
import com.fotro.activities.share.ShareActivity;
import com.fotro.effects.Effect;
import com.fotro.effects.adjusts.ContrastBrightnessAdjust;
import com.fotro.logger.Logger;
import com.fotro.photo.PhotoManager;
import com.fotro.activities.mvp.AbstractPresenter;

import java.util.ArrayList;
import java.util.List;

class EffectPresenter extends AbstractPresenter<EffectActivity> {
    private static final String TAG = EffectPresenter.class.getSimpleName();

    private List<Effect> mAdjustList;
    private List<EffectItem> mAdjustItemList;
    private Bitmap mPhoto;

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

            ContrastBrightnessAdjust contrastBrightnessAdjust = new ContrastBrightnessAdjust();
            mAdjustList.add(contrastBrightnessAdjust);
            mAdjustItemList.add(new BrightnessAdjustItem(contrastBrightnessAdjust, listener));
            mAdjustItemList.add(new ContrastAdjustItem(contrastBrightnessAdjust, listener));
        }
        return mAdjustItemList;
    }

    void onCancelEffectItem() {
        // TODO
    }

    void onApplyEffectItem() {
        // TODO
    }

    private void onChangeEffect() {
        Logger.debug(TAG, "Changed");
    }

    void onSelectEffectItem(EffectItem effectItem) {
        mActivity.showAdjustView(effectItem.getView(mActivity),
                                 mActivity.getString(effectItem.getDisplayName()));
    }
}
