package com.fotro.photo;

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

    public void clear() {
        if (mBitmap != null) {
            mBitmap.recycle();
            mBitmap = null;
        }
    }

    public void setPhoto(Bitmap bitmap, AspectRatio aspectRatio) {
        clear();
        mBitmap = BitmapUtils.resizeBitmap(bitmap, aspectRatio.getWidth(), aspectRatio.getHeight());
    }

    public Bitmap getPhoto() {
        return mBitmap;
    }
}
