package com.fotro.activities.gallery;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.provider.MediaStore;

import com.fotro.activities.editor.EditorActivity;
import com.fotro.activities.mvp.AbstractPresenter;
import com.fotro.photo.AspectRatio;
import com.fotro.photo.PhotoManager;

import java.util.ArrayList;
import java.util.List;

class GalleryPresenter extends AbstractPresenter<GalleryActivity> {
    private List<Long> mPhotoList;

    private AspectRatio mAspectRatio = AspectRatio.RATIO_OF_1_TO_1;

    GalleryPresenter(GalleryActivity activity) {
        super(activity);
    }

    @Override
    protected void onCreate() {
        mActivity.setAspectRatio(mAspectRatio);
    }

    @Override
    protected void onResume() {
        mActivity.setPhotoList(getPhotoList());
    }

    private List<Long> getPhotoList() {
        if (mPhotoList != null) {
            return mPhotoList;
        }

        String[] projection = new String[]{MediaStore.Images.Media._ID};
        ContentResolver contentResolver = getActivity().getContentResolver();
        Cursor cursor = contentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                              projection,
                                              "",
                                              null,
                                              MediaStore.Images.Media.DATE_ADDED);

        mPhotoList = new ArrayList<>();
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int idColumn = cursor.getColumnIndex(projection[0]);
                do {
                    Long photoId = cursor.getLong(idColumn);
                    mPhotoList.add(0, photoId);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        return mPhotoList;
    }

    void onBackClick() {
        PhotoManager.getInstance().clear();
        mActivity.finish();
    }

    void onNextClick() {
        PhotoManager.getInstance().setPhoto(mActivity.crop(), mAspectRatio);

        Intent intent = new Intent(mActivity, EditorActivity.class);
        mActivity.startActivity(intent);
    }

    void onAspectRatioClick() {
        mAspectRatio =
                (mAspectRatio == AspectRatio.RATIO_OF_16_TO_9)
                        ? AspectRatio.RATIO_OF_1_TO_1
                        : AspectRatio.RATIO_OF_16_TO_9;
        mActivity.setAspectRatio(mAspectRatio);
    }

    void onCameraClick() {

    }
}
