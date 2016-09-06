package com.filatti.activities.gallery;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageButton;

import com.filatti.R;
import com.filatti.activities.FilattiActivity;
import com.filatti.utilities.photo.AspectRatio;
import com.filatti.utilities.photo.DecodeUtils;
import com.filatti.utilities.photo.DisplayUtils;
import com.filatti.utilities.ThreadPool;
import com.google.common.base.Preconditions;
import com.lyft.android.scissors.CropView;

import java.util.ArrayList;
import java.util.List;

public class GalleryActivity extends FilattiActivity {
    static final int IMAGE_CAPTURE_REQUEST_CODE = 100;

    private GalleryPresenter mPresenter;

    private ImageButton mAspectRatioButton;

    private CropView mCropView;
    private AspectRatio mAspectRatio = AspectRatio.RATIO_OF_1_TO_1;

    private PhotoListAdapter mPhotoListAdapter;
    private List<Long> mPhotoList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gallery_activity);
        mPresenter = new GalleryPresenter(this);

        initViews();
        setAspectRatio(mAspectRatio);

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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == IMAGE_CAPTURE_REQUEST_CODE) {
            mPresenter.onPictureTaken(resultCode == RESULT_OK);
        }
    }

    private void initViews() {
        initBackButton();
        initNextButton();
        initImageView();
        initAspectRatioButton();
        initCameraButton();
        initGridView();
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
        mCropView = (CropView) findViewById(R.id.cropView);
        ViewGroup.MarginLayoutParams layoutParams =
                (ViewGroup.MarginLayoutParams) mCropView.getLayoutParams();
        layoutParams.height = DisplayUtils.getScreenSize(getResources()).getWidth();
        mCropView.setLayoutParams(layoutParams);
        mCropView.requestLayout();
    }

    private void initAspectRatioButton() {
        mAspectRatioButton = (ImageButton) findViewById(R.id.aspectRatioButton);
        ViewGroup.MarginLayoutParams layoutParams =
                (ViewGroup.MarginLayoutParams) mAspectRatioButton.getLayoutParams();
        layoutParams.topMargin = DisplayUtils.getScreenSize(getResources()).getWidth();
        mAspectRatioButton.setLayoutParams(layoutParams);
        mAspectRatioButton.requestLayout();

        mAspectRatioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.onAspectRatioClick();
            }
        });
    }

    private void initCameraButton() {
        ImageButton button = (ImageButton) findViewById(R.id.cameraButton);
        ViewGroup.MarginLayoutParams layoutParams =
                (ViewGroup.MarginLayoutParams) button.getLayoutParams();
        layoutParams.topMargin = DisplayUtils.getScreenSize(getResources()).getWidth();
        button.setLayoutParams(layoutParams);
        button.requestLayout();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.onCameraClick();
            }
        });
    }

    private void initGridView() {
        GridView gridView = (GridView) findViewById(R.id.gridView);
        mPhotoListAdapter = new PhotoListAdapter(this);
        gridView.setAdapter(mPhotoListAdapter);

        setPhotoList(mPhotoList);
    }

    void setAspectRatio(AspectRatio aspectRatio) {
        Preconditions.checkNotNull(aspectRatio);

        mAspectRatio = aspectRatio;
        if (mCropView != null) {
            mCropView.setViewportRatio(aspectRatio.getRatio());
            int res = (mAspectRatio == AspectRatio.RATIO_OF_1_TO_1)
                    ? R.drawable.aspect_ratio_16_9
                    : R.drawable.aspect_ratio_1_1;
            mAspectRatioButton.setImageResource(res);
        }
    }

    AspectRatio getAspectRatio() {
        return mAspectRatio;
    }

    void setPhotoList(List<Long> photoList) {
        Preconditions.checkNotNull(photoList);

        mPhotoList = photoList;
        if (mPhotoListAdapter != null) {
            mPhotoListAdapter.setPhotoList(mPhotoList);
        }
    }

    void display(long photoId) {
        display(getPhotoUri(photoId));
    }

    void display(Uri photoUri) {
        DisplayUtils.Size screenSize = DisplayUtils.getScreenSize(getResources());
        int size = Math.min(screenSize.getWidth(), screenSize.getHeight()) * 2;
        final Bitmap bitmap = DecodeUtils.decode(this, photoUri, size, size);
        ThreadPool.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (bitmap != null) {
                    setAspectRatio((bitmap.getWidth() > bitmap.getHeight())
                                           ? AspectRatio.RATIO_OF_16_TO_9
                                           : AspectRatio.RATIO_OF_1_TO_1);
                }
                mCropView.setImageBitmap(bitmap);
            }
        });
    }

    Uri getPhotoUri(long photoId) {
        return Uri.parse(MediaStore.Images.Media.EXTERNAL_CONTENT_URI + "/" + photoId);
    }

    Bitmap crop() {
        return mCropView.crop();
    }
}
