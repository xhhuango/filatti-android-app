package com.fotro.activities.photography;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.fotro.R;
import com.fotro.activities.photography.gallery.GalleryFragment;
import com.fotro.utils.BitmapUtils;

public class PhotographyActivity extends FragmentActivity {
    private PhotographyPresenter mPresenter;

    private GalleryFragment mGalleryFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photography);

        initViews();
        mPresenter = new PhotographyPresenter(this);
    }

    private void initViews() {
        initNextButton();
        mGalleryFragment =
                (GalleryFragment) getSupportFragmentManager().findFragmentById(R.id.contentFragment);
    }

    private void initNextButton() {
        ImageButton nextButton = (ImageButton) findViewById(R.id.nextButton);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.onClickNext();
            }
        });
    }

    Bitmap getPhoto() {
        Bitmap bitmap = mGalleryFragment.crop();
        return BitmapUtils.resizeBitmap(bitmap, 1080, 1080);
    }
}
