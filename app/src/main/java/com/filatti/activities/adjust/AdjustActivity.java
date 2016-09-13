package com.filatti.activities.adjust;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.filatti.R;
import com.filatti.activities.FilattiActivity;
import com.filatti.managers.EffectManager;
import com.filatti.utilities.photo.DisplayUtils;

public class AdjustActivity extends FilattiActivity {
    private AdjustPresenter mPresenter;

    private ViewGroup mImageContainer;
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
        initImageOverlayView();
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
        mImageContainer = (ViewGroup) findViewById(R.id.imageContainer);
        ViewGroup.MarginLayoutParams layoutParams =
                (ViewGroup.MarginLayoutParams) mImageContainer.getLayoutParams();
        layoutParams.height = DisplayUtils.getScreenSize(getResources()).getWidth();
        mImageContainer.setLayoutParams(layoutParams);
        mImageContainer.requestLayout();

        mImageView = (ImageView) findViewById(R.id.imageView);
        mImageView.setImageBitmap(EffectManager.getInstance().getOriginalBitmap());
    }

    private void initImageOverlayView() {
        View adjustOverlayView = mPresenter.getAdjustOverlyView(mImageContainer);

        RelativeLayout.LayoutParams layoutParams =
                (RelativeLayout.LayoutParams) adjustOverlayView.getLayoutParams();
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        Bitmap bitmap = EffectManager.getInstance().getOriginalBitmap();
        layoutParams.height = bitmap.getHeight();
        layoutParams.width = bitmap.getWidth();
        adjustOverlayView.setLayoutParams(layoutParams);

        mImageContainer.addView(adjustOverlayView);
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
