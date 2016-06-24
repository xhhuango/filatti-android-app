package com.fotro.utils;

import android.content.res.Resources;
import android.util.DisplayMetrics;

public final class ScreenUtils {
    public static Size getScreenSize(Resources resources) {
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        return new Size(displayMetrics.widthPixels, displayMetrics.heightPixels);
    }

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
