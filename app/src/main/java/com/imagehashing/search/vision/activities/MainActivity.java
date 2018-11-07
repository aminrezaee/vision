package com.imagehashing.search.vision.activities;

import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.imagehashing.search.vision.Application;
import com.imagehashing.search.vision.AssetsFileReader;
import com.imagehashing.search.vision.CacheClearAsyncTask;
import com.imagehashing.search.vision.Designer;
import com.imagehashing.search.vision.R;
import com.imagehashing.search.vision.dataModels.Image;
import com.imagehashing.search.vision.dataModels.Pattern;
import com.imagehashing.search.vision.dataModels.Pattern_;
import com.imagehashing.search.vision.fragments.ConstraintsFragment;
import com.imagehashing.search.vision.fragments.FragmentHandler;
import com.imagehashing.search.vision.fragments.MainFragment;
import com.imagehashing.search.vision.fragments.SettingsFragment;
import com.imagehashing.search.vision.methods.LocalitySensitiveHasing;
import com.imagehashing.search.vision.methods.SemanticHashing;


import java.io.IOException;
import java.util.List;
import java.util.Queue;

import io.objectbox.Box;
import io.objectbox.query.Query;

public class MainActivity extends AppCompatActivity implements FragmentHandler {
    boolean itemClicked = false;
    Box<Image> imageBox;
    Box<Pattern> patternBox;
    MainFragment mainFragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageBox = ((Application) getApplication()).getBoxStore().boxFor(Image.class);
        patternBox = ((Application) getApplication()).getBoxStore().boxFor(Pattern.class);

        final BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);


        final Designer designer = new Designer(getApplicationContext());
        designer.setBottomNavigationItemColor(bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                new CacheClearAsyncTask(getApplicationContext()).execute();
                Fragment selectedFragment = null;
                switch (item.getItemId()) {
                    case R.id.home:
                        selectedFragment = new MainFragment();
                        break;
                    case R.id.settings:
                        SettingsFragment fragment = new SettingsFragment();
                        fragment.setManager(getFragmentManager());
                        selectedFragment = fragment;
                        break;
                    case R.id.constraints:
                        selectedFragment = new ConstraintsFragment();
                }
                itemClicked = true;
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.bottom_navigation_fragment, selectedFragment);
                transaction.commit();
                return true;
            }
        });
        bottomNavigationView.setOnNavigationItemReselectedListener(new BottomNavigationView.OnNavigationItemReselectedListener() {
            @Override
            public void onNavigationItemReselected(@NonNull MenuItem item) {
                if (!itemClicked) {
                    Fragment selectedFragment = null;
                    designer.setBottomNavigationItemColor(bottomNavigationView);
                    switch (item.getItemId()) {
                        case R.id.home:
                            if (mainFragment == null) {
                                mainFragment = new MainFragment();
                                selectedFragment = mainFragment;
                            } else {
                                selectedFragment = mainFragment;
                            }
                            break;
                        case R.id.settings:
                            selectedFragment = new SettingsFragment();
                            break;
                    }
                    Drawable icon = item.getIcon();
                    designer.setIconColor(icon, designer.getPrimaryColorId());
                    bottomNavigationView.getMenu().findItem(item.getItemId()).setIcon(icon);
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.bottom_navigation_fragment, selectedFragment);
                    transaction.commit();
                    itemClicked = true;
                }
            }
        });
        bottomNavigationView.setSelectedItemId(R.id.home);
        Log.d("aaa", "onCreate: done");
    }

    @Override
    public void gotoView(int position) {

    }

}
