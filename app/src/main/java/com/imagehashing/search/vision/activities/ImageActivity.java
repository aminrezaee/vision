package com.imagehashing.search.vision.activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.imagehashing.search.vision.Application;
import com.imagehashing.search.vision.R;
import com.imagehashing.search.vision.dataModels.Image;
import com.imagehashing.search.vision.dataModels.Image_;
import com.imagehashing.search.vision.dataModels.Pattern;
import com.imagehashing.search.vision.listAdapters.ImagesListAdapter;

import io.objectbox.Box;
import io.objectbox.query.QueryBuilder;

public class ImageActivity extends AppCompatActivity {
    Box<Image> imageBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager()
                .getDefaultDisplay()
                .getMetrics(displayMetrics);
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/IRANSansMobile_Light.ttf");
        Intent intent = getIntent();
        String imageId = intent.getExtras().getString(getString(R.string.id));
        ImageView imageView = findViewById(R.id.image);
        imageBox = ((Application) getApplication()).getBoxStore().boxFor(Image.class);
        QueryBuilder<Image> builder = imageBox.query();
        builder.equal(Image_.name, imageId);
        Image image = builder.build().find().get(0);
        TextView features = findViewById(R.id.features);
        features.setTypeface(font);
        String text = "Features: \n";
        for (int i = 0; i < image.patterns.size(); i++) {
            Pattern pattern = image.patterns.get(i);
            text = text + (i + 1) + "." + pattern.getName() + ":" + "\t\t" + "count:" + "\t\t" + pattern.getCount() + "\n";
        }
        features.setText(text);
        Glide.with(this).load(ImagesListAdapter.imagesPath + imageId + ".jpg").
                apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.RESOURCE).
                        override(displayMetrics.widthPixels / 2, displayMetrics.heightPixels / 2)).into(imageView);
    }
}
