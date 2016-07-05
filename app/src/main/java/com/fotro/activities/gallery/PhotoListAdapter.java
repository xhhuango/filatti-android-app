package com.fotro.activities.gallery;

import android.graphics.Bitmap;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.fotro.utils.DecodeUtils;
import com.fotro.utils.ThreadPool;
import com.google.common.base.Preconditions;

import java.util.ArrayList;
import java.util.List;

class PhotoListAdapter extends BaseAdapter {
    private static final int PADDING = 1;

    private final GalleryActivity mActivity;
    private List<Long> mPhotoList = new ArrayList<>();
    private final int mImageSize;

    private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            mActivity.display(mPhotoList.get((Integer) view.getTag()));
        }
    };

    PhotoListAdapter(GalleryActivity activity, int imageViewSize) {
        Preconditions.checkNotNull(activity);
        Preconditions.checkArgument(imageViewSize > 0);
        mActivity = activity;
        mImageSize = imageViewSize - (PADDING * 2);
    }

    void setPhotoList(List<Long> photoList) {
        Preconditions.checkNotNull(photoList);

        mPhotoList = photoList;
        ThreadPool.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                notifyDataSetChanged();
            }
        });
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
            imageButton = new ImageButton(mActivity);
            imageButton.setLayoutParams(new GridView.LayoutParams(mImageSize, mImageSize));
            imageButton.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageButton.setPadding(PADDING, PADDING, PADDING, PADDING);
        } else {
            imageButton = (ImageButton) convertView;
            imageButton.setImageBitmap(null);
        }

        final Uri photoUri = mActivity.getPhotoUri(mPhotoList.get(position));
        ThreadPool.run(new Runnable() {
            @Override
            public void run() {
                final Bitmap bitmap = DecodeUtils.decode(mActivity,
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
