package com.fotro.activities.gallery;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.fotro.R;
import com.fotro.photo.AspectRatio;
import com.fotro.utils.DecodeUtils;
import com.fotro.utils.ScreenUtils;
import com.fotro.utils.ThreadPool;
import com.google.common.base.Preconditions;
import com.lyft.android.scissors.CropView;

import java.util.ArrayList;
import java.util.List;

public class GalleryActivity extends FragmentActivity {
    static final int IMAGE_CAPTURE_REQUEST_CODE = 100;

    private static final int GRID_COLUMNS = 4;

    private GalleryPresenter mPresenter;

    private Button mAspectRatioButton;

    private CropView mCropView;
    private AspectRatio mAspectRatio = AspectRatio.RATIO_OF_1_TO_1;

    private BaseAdapter mPhotoListAdapter;
    private List<Long> mPhotoList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        initViews();
        setAspectRatio(mAspectRatio);

        mPresenter = new GalleryPresenter(this);
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
        initImageViewTouch();
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

    private void initImageViewTouch() {
        mCropView = (CropView) findViewById(R.id.cropView);
        ViewGroup.MarginLayoutParams layoutParams =
                (ViewGroup.MarginLayoutParams) mCropView.getLayoutParams();
        layoutParams.height = ScreenUtils.getScreenSize(getResources()).getWidth();
        mCropView.setLayoutParams(layoutParams);
        mCropView.requestLayout();
    }

    private void initAspectRatioButton() {
        mAspectRatioButton = (Button) findViewById(R.id.aspectRatioButton);
        ViewGroup.MarginLayoutParams layoutParams =
                (ViewGroup.MarginLayoutParams) mAspectRatioButton.getLayoutParams();
        layoutParams.topMargin = ScreenUtils.getScreenSize(getResources()).getWidth();
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
        Button button = (Button) findViewById(R.id.cameraButton);
        ViewGroup.MarginLayoutParams layoutParams =
                (ViewGroup.MarginLayoutParams) button.getLayoutParams();
        layoutParams.topMargin = ScreenUtils.getScreenSize(getResources()).getWidth();
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
        gridView.setNumColumns(GRID_COLUMNS);
        int mImageViewSize = getResources().getDisplayMetrics().widthPixels / GRID_COLUMNS;
        gridView.setColumnWidth(mImageViewSize);
        mPhotoListAdapter = new PhotoListAdapter(mImageViewSize);
        gridView.setAdapter(mPhotoListAdapter);

        setPhotoList(mPhotoList);
    }

    void setAspectRatio(AspectRatio aspectRatio) {
        Preconditions.checkNotNull(aspectRatio);

        mAspectRatio = aspectRatio;
        if (mCropView != null) {
            mCropView.setViewportRatio(aspectRatio.getRatio());
            mAspectRatioButton.setText(mAspectRatio == AspectRatio.RATIO_OF_1_TO_1 ? "16:9" : "1:1");
        }
    }

    AspectRatio getAspectRatio() {
        return mAspectRatio;
    }

    void setPhotoList(List<Long> photoList) {
        Preconditions.checkNotNull(photoList);

        mPhotoList = photoList;
        if (mPhotoListAdapter != null) {
            ThreadPool.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mPhotoListAdapter.notifyDataSetChanged();
                }
            });
        }
    }

    void display(long photoId) {
        display(getPhotoUri(photoId));
    }

    void display(Uri photoUri) {
        ScreenUtils.Size screenSize = ScreenUtils.getScreenSize(getResources());
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

    private Uri getPhotoUri(long photoId) {
        return Uri.parse(MediaStore.Images.Media.EXTERNAL_CONTENT_URI + "/" + photoId);
    }

    Bitmap crop() {
        return mCropView.crop();
    }

    private class PhotoListAdapter extends BaseAdapter {
        private static final int PADDING = 1;

        private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                display(mPhotoList.get((Integer) view.getTag()));
            }
        };

        private final int mImageSize;

        private PhotoListAdapter(int imageViewSize) {
            Preconditions.checkArgument(imageViewSize > 0);
            mImageSize = imageViewSize - (PADDING * 2);
        }

        @Override
        public int getCount() {
            return mPhotoList.size();
        }

        @Override
        public Object getItem(int position) {
            return mPhotoList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return mPhotoList.get(position);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final ImageButton imageButton;

            if (convertView == null) {
                imageButton = new ImageButton(GalleryActivity.this);
                imageButton.setLayoutParams(new GridView.LayoutParams(mImageSize, mImageSize));
                imageButton.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageButton.setPadding(PADDING, PADDING, PADDING, PADDING);
            } else {
                imageButton = (ImageButton) convertView;
                imageButton.setImageBitmap(null);
            }

            final Uri photoUri = getPhotoUri(mPhotoList.get(position));
            ThreadPool.run(new Runnable() {
                @Override
                public void run() {
                    final Bitmap bitmap = DecodeUtils.decode(GalleryActivity.this,
                                                             photoUri,
                                                             mImageSize,
                                                             mImageSize);
                    ThreadPool.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            imageButton.setImageBitmap(bitmap);
                        }
                    });
                }
            });
            imageButton.setTag(position);
            imageButton.setOnClickListener(mOnClickListener);

            return imageButton;
        }
    }
}
