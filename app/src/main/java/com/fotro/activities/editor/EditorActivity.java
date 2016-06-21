package com.fotro.activities.editor;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import com.fotro.R;
import com.fotro.activities.editor.adjustment.AdjustmentFragment;

public class EditorActivity extends FragmentActivity {
    private EditorPresenter mPresenter;

    private AdjustmentFragment mAdjustmentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        initViews();

        mPresenter = new EditorPresenter(this);
    }

    private void initViews() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        mAdjustmentFragment =
                (AdjustmentFragment) fragmentManager.findFragmentById(R.id.contentFragment);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.onResume();
    }

    void setPhoto(Bitmap bitmap) {
        mAdjustmentFragment.setPhoto(bitmap);
    }
}
