package com.filatti.utilities.photo;

import com.google.common.base.Preconditions;

public enum AspectRatio {
    RATIO_OF_1_TO_1(1080, 1080),
    RATIO_OF_16_TO_9(1920, 1080);

    private final int mWidth;
    private final int mHeight;
    private final float mRatio;

    AspectRatio(int width, int height) {
        Preconditions.checkArgument(width > 0);
        Preconditions.checkArgument(height > 0);

        mWidth = width;
        mHeight = height;
        mRatio = ((float) width) / ((float) height);
    }

    public int getWidth() {
        return mWidth;
    }

    public int getHeight() {
        return mHeight;
    }

    public float getRatio() {
        return mRatio;
    }
}
