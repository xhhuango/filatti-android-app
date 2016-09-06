package com.filatti.utilities.photo;

import android.graphics.Bitmap;
import android.graphics.Matrix;

import com.google.common.base.Preconditions;

import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

public final class BitmapUtils {
    private BitmapUtils() {
    }

    public static Bitmap resizeBitmap(final Bitmap input, int destWidth, int destHeight)
            throws OutOfMemoryError {
        return resizeBitmap(input, destWidth, destHeight, 0);
    }

    public static Bitmap resizeBitmap(Bitmap input, int destWidth, int destHeight, int rotation)
            throws OutOfMemoryError {
        int dstWidth = destWidth;
        int dstHeight = destHeight;
        final int srcWidth = input.getWidth();
        final int srcHeight = input.getHeight();

        if (rotation == 90 || rotation == 270) {
            dstWidth = destHeight;
            dstHeight = destWidth;
        }

        boolean needsResize = false;
        if ((srcWidth > dstWidth) || (srcHeight > dstHeight)) {
            needsResize = true;
            if ((srcWidth > srcHeight) && (srcWidth > dstWidth)) {
                float p = (float) dstWidth / (float) srcWidth;
                dstHeight = (int) (srcHeight * p);
            } else {
                float p = (float) dstHeight / (float) srcHeight;
                dstWidth = (int) (srcWidth * p);
            }
        } else {
            dstWidth = srcWidth;
            dstHeight = srcHeight;
        }

        if (needsResize || rotation != 0) {
            Bitmap output;
            if (rotation == 0) {
                output = Bitmap.createScaledBitmap(input, dstWidth, dstHeight, true);
            } else {
                Matrix matrix = new Matrix();
                matrix.postScale((float) dstWidth / srcWidth, (float) dstHeight / srcHeight);
                matrix.postRotate(rotation);
                output = Bitmap.createBitmap(input, 0, 0, srcWidth, srcHeight, matrix, true);
            }
            return output;
        } else {
            return input;
        }
    }

    public static Bitmap createBitmap(int cols, int rows) {
        return Bitmap.createBitmap(cols, rows, Bitmap.Config.ARGB_8888);
    }

    public static void convertMatToBitmap(Mat src, Bitmap dst) {
        Preconditions.checkNotNull(src);
        Preconditions.checkNotNull(dst);

        Mat tmp = new Mat(src.size(), src.type());
        Imgproc.cvtColor(src, tmp, Imgproc.COLOR_BGR2RGB);
        Utils.matToBitmap(tmp, dst);
        tmp.release();
    }

    public static void convertBitmapToMat(Bitmap src, Mat dst) {
        Mat mat = new Mat();
        Utils.bitmapToMat(src, mat);
        Imgproc.cvtColor(mat, dst, Imgproc.COLOR_RGBA2BGR);
        mat.release();
    }
}
