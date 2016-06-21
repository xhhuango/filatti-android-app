package com.fotro;

import android.graphics.Bitmap;

import com.fotro.utils.BitmapUtils;

public final class PhotoManager {
    private static PhotoManager sPhotoManager;

    public static synchronized PhotoManager getInstance() {
        if (sPhotoManager == null) {
            sPhotoManager = new PhotoManager();
        }
        return sPhotoManager;
    }

    private Bitmap mBitmap;

    private PhotoManager() {
    }

    public void setPhoto(Bitmap bitmap) {
        if (mBitmap != null) {
            mBitmap.recycle();
        }
        mBitmap = BitmapUtils.resizeBitmap(bitmap, 1080, 1080);
    }

    public Bitmap getPhoto() {
        return mBitmap;
    }
}
