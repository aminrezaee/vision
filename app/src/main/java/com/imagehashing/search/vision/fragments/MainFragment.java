package com.imagehashing.search.vision.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.imagehashing.search.vision.Application;
import com.imagehashing.search.vision.BasicDesigner;
import com.imagehashing.search.vision.Designer;
import com.imagehashing.search.vision.R;
import com.imagehashing.search.vision.activities.GalleryActivity;
import com.imagehashing.search.vision.activities.ImageActivity;
import com.imagehashing.search.vision.dataModels.Constraint;
import com.imagehashing.search.vision.dataModels.Image;
import com.imagehashing.search.vision.dataModels.Image_;
import com.imagehashing.search.vision.dataModels.Pattern;
import com.imagehashing.search.vision.listAdapters.ImagesListAdapter;
import com.imagehashing.search.vision.methods.LocalitySensitiveHasing;
import com.imagehashing.search.vision.methods.SemanticHashing;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.objectbox.Box;
import io.objectbox.query.Query;

import static android.app.Activity.RESULT_OK;


public class MainFragment extends Fragment {
    FragmentHandler fragmentHandler;
    private int position;
    ImageView imageButton;
    ImageView image;
    RelativeLayout searchLayout;
    RelativeLayout imageLayout;
    Box<Image> imageBox;
    Box<Constraint> constraintBox;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.fragmentHandler = (FragmentHandler) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/IRANSansMobile_Light.ttf");
        Typeface fontBold = Typeface.createFromAsset(getActivity().getAssets(), "fonts/IRANSansMobile.ttf");
        container = (ViewGroup) inflater.inflate(R.layout.fragment_main, null);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager()
                .getDefaultDisplay()
                .getMetrics(displayMetrics);
        Designer designer = new Designer(getActivity());
        imageBox = ((Application) getActivity().getApplication()).getBoxStore().boxFor(Image.class);
        constraintBox = ((Application) getActivity().getApplication()).getBoxStore().boxFor(Constraint.class);
        imageButton = container.findViewById(R.id.image_button);
        CardView searchImagesLayout = container.findViewById(R.id.image_button_layout);
        searchLayout = container.findViewById(R.id.search_layout);
        imageLayout = container.findViewById(R.id.image_layout);
        image = container.findViewById(R.id.search_image);
        Drawable drawable = ContextCompat.getDrawable(getActivity(), R.drawable.image);
        designer.setIconColor(drawable, designer.getPrimaryColorId());
        imageButton.setImageDrawable(drawable);
        designer.setViewBackground(searchImagesLayout, R.color.white, true, BasicDesigner.CIRCLE, 0);

        Bundle extras = getActivity().getIntent().getExtras();
        String imageId = "";
        if (extras != null)
            imageId = extras.getString(getString(R.string.id), "");
        if (!imageId.equals("")) {
            searchLayout.setVisibility(View.GONE);
            imageLayout.setVisibility(View.VISIBLE);
            int imageSize = (int) ((float) displayMetrics.widthPixels / 3);
            Glide.with(getActivity()).load(ImagesListAdapter.imagesPath + imageId + ".jpg").apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.RESOURCE).override(imageSize, imageSize))
                    .into(image);
            image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), GalleryActivity.class);
                    startActivity(intent);
                }
            });
            Image image = imageBox.query().equal(Image_.name, imageId).build().find().get(0);
            TextView features = container.findViewById(R.id.features);
            features.setTypeface(font);
            String text = "Features: \n";
            for (int i = 0; i < image.patterns.size(); i++) {
                Pattern pattern = image.patterns.get(i);
                text = text + (i + 1) + "." + pattern.getName() + ":" + "\t\t" + "count:" + "\t\t" + pattern.getCount() + "\n";
            }
            features.setText(text);
        } else {
            searchLayout.setVisibility(View.VISIBLE);
            imageLayout.setVisibility(View.GONE);

            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LocalitySensitiveHasing localitySensitiveHasing = new LocalitySensitiveHasing(imageBox, getContext(), constraintBox, 320, 240);
                    localitySensitiveHasing.LSH();
                    Intent intent = new Intent(getContext(), GalleryActivity.class);
                    startActivity(intent);
                }
            });
            TextView searchBox = container.findViewById(R.id.search_box);
            searchBox.setTypeface(font);
        }

        TextView results = container.findViewById(R.id.results);
        results.setTypeface(fontBold);
        TextView featuresTitle = container.findViewById(R.id.features_title);
        featuresTitle.setTypeface(fontBold);
        TextView features = container.findViewById(R.id.features);
        features.setTypeface(font);

        RecyclerView list = container.findViewById(R.id.image_list);
        list.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(), 4);
        list.setLayoutManager(layoutManager);
        List<Image> images = null;
        if (imageId.equals("")) {
            images = imageBox.getAll();
//            Collections.shuffle(images);
            images = images.subList(0, 20);
        } else {
            Image image = imageBox.query().equal(Image_.name, imageId).build().find().get(0);
            SharedPreferences settings = getActivity().getSharedPreferences(getString(R.string.prefs), 0);
            int wayOfSearch = settings.getInt(getString(R.string.way_of_search), 0);
            switch (wayOfSearch) {
                case 0:
                    LocalitySensitiveHasing localitySensitiveHasing = new LocalitySensitiveHasing(imageBox, getContext(), constraintBox, 320, 240);
                    images = localitySensitiveHasing.getSimilarImages(image);
                    break;
                case 1:
                    SemanticHashing semanticHashing = new SemanticHashing(imageBox);
                    images = semanticHashing.getSimilarImages(1, image);
                    break;
                case 2:
                    ////
                    break;
            }
        }
        ImagesListAdapter adapter = new ImagesListAdapter(getActivity(), images, false);
        list.setAdapter(adapter);
        return container;
    }

    public void setPosition(int position) {
        this.position = position;
    }

}
