package com.filatti.utilities;

import com.filatti.BuildConfig;

public final class Millis {
    private Millis() {
    }

    public static long now() {
        return BuildConfig.DEBUG ? System.currentTimeMillis() : 0;
    }
}
