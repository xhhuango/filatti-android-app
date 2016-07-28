package com.filatti.activities.effect;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.filatti.R;
import com.filatti.activities.effect.items.EffectItem;
import com.filatti.utils.ScreenUtils;
import com.google.common.base.Preconditions;

import java.util.ArrayList;
import java.util.List;

public class EffectActivity extends FragmentActivity {
    private EffectPresenter mPresenter;

    private View mHeader;
    private ImageView mImageView;

    private GridView mGridView;
    private EffectItemListAdapter mEffectItemListAdapter;

    private View mEffectHeader;
    private TextView mEffectNameTextView;

    private FrameLayout mEffectContainer;
    private View mEffectSettingView;

    private List<EffectItem> mEffectItemList = new ArrayList<>();
    private Bitmap mPhotoBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_effect);
        mPresenter = new EffectPresenter(this);

        initViews();

        setPhoto(mPhotoBitmap);

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
        initHeader();
        initBackButton();
        initNextButton();
        initFilterButton();
        initAdjustButton();
        initGridView();

        initEffectHeader();
        initNoButton();
        initResetButton();
        initOkButton();
        initEffectContainer();

        initImageView();
    }

    private void initHeader() {
        mHeader = findViewById(R.id.header);
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

    private void initNextButton() {
        ImageButton button = (ImageButton) findViewById(R.id.nextButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.onNextClick();
            }
        });
    }

    private void initFilterButton() {
        ImageButton button = (ImageButton) findViewById(R.id.filterButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.onFilterClick();
            }
        });
    }

    private void initAdjustButton() {
        ImageButton button = (ImageButton) findViewById(R.id.adjustButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.onAdjustClick();
            }
        });
    }

    private void initGridView() {
        mGridView = (GridView) findViewById(R.id.gridView);
        mGridView.setNumColumns(4);
        int mImageViewSize = getResources().getDisplayMetrics().widthPixels / 4;
        mGridView.setColumnWidth(mImageViewSize);
        mEffectItemListAdapter = new EffectItemListAdapter(mPresenter, mImageViewSize);
        mGridView.setAdapter(mEffectItemListAdapter);

        setEffectItemList(mEffectItemList);
    }

    private void initEffectHeader() {
        mEffectHeader = findViewById(R.id.header_effect);
        mEffectNameTextView = (TextView) findViewById(R.id.effectName);
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

    private void initEffectContainer() {
        mEffectContainer = (FrameLayout) findViewById(R.id.effectContainer);
    }

    private void initImageView() {
        mImageView = (ImageView) findViewById(R.id.imageView);
        ViewGroup.MarginLayoutParams layoutParams =
                (ViewGroup.MarginLayoutParams) mImageView.getLayoutParams();
        layoutParams.height = ScreenUtils.getScreenSize(getResources()).getWidth();
        mImageView.setLayoutParams(layoutParams);
        mImageView.requestLayout();
    }

    void setPhoto(Bitmap bitmap) {
        mPhotoBitmap = bitmap;
        if (mImageView != null) {
            mImageView.setImageBitmap(bitmap);
        }
    }

    void setEffectItemList(List<EffectItem> effectItemList) {
        Preconditions.checkNotNull(effectItemList);

        mEffectItemList = effectItemList;
        if (mEffectItemListAdapter != null) {
            mEffectItemListAdapter.setEffectItemList(effectItemList);
        }
    }

    void showEffectSettingView(View effectSettingView, String title) {
        Preconditions.checkNotNull(effectSettingView);
        Preconditions.checkNotNull(title);

        if (mEffectSettingView != null) {
            dismissEffectSettingView();
        }
        mEffectSettingView = effectSettingView;
        mEffectContainer.addView(effectSettingView);

        ViewGroup.LayoutParams layoutParams = effectSettingView.getLayoutParams();
        layoutParams.height = mEffectContainer.getHeight();
        effectSettingView.setLayoutParams(layoutParams);

        mGridView.setVisibility(View.GONE);

        mHeader.setVisibility(View.GONE);
        mEffectNameTextView.setText(title);
        mEffectHeader.setVisibility(View.VISIBLE);
    }

    void dismissEffectSettingView() {
        if (mEffectSettingView != null) {
            mEffectContainer.removeView(mEffectSettingView);
            mEffectSettingView = null;
        }
        mGridView.setVisibility(View.VISIBLE);

        mHeader.setVisibility(View.VISIBLE);
        mEffectHeader.setVisibility(View.GONE);
    }
}
