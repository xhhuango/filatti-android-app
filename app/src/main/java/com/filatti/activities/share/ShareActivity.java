package com.filatti.activities.share;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.filatti.R;

public class ShareActivity extends FragmentActivity {
    private SharePresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.share_activity);

        mPresenter = new SharePresenter(this);
    }
}
