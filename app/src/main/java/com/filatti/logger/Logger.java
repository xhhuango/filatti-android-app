package com.filatti.logger;

import android.util.Log;

import com.filatti.BuildConfig;

public final class Logger {
    private Logger() {
    }

    public static void debug(String tag, String msg) {
        if (BuildConfig.DEBUG)
            Log.d(tag, msg);
    }

    public static void debug(String tag, String msg, Throwable throwable) {
        if (BuildConfig.DEBUG)
            Log.d(tag, msg, throwable);
    }

    public static void error(String tag, String msg) {
        Log.e(tag, msg);
    }

    public static void error(String tag, String msg, Throwable throwable) {
        Log.e(tag, msg, throwable);
    }

    public static void error(String tag, Throwable throwable) {
        Log.e(tag, throwable.getMessage(), throwable);
    }
}
