package com.fotro;

import android.app.Activity;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;

import com.fotro.imgproc.filters.Filter;
import com.fotro.imgproc.filters.FilterException;

import org.json.JSONException;
import org.json.JSONObject;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.core.MatOfInt;
import org.opencv.imgproc.Imgproc;

import java.io.IOException;
import java.io.InputStream;

public class PhotoActivity extends Activity {
    private static final String TAG = PhotoActivity.class.getSimpleName();

    static final String EXTRA_PHOTO_URI = "photoUri";

    static {
        System.loadLibrary("opencv_java3");
        OpenCVLoader.initDebug();
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photo_activity);

        Uri photoUri = getIntent().getParcelableExtra(EXTRA_PHOTO_URI);
        ImageView beforeImageView = (ImageView) findViewById(R.id.beforePhotoImageView);
        ImageView afterImageView = (ImageView) findViewById(R.id.afterPhotoImageView);

        Bitmap bitmap = null;
        try {
            bitmap = new UserPicture(photoUri, getContentResolver()).getBitmap();
        } catch (IOException e) {
            e.printStackTrace();
        }

        beforeImageView.setImageBitmap(bitmap);
        afterImageView.setImageBitmap(applyFilter(bitmap));
    }

    private Bitmap applyFilter(Bitmap bitmap) {
        Mat tmp;

        Mat src = new MatOfInt();
        Utils.bitmapToMat(bitmap, src);
        tmp = new MatOfInt();
        Imgproc.cvtColor(src, tmp, Imgproc.COLOR_RGBA2RGB);
        src = tmp;

        Filter filter = getFilter("contract_brightness.json");
        filter.check();
        filter.init();
        Mat dst = new MatOfInt();
        filter.apply(src, dst);

        tmp = new MatOfInt();
        Imgproc.cvtColor(dst, tmp, Imgproc.COLOR_RGB2RGBA);
        dst = tmp;
        Bitmap appliedBitmap = Bitmap.createBitmap(dst.cols(), dst.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(dst, appliedBitmap);

        return appliedBitmap;
    }

    private Filter getFilter(String filerFileName) {
        JSONObject jsonObject = loadJsonFile(filerFileName);
        try {
            return new Filter(jsonObject);
        } catch (FilterException e) {
            e.printStackTrace();
            return null;
        }
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
