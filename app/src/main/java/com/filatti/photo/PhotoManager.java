package com.filatti.photo;

import android.graphics.Bitmap;
import android.os.Environment;

import com.filatti.utils.BitmapUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public final class PhotoManager {
    private static final String PICTURE_DIRECTORY = "Filatti";
    private static final String PICTURE_PREFIX = "IMG_";
    private static final String PICTURE_EXTENSION = ".jpg";

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

    public File createPictureFile() {
        File publicPictureDirectory =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File pictureDirectory = new File(publicPictureDirectory, PICTURE_DIRECTORY);

        if (!pictureDirectory.exists()) {
            if (!pictureDirectory.mkdirs()) {
                return null;
            }
        }

        String timestamp =
                new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH).format(new Date());
        return new File(pictureDirectory.getPath() + File.separator
                                + PICTURE_PREFIX + timestamp + PICTURE_EXTENSION);
    }
}
