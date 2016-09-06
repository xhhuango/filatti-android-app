package com.filatti.utilities;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class ThreadPool {
    private static Handler sUiThreadHandler;
    private static final ExecutorService EXECUTOR_SERVICE = Executors.newCachedThreadPool();

    private ThreadPool() {
    }

    public static boolean checkIfMainThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }

    public static void runOnUiThread(Runnable runnable) {
        if (sUiThreadHandler == null) {
            sUiThreadHandler = new Handler(Looper.getMainLooper());
        }
        sUiThreadHandler.post(runnable);
    }

    public static void run(Runnable runnable) {
        EXECUTOR_SERVICE.execute(runnable);
    }
}
