package com.filatti.activities.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.filatti.R;
import com.filatti.activities.gallery.GalleryActivity;
import com.filatti.effects.adjusts.SaturationNativeAdjust;
import com.filatti.logger.Logger;

public class MainActivity extends Activity {
    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initAlbumButton();
    }

    private void initAlbumButton() {
        Button albumButton = (Button) findViewById(R.id.albumButton);
        albumButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaturationNativeAdjust saturationNativeAdjust = new SaturationNativeAdjust();
                Logger.debug(TAG, "native = " + saturationNativeAdjust.setSaturation());

                Intent intent = new Intent(MainActivity.this, GalleryActivity.class);
                startActivity(intent);
            }
        });
    }
}