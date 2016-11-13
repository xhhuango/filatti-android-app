package com.filatti.effects;

import android.graphics.Bitmap;
import android.os.Environment;

import com.filatti.activities.adjust.items.AdjustItem;
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

    public static synchronized EffectManager initInstance(Bitmap bitmap, AspectRatio aspectRatio) {
        destroyInstance();
        sEffectManager = new EffectManager(bitmap, aspectRatio);
        return sEffectManager;
    }

    public static synchronized EffectManager getInstance() {
        Preconditions.checkNotNull(sEffectManager);
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

    private EffectManager(Bitmap bitmap, AspectRatio aspectRatio) {
        mOriginalBitmap =
                BitmapUtils.resizeBitmap(bitmap, aspectRatio.getWidth(), aspectRatio.getHeight());
        mAdjustComposite = new AdjustComposite();
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

    public AdjustComposite getAdjustComposite() {
        return mAdjustComposite;
    }

    public void setSelectedAdjustItem(AdjustItem adjustItem) {
        mSelectedAdjustItem = adjustItem;
    }

    public AdjustItem getSelectedAdjustItem() {
        return mSelectedAdjustItem;
    }
}
