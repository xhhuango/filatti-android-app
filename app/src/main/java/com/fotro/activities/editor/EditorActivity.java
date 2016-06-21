package com.fotro.activities.editor;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.fotro.R;

public class EditorActivity extends FragmentActivity {
    private EditorPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        mPresenter = new EditorPresenter(this);
    }
}
