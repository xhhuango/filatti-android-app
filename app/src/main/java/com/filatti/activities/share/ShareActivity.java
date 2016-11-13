package com.filatti.activities.share;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.filatti.R;
import com.filatti.effects.EffectManager;
import com.filatti.utilities.photo.DisplayUtils;

public class ShareActivity extends FragmentActivity {
    private SharePresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.share_activity);

        mPresenter = new SharePresenter(this);
        initViews();
        mPresenter.onCreate();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.onResume();
    }

    @Override
    protected void onPause() {
        mPresenter.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mPresenter.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        mPresenter.onBackPressed();
    }

    private void initViews() {
        initBackButton();
        initFinishButton();
        initImageView();
    }

    private void initBackButton() {
        ImageButton button = (ImageButton) findViewById(R.id.backButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.onBackClick();
            }
        });
    }

    private void initFinishButton() {
        ImageButton button = (ImageButton) findViewById(R.id.finishButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.onFinishClick();
            }
        });
    }

    private void initImageView() {
        ViewGroup imageContainer = (ViewGroup) findViewById(R.id.imageContainer);
        ViewGroup.MarginLayoutParams layoutParams =
                (ViewGroup.MarginLayoutParams) imageContainer.getLayoutParams();
        layoutParams.height = DisplayUtils.getScreenSize(getResources()).getWidth();
        imageContainer.setLayoutParams(layoutParams);
        imageContainer.requestLayout();

        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        imageView.setImageBitmap(EffectManager.getInstance().getAppliedBitmap());
    }
}
