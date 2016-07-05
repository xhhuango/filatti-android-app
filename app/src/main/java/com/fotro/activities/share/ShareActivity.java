package com.fotro.activities.share;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.fotro.R;

public class ShareActivity extends FragmentActivity {
    private SharePresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);

        mPresenter = new SharePresenter(this);
    }
}
