package com.filatti.activities.gallery;

import android.graphics.Bitmap;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.filatti.utils.DecodeUtils;
import com.filatti.utils.ThreadPool;
import com.google.common.base.Preconditions;

import java.util.ArrayList;
import java.util.List;

class PhotoListAdapter extends BaseAdapter {
    private final GalleryActivity mActivity;
    private List<Long> mPhotoList = new ArrayList<>();

    private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            mActivity.display(mPhotoList.get((Integer) view.getTag()));
        }
    };

    PhotoListAdapter(GalleryActivity activity) {
        Preconditions.checkNotNull(activity);
        mActivity = activity;
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
        GridView gridView = (GridView) parent;
        final int columnSize = gridView.getColumnWidth();
        final ImageView imageView;

        if (convertView == null) {
            imageView = new ImageView(gridView.getContext());
            GridView.LayoutParams layoutParams = new GridView.LayoutParams(columnSize, columnSize);
            imageView.setLayoutParams(layoutParams);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        } else {
            imageView = (ImageView) convertView;
            imageView.setImageBitmap(null);
        }

        final Uri photoUri = mActivity.getPhotoUri(mPhotoList.get(position));
        ThreadPool.run(new Runnable() {
            @Override
            public void run() {
                final Bitmap bitmap =
                        DecodeUtils.decode(mActivity, photoUri, columnSize, columnSize);
                ThreadPool.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        imageView.setImageBitmap(bitmap);
                    }
                });
            }
        });
        imageView.setTag(position);
        imageView.setOnClickListener(mOnClickListener);

        return imageView;
    }
}
