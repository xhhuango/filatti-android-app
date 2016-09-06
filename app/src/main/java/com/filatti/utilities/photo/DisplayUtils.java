package com.filatti.utilities.photo;

import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.TypedValue;

public final class DisplayUtils {
    private DisplayUtils() {
    }

    public static int dipToPixel(int dip) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                                               dip,
                                               Resources.getSystem().getDisplayMetrics());
    }

    public static Size getScreenSize(Resources resources) {
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        return new Size(displayMetrics.widthPixels, displayMetrics.heightPixels);
    }

    /**
     * android.util.Size is supported since API 21
     */
    public static class Size {
        private final int mWidth;
        private final int mHeight;

        private Size(int width, int height) {
            mWidth = width;
            mHeight = height;
        }

        public int getWidth() {
            return mWidth;
        }

        public int getHeight() {
            return mHeight;
        }
    }
}
