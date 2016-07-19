package com.filatti.activities.mvp;

public abstract class AbstractPresenter<T> {
    protected final T mActivity;

    protected AbstractPresenter(T activity) {
        mActivity = activity;
    }

    protected T getActivity() {
        return mActivity;
    }

    protected void onCreate() {
    }

    protected void onRestart() {
    }

    protected void onStart() {
    }

    protected void onResume() {
    }

    protected void onPause() {
    }

    protected void onStop() {
    }

    protected void onDestroy() {
    }

    protected void onBackPressed() {
    }
}
