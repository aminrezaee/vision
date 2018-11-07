package com.imagehashing.search.vision.activities;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.imagehashing.search.vision.Application;
import com.imagehashing.search.vision.R;
import com.imagehashing.search.vision.dataModels.Image;
import com.imagehashing.search.vision.listAdapters.ImagesListAdapter;

import java.util.Collections;
import java.util.List;

import io.objectbox.Box;

public class GalleryActivity extends AppCompatActivity {

    Box<Image> imageBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/IRANSansMobile_Light.ttf");
        TextView title = findViewById(R.id.title);
        title.setTypeface(font);
        RecyclerView list = findViewById(R.id.image_list);
        list.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 4);
        list.setLayoutManager(layoutManager);
        imageBox = ((Application) getApplication()).getBoxStore().boxFor(Image.class);
        List<Image> images = imageBox.getAll();
        ImagesListAdapter adapter = new ImagesListAdapter(this, images, true);
        list.setAdapter(adapter);
    }
}
