package com.fotro.activities.effect;

import android.app.Activity;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;

import com.fotro.R;
import com.fotro.effects.ImageProcessingException;
import com.fotro.effects.filters.Filter;
import com.fotro.effects.filters.FilterFactory;
import com.fotro.logger.Logger;

import org.json.JSONException;
import org.json.JSONObject;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.core.MatOfInt;
import org.opencv.imgproc.Imgproc;

import java.io.IOException;
import java.io.InputStream;

public class AdjustmentActivity extends Activity {
    private static final String TAG = AdjustmentActivity.class.getSimpleName();

    public static final String EXTRA_PHOTO_URI = "photoUri";

    static {
        System.loadLibrary("opencv_java3");
        OpenCVLoader.initDebug();
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.adjustment_activity);

        Uri photoUri = getIntent().getParcelableExtra(EXTRA_PHOTO_URI);
        ImageView beforeImageView = (ImageView) findViewById(R.id.beforePhotoImageView);
        final ImageView afterImageView = (ImageView) findViewById(R.id.afterPhotoImageView);

        Bitmap bitmap = null;
//        try {
//            bitmap = new UserPicture(photoUri, getContentResolver()).getBitmap();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        beforeImageView.setImageBitmap(bitmap);
        final Bitmap finalBitmap = bitmap;
        new Thread(new Runnable() {
            @Override
            public void run() {
                final Bitmap bitmap1 = applyFilter(finalBitmap);

                AdjustmentActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        afterImageView.setImageBitmap(bitmap1);
                    }
                });
            }
        }).start();
    }

    private Bitmap applyFilter(Bitmap bitmap) {
        try {
            Mat tmp;

            Logger.debug(TAG, "tmp size = " + (bitmap.getRowBytes() / 1000.0));

            long before, after;

            Mat src = new MatOfInt();

            before = System.currentTimeMillis();
            Utils.bitmapToMat(bitmap, src);
            after = System.currentTimeMillis();
            Logger.debug(TAG, "bitmapToMat(bitmap, src): " + (after - before));

            tmp = new MatOfInt();
            before = System.currentTimeMillis();
            Imgproc.cvtColor(src, tmp, Imgproc.COLOR_RGBA2RGB);
            after = System.currentTimeMillis();
            Logger.debug(TAG, "cvtColor(src, tmp, RGBA2RGB): " + (after - before));
            src = tmp;

            Filter filter = createFilter("coco.json");
            Mat dst = new MatOfInt();
            before = System.currentTimeMillis();
            filter.apply(src, dst);
            after = System.currentTimeMillis();
            Logger.debug(TAG, "apply(): " + (after - before));

            tmp = new MatOfInt();
            before = System.currentTimeMillis();
            Imgproc.cvtColor(dst, tmp, Imgproc.COLOR_RGB2RGBA);
            after = System.currentTimeMillis();
            Logger.debug(TAG, "cvtColor(dst, tmp, RGB2RGBA): " + (after - before));
            dst = tmp;
            Bitmap appliedBitmap = Bitmap.createBitmap(dst.cols(), dst.rows(), Bitmap.Config.ARGB_8888);
            before = System.currentTimeMillis();
            Utils.matToBitmap(dst, appliedBitmap);
            after = System.currentTimeMillis();
            Logger.debug(TAG, "matToBitmap(dst, appliedBitmap): " + (after - before));

            return appliedBitmap;
        } catch (ImageProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    private Filter createFilter(String filerFileName) throws ImageProcessingException {
        JSONObject object = loadJsonFile(filerFileName);
        return new FilterFactory().importFilter(object);
    }

    private JSONObject loadJsonFile(String fileName) {
        try {
            InputStream is = getAssets().open(fileName);
            byte[] buffer = new byte[is.available()];
            is.read(buffer);
            return new JSONObject(new String(buffer));
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
