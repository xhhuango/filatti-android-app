package com.fotro.activities.camera;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.fotro.R;

public class CameraFragment extends Fragment {
    private ViewGroup mRootView;

    private SurfaceView mCameraView;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        mRootView = (ViewGroup) inflater.inflate(R.layout.fragment_camera, container, false);

        initCameraView();
        initTakePictureButton();

        return mRootView;
    }

    private void initCameraView() {
        mCameraView = (SurfaceView) mRootView.findViewById(R.id.cameraView);
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        int size = displayMetrics.widthPixels;
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(size, size);
        mCameraView.setLayoutParams(layoutParams);
    }

    private void initTakePictureButton() {
        ImageButton button = (ImageButton) mRootView.findViewById(R.id.takePictureButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                
            }
        });
    }
}
