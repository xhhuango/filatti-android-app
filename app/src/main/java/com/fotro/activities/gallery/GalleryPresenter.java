package com.fotro.activities.gallery;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.fotro.activities.editor.EditorActivity;
import com.fotro.activities.mvp.AbstractPresenter;
import com.fotro.photo.AspectRatio;
import com.fotro.photo.PhotoManager;
import com.fotro.utils.ThreadPool;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

class GalleryPresenter extends AbstractPresenter<GalleryActivity> {
    private List<Long> mPhotoList;

    private File mPhotographyFile;
    private Uri mPhotographyFileUri;

    GalleryPresenter(GalleryActivity activity) {
        super(activity);
    }

    @Override
    protected void onCreate() {
    }

    @Override
    protected void onResume() {
        if (mPhotographyFileUri == null) {
            ThreadPool.run(new Runnable() {
                @Override
                public void run() {
                    mActivity.setPhotoList(getPhotoList());
                    mActivity.display(mPhotoList.get(0));
                }
            });
        }
    }

    @Override
    protected void onPause() {
    }

    @Override
    protected void onDestroy() {
        deletePhotographyFileIfExists();
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

    private void deletePhotographyFileIfExists() {
        if (mPhotographyFileUri != null) {
            if (mPhotographyFile.exists()) {
                //noinspection ResultOfMethodCallIgnored
                mPhotographyFile.delete();
            }
        }
    }

    void onBackClick() {
        PhotoManager.getInstance().clear();
        mActivity.finish();
    }

    void onNextClick() {
        PhotoManager.getInstance().setPhoto(mActivity.crop(), mActivity.getAspectRatio());

        Intent intent = new Intent(mActivity, EditorActivity.class);
        mActivity.startActivity(intent);
        mActivity.finish();
    }

    void onAspectRatioClick() {
        mActivity.setAspectRatio((mActivity.getAspectRatio() == AspectRatio.RATIO_OF_16_TO_9)
                                         ? AspectRatio.RATIO_OF_1_TO_1
                                         : AspectRatio.RATIO_OF_16_TO_9);
    }

    void onCameraClick() {
        deletePhotographyFileIfExists();

        mPhotographyFile = PhotoManager.getInstance().createPictureFile();
        mPhotographyFileUri = Uri.fromFile(mPhotographyFile);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mPhotographyFileUri);
        mActivity.startActivityForResult(intent, GalleryActivity.IMAGE_CAPTURE_REQUEST_CODE);
    }

    void onPictureTaken(boolean hasTaken) {
        if (hasTaken) {
            mActivity.display(mPhotographyFileUri);
        } else {
            mPhotographyFileUri = null;
            mPhotographyFile = null;
        }
    }
}
