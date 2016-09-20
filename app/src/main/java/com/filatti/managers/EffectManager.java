package com.filatti.managers;

import android.graphics.Bitmap;
import android.os.Environment;

import com.filatti.activities.adjust.items.AdjustItem;
import com.filatti.effects.AdjustComposite;
import com.filatti.utilities.photo.BitmapUtils;
import com.filatti.utilities.photo.AspectRatio;
import com.google.common.base.Preconditions;

import org.opencv.core.Mat;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public final class EffectManager {
    private static final String PICTURE_DIRECTORY = "Filatti";
    private static final String PICTURE_PREFIX = "IMG_";
    private static final String PICTURE_EXTENSION = ".jpg";

    private static EffectManager sEffectManager;

    public static synchronized EffectManager getInstance() {
        if (sEffectManager == null) {
            sEffectManager = new EffectManager();
        }
        return sEffectManager;
    }

    public static synchronized void destroyInstance() {
        if (sEffectManager != null) {
            sEffectManager.clear();
            sEffectManager = null;
        }
    }

    private Bitmap mOriginalBitmap;
    private Bitmap mAppliedBitmap;

    private AdjustComposite mAdjustComposite;
    private AdjustItem mSelectedAdjustItem;

    private EffectManager() {
    }

    private void clear() {
        if (mOriginalBitmap != null) {
            mOriginalBitmap.recycle();
            mOriginalBitmap = null;
        }

        if (mAppliedBitmap != null && !mAppliedBitmap.isRecycled()) {
            mAppliedBitmap.recycle();
            mAppliedBitmap = null;
        }
    }

    public void setOriginalBitmap(Bitmap bitmap, AspectRatio aspectRatio) {
        clear();
        mOriginalBitmap =
                BitmapUtils.resizeBitmap(bitmap, aspectRatio.getWidth(), aspectRatio.getHeight());
    }

    public Bitmap getOriginalBitmap() {
        return mOriginalBitmap;
    }

    public int getBitmapHeight() {
        return mOriginalBitmap.getHeight();
    }

    public int getBitmapWidth() {
        return mOriginalBitmap.getWidth();
    }

    public Bitmap applyBitmap() {
        Mat mat = new Mat();
        BitmapUtils.convertBitmapToMat(mOriginalBitmap, mat);

        mat = mAdjustComposite.apply(mat);

        if (mAppliedBitmap == null) {
            mAppliedBitmap = BitmapUtils.createBitmap(mat.cols(), mat.rows());
        }
        BitmapUtils.convertMatToBitmap(mat, mAppliedBitmap);

        return mAppliedBitmap;
    }

    public Bitmap getAppliedBitmap() {
        return mAppliedBitmap != null ? mAppliedBitmap : applyBitmap();
    }

    public Mat getMatAppliedUtilSelectedAdjustItem() {
        Preconditions.checkState(mSelectedAdjustItem != null);

        Mat mat = new Mat();
        BitmapUtils.convertBitmapToMat(mOriginalBitmap, mat);
        return mAdjustComposite.apply(mat, mSelectedAdjustItem.getEffect());
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

    public void setAdjustComposite(AdjustComposite adjustComposite) {
        mAdjustComposite = adjustComposite;
    }

    public void setSelectedAdjustItem(AdjustItem adjustItem) {
        mSelectedAdjustItem = adjustItem;
    }

    public AdjustItem getSelectedAdjustItem() {
        return mSelectedAdjustItem;
    }
}
