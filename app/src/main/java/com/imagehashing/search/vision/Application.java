package com.imagehashing.search.vision;

import android.content.SharedPreferences;

import com.imagehashing.search.vision.dataModels.Image;
import com.imagehashing.search.vision.dataModels.MyObjectBox;
import com.imagehashing.search.vision.dataModels.Pattern;
import com.imagehashing.search.vision.listAdapters.ImagesListAdapter;
import com.imagehashing.search.vision.methods.LocalitySensitiveHasing;

import java.io.File;
import java.io.IOException;

import io.objectbox.Box;
import io.objectbox.BoxStore;

public class Application extends android.app.Application {
    BoxStore boxStore;

    @Override
    public void onCreate() {
        super.onCreate();
        boxStore = MyObjectBox.builder().androidContext(Application.this).build();
        Box<Image> imageBox = boxStore.boxFor(Image.class);
        Box<Pattern> patternBox = boxStore.boxFor(Pattern.class);
        SharedPreferences settings = getSharedPreferences(getString(R.string.prefs), 0);
        boolean isFirstTime = settings.getBoolean(getString(R.string.first_time), true);
        if (isFirstTime) {
            try {
                new AssetsFileReader(getApplicationContext(), imageBox, patternBox).readFileAndInsertDatas();
            } catch (IOException e) {
                e.printStackTrace();
            }

            SharedPreferences.Editor editor = settings.edit();
            editor.putBoolean(getString(R.string.first_time), false);
            editor.commit();
        }
        boolean filesAdded = imageBox.getAll().size() > 0;
        if (!filesAdded) {
            File file = new File(ImagesListAdapter.imagesPath);
            String[] images = file.list();
            for(String imageName:images) {
                Image image = new Image();
                image.setName(imageName);
                imageBox.put(image);
            }
        }
//        LocalitySensitiveHasing localitySensitiveHasing = new LocalitySensitiveHasing(imageBox,getApplicationContext());
//        localitySensitiveHasing.LSH();

    }

    public BoxStore getBoxStore() {
        return boxStore;
    }
}
