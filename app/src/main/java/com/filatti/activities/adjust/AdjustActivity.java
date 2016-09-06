package com.filatti.activities.adjust;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.filatti.R;
import com.filatti.activities.FilattiActivity;
import com.filatti.managers.EffectManager;
import com.filatti.utilities.photo.DisplayUtils;

public class AdjustActivity extends FilattiActivity {
    private AdjustPresenter mPresenter;

    private ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.adjust_activity);

        mPresenter = new AdjustPresenter(this);
        mPresenter.onCreate();

        initViews();
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
        super.onBackPressed();
    }

    private void initViews() {
        initTitleTextView();
        initNoButton();
        initResetButton();
        initOkButton();
        initImageView();
        initAdjustView();
    }

    private void initTitleTextView() {
        TextView textView = (TextView) findViewById(R.id.titleTextView);
        textView.setText(mPresenter.getAdjustName());
    }

    private void initNoButton() {
        ImageButton button = (ImageButton) findViewById(R.id.noButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.onCancelEffectItem();
            }
        });
    }

    private void initResetButton() {
        ImageButton button = (ImageButton) findViewById(R.id.resetButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.onResetEffectItem();
            }
        });
    }

    private void initOkButton() {
        ImageButton button = (ImageButton) findViewById(R.id.okButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.onApplyEffectItem();
            }
        });
    }

    private void initImageView() {
        mImageView = (ImageView) findViewById(R.id.imageView);
        ViewGroup.MarginLayoutParams layoutParams =
                (ViewGroup.MarginLayoutParams) mImageView.getLayoutParams();
        layoutParams.height = DisplayUtils.getScreenSize(getResources()).getWidth();
        mImageView.setLayoutParams(layoutParams);
        mImageView.requestLayout();
        mImageView.setImageBitmap(EffectManager.getInstance().getOriginalBitmap());
    }

    private void initAdjustView() {
        ViewGroup container = (FrameLayout) findViewById(R.id.adjustContainer);
        View adjustView = mPresenter.getAdjustView(container);
        container.addView(adjustView);
    }

    void setPhoto(Bitmap bitmap) {
        if (mImageView != null) {
            mImageView.setImageBitmap(bitmap);
        }
    }
}
