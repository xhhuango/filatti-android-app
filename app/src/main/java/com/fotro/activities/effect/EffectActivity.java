package com.fotro.activities.effect;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.fotro.R;
import com.fotro.utils.ScreenUtils;
import com.google.common.base.Preconditions;

import java.util.ArrayList;
import java.util.List;

public class EffectActivity extends FragmentActivity {
    private EffectPresenter mPresenter;

    private ImageView mImageView;

    private EffectItemListAdapter mEffectItemListAdapter;

    private List<EffectItem> mEffectItemList = new ArrayList<>();
    private Bitmap mPhotoBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_effect);

        initViews();

        setPhoto(mPhotoBitmap);

        mPresenter = new EffectPresenter(this);
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

    private void initViews() {
        initBackButton();
        initNextButton();
        initImageView();
        initGridView();
        initFilterButton();
        initAdjustButton();
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

    private void initImageView() {
        mImageView = (ImageView) findViewById(R.id.imageView);
        ViewGroup.MarginLayoutParams layoutParams =
                (ViewGroup.MarginLayoutParams) mImageView.getLayoutParams();
        layoutParams.height = ScreenUtils.getScreenSize(getResources()).getWidth();
        mImageView.setLayoutParams(layoutParams);
        mImageView.requestLayout();
    }

    private void initGridView() {
        GridView gridView = (GridView) findViewById(R.id.gridView);
        mEffectItemListAdapter = new EffectItemListAdapter();
        gridView.setAdapter(mEffectItemListAdapter);

        setEffectItemList(mEffectItemList);
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
}
