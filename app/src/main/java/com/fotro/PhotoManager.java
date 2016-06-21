package com.fotro;

import android.graphics.Bitmap;

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
        mBitmap = bitmap;
    }

    public Bitmap getPhoto() {
        return mBitmap;
    }
}
