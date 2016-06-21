package com.fotro.activities.photography.gallery;

import android.content.ContentResolver;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.fotro.R;
import com.fotro.utils.DecodeUtils;
import com.lyft.android.scissors.CropView;

import java.util.ArrayList;
import java.util.List;

public class GalleryFragment extends Fragment {
    private static final int GRID_COLUMNS = 4;
    private static final int PADDING = 1;

    private ViewGroup mRootView;
    private CropView mCropView;

    private BaseAdapter mGridViewAdapter;

    private List<Long> mPhotoList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        mRootView = (ViewGroup) inflater.inflate(R.layout.fragment_gallery, container, false);

        initImageViewTouch();
        initGridView();

        return mRootView;
    }

    private void initImageViewTouch() {
        mCropView =
                (CropView) mRootView.findViewById(R.id.cropView);
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        int size = displayMetrics.widthPixels;
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(size, size);
        mCropView.setLayoutParams(layoutParams);
    }

    private void initGridView() {
        GridView gridView = (GridView) mRootView.findViewById(R.id.gridView);
        gridView.setNumColumns(GRID_COLUMNS);
        final int mImageViewSize = getResources().getDisplayMetrics().widthPixels / GRID_COLUMNS;
        gridView.setColumnWidth(mImageViewSize);
        gridView.setHorizontalSpacing(2);
        gridView.setVerticalSpacing(2);
        mGridViewAdapter = new BaseAdapter() {
            private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    display(mPhotoList.get((Integer) view.getTag()));
                }
            };

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
                return 0;
            }

            @Override
            public View getView(final int position, View convertView, ViewGroup parent) {
                ImageButton imageButton;

                int imageSize = mImageViewSize - (PADDING * 2);

                if (convertView == null) {
                    imageButton = new ImageButton(GalleryFragment.this.getActivity());
                    imageButton.setLayoutParams(new GridView.LayoutParams(imageSize, imageSize));
                    imageButton.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    imageButton.setPadding(PADDING, PADDING, PADDING, PADDING);
                } else {
                    imageButton = (ImageButton) convertView;
                }

                Uri photoUri = getPhotoUri(mPhotoList.get(position));
                Bitmap bitmap = DecodeUtils.decode(getActivity(), photoUri, imageSize, imageSize);
                imageButton.setImageBitmap(bitmap);
                imageButton.setTag(position);
                imageButton.setOnClickListener(mOnClickListener);

                return imageButton;
            }
        };
        gridView.setAdapter(mGridViewAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();

        mPhotoList = getPhotoList();
        display(mPhotoList.get(0));
        mGridViewAdapter.notifyDataSetChanged();
    }

    private List<Long> getPhotoList() {
        String[] projection = new String[]{MediaStore.Images.Media._ID};
        ContentResolver contentResolver = getActivity().getContentResolver();
        Cursor cursor = contentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                              projection,
                                              "",
                                              null,
                                              MediaStore.Images.Media.DATE_ADDED);

        List<Long> photoList = new ArrayList<>();
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int idColumn = cursor.getColumnIndex(projection[0]);
                do {
                    Long photoId = cursor.getLong(idColumn);
                    photoList.add(0, photoId);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        return photoList;
    }

    private void display(long photoId) {
        Uri photoUri = getPhotoUri(photoId);
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int size = Math.min(metrics.widthPixels, metrics.heightPixels) * 2;
        Bitmap bitmap = DecodeUtils.decode(getActivity(), photoUri, size, size);
        mCropView.setImageBitmap(bitmap);
    }

    private Uri getPhotoUri(long photoId) {
        return Uri.parse(MediaStore.Images.Media.EXTERNAL_CONTENT_URI + "/" + photoId);
    }

    public Bitmap crop() {
        return mCropView.crop();
    }
}
