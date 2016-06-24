package com.fotro.activities.editor.adjustment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.fotro.R;

public class AdjustmentFragment extends Fragment {
    private ViewGroup mRootView;
    private ImageView mImageView;

    private Bitmap mPhoto;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        mRootView = (ViewGroup) inflater.inflate(R.layout.fragment_adjustment, container, false);

        initImageView();

        return mRootView;
    }

    private void initImageView() {
        mImageView = (ImageView) mRootView.findViewById(R.id.imageView);
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        int size = displayMetrics.widthPixels;
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(size, size);
        mImageView.setLayoutParams(layoutParams);
    }

    public void setPhoto(Bitmap bitmap) {
        mPhoto = bitmap;
        if (mImageView != null) {
            mImageView.setImageBitmap(mPhoto);
        }
    }
}