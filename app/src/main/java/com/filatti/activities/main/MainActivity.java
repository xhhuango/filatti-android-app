package com.filatti.activities.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.filatti.R;
import com.filatti.activities.FilattiActivity;
import com.filatti.activities.gallery.GalleryActivity;

public class MainActivity extends FilattiActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        initAlbumButton();
    }

    private void initAlbumButton() {
        Button albumButton = (Button) findViewById(R.id.albumButton);
        albumButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, GalleryActivity.class);
                startActivity(intent);
            }
        });
    }
}
