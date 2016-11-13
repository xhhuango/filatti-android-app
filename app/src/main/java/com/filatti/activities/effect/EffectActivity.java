package com.filatti.activities.effect;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.filatti.R;
import com.filatti.activities.FilattiActivity;
import com.filatti.activities.adjust.items.AdjustItem;
import com.filatti.utilities.photo.DisplayUtils;
import com.google.common.base.Preconditions;

import java.util.ArrayList;
import java.util.List;

public class EffectActivity extends FilattiActivity {
    private EffectPresenter mPresenter;

    private ImageView mImageView;

    private EffectItemListAdapter mEffectItemListAdapter;

    private List<AdjustItem> mEffectItemList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.effect_activity);

        mPresenter = new EffectPresenter(this);
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
        initNextButton();
        initFilterButton();
        initAdjustButton();
        initGridView();
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
        GridView gridView = (GridView) findViewById(R.id.gridView);
        gridView.setNumColumns(4);
        int mImageViewSize = getResources().getDisplayMetrics().widthPixels / 4;
        gridView.setColumnWidth(mImageViewSize);
        mEffectItemListAdapter = new EffectItemListAdapter(mPresenter, mImageViewSize);
        gridView.setAdapter(mEffectItemListAdapter);

        setEffectItemList(mEffectItemList);
    }

    private void initImageView() {
        mImageView = (ImageView) findViewById(R.id.imageView);
        ViewGroup.MarginLayoutParams layoutParams =
                (ViewGroup.MarginLayoutParams) mImageView.getLayoutParams();
        layoutParams.height = DisplayUtils.getScreenSize(getResources()).getWidth();
        mImageView.setLayoutParams(layoutParams);
        mImageView.requestLayout();
    }

    void setPhoto(Bitmap bitmap) {
        if (mImageView != null) {
            mImageView.setImageBitmap(bitmap);
        }
    }

    void setEffectItemList(List<AdjustItem> effectItemList) {
        Preconditions.checkNotNull(effectItemList);

        mEffectItemList = effectItemList;
        if (mEffectItemListAdapter != null) {
            mEffectItemListAdapter.setEffectItemList(effectItemList);
        }
    }
}
