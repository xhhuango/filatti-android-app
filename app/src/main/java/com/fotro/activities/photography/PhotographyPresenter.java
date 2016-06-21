package com.fotro.activities.photography;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.provider.MediaStore;

import com.fotro.PhotoManager;
import com.fotro.activities.editor.EditorActivity;
import com.fotro.activities.mvp.AbstractPresenter;

import java.util.ArrayList;
import java.util.List;

class PhotographyPresenter extends AbstractPresenter<PhotographyActivity> {
    PhotographyPresenter(PhotographyActivity activity) {
        super(activity);
    }

    @Override
    protected void onResume() {
        getActivity().showPhotos(getPhotoList());
    }

    private List<Long> getPhotoList() {
        String[] projection = new String[]{MediaStore.Images.Media._ID};
        ContentResolver contentResolver = getActivity().getContentResolver();
        Cursor cursor = contentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                              projection,
                                              "",
                                              null,
                                              MediaStore.Images.Media.DATE_ADDED);

        List<Long> photoList = new ArrayList<>();
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int idColumn = cursor.getColumnIndex(projection[0]);
                do {
                    Long photoId = cursor.getLong(idColumn);
                    photoList.add(0, photoId);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        return photoList;
    }

    void onClickNext() {
        PhotoManager.getInstance().setPhoto(getActivity().getSelectedPhoto());

        Intent intent = new Intent(getActivity(), EditorActivity.class);
        getActivity().startActivity(intent);
    }
}
