package com.filatti.activities.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;

import com.filatti.R;
import com.filatti.activities.FilattiActivity;
import com.filatti.activities.gallery.GalleryActivity;

public class MainActivity extends FilattiActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        initTabLayou();
    }

    private void initTabLayou() {
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        break;

                    case 1:
                        Intent intent = new Intent(MainActivity.this, GalleryActivity.class);
                        startActivity(intent);
                        break;

                    case 2:
                        break;

                    default:
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                onTabSelected(tab);
            }
        });
    }
}
