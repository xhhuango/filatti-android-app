package com.fotro.activities.editor;

import com.fotro.photo.PhotoManager;
import com.fotro.activities.mvp.AbstractPresenter;

class EditorPresenter extends AbstractPresenter<EditorActivity> {
    EditorPresenter(EditorActivity activity) {
        super(activity);
    }

    @Override
    protected void onResume() {
        getActivity().setPhoto(PhotoManager.getInstance().getPhoto());
    }
}
