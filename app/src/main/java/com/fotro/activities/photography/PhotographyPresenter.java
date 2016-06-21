package com.fotro.activities.photography;

import android.content.Intent;

import com.fotro.PhotoManager;
import com.fotro.activities.editor.EditorActivity;

class PhotographyPresenter {
    private final PhotographyActivity mActivity;

    PhotographyPresenter(PhotographyActivity activity) {
        mActivity = activity;
    }

    void onClickNext() {
        PhotoManager.getInstance().setPhoto(mActivity.getPhoto());

        Intent intent = new Intent(mActivity, EditorActivity.class);
        mActivity.startActivity(intent);
    }
}
