package com.filatti.activities;

import android.app.Application;

import com.filatti.BuildConfig;

import timber.log.Timber;

public class FilattiApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
    }
}
