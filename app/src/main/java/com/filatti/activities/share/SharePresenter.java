package com.filatti.activities.share;

import android.content.Intent;

import com.filatti.activities.effect.EffectActivity;
import com.filatti.activities.mvp.AbstractPresenter;
import com.filatti.effects.EffectManager;

class SharePresenter extends AbstractPresenter<ShareActivity> {
    SharePresenter(ShareActivity activity) {
        super(activity);
    }

    @Override
    protected  ShareActivity getActivity() {
        return super.getActivity();
    }

    @Override
    protected void onCreate() {
    }

    @Override
    protected void onResume() {
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
        Intent intent = new Intent(mActivity, EffectActivity.class);
        mActivity.startActivity(intent);
        mActivity.finish();
    }

    void onFinishClick() {
        EffectManager.destroyInstance();
        mActivity.finish();
    }
}
