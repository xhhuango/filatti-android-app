package com.fotro.activities.camera;

import android.content.Context;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.fotro.logger.Logger;

import java.io.IOException;

public class CameraView extends SurfaceView {
    private static final String TAG = CameraView.class.getSimpleName();

    private Camera mCamera;

    private final SurfaceHolder.Callback mSurfaceHolderCallback = new SurfaceHolder.Callback() {
        @Override
        public void surfaceCreated(SurfaceHolder surfaceHolder) {
            try {
                mCamera = Camera.open();

                Camera.Parameters param;
                param = mCamera.getParameters();
                mCamera.setParameters(param);

                mCamera.setPreviewDisplay(getHolder());
                mCamera.setDisplayOrientation(90);
                mCamera.startPreview();
            } catch (IOException e) {
                Logger.error(TAG, "Can't open camera", e);
            }
        }

        @Override
        public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
            if (getHolder().getSurface() == null)
                return;

            try {
                mCamera.stopPreview();
            } catch (Exception e) {
                Logger.error(TAG, "Can't stop camera preview", e);
            }

            try {
                mCamera.setPreviewDisplay(getHolder());
                mCamera.startPreview();
            } catch (IOException e) {
                Logger.error(TAG, "Can't open camera", e);
            }
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    };

    public CameraView(Context context) {
        super(context);
        initHolder();
    }

    public CameraView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initHolder();
    }

    public CameraView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initHolder();
    }

    private void initHolder() {
        SurfaceHolder surfaceHolder = getHolder();
        surfaceHolder.addCallback(mSurfaceHolderCallback);
    }
}
