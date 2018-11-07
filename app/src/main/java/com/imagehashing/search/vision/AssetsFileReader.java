package com.imagehashing.search.vision;

import android.content.Context;

import com.imagehashing.search.vision.dataModels.Image;
import com.imagehashing.search.vision.dataModels.Pattern;
import com.imagehashing.search.vision.dataModels.Pattern_;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import io.objectbox.Box;
import io.objectbox.query.QueryBuilder;

public class AssetsFileReader {
    Context context;
    Box<Image> imageBox;
    Box<Pattern> patternBox;

    public AssetsFileReader(Context context, Box<Image> imageBox, Box<Pattern> patternBox) {
        this.context = context;
        this.imageBox = imageBox;
        this.patternBox = patternBox;
    }

    public void readFileAndInsertDatas() throws IOException {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(context.getAssets().open("out")));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String line;
        String name = null;
        ArrayList<Pattern> labels = new ArrayList<>();
        while ((line = reader.readLine()) != null) {
            if (line.contains("./data/Images/")) {
                if ((labels.size() > 0)) {
                    // adding image:
                    Image image = new Image();
                    image.setName(name);
                    image.patterns.addAll(labels);
                    imageBox.put(image);
                }
                // next image:
                name = line.split("/data/Images/")[1].split(".jpg")[0];
                labels = new ArrayList<>();
            } else if (line.contains("%")) {
                if (Integer.parseInt((line.split(":")[1]).replace("%", "").replace(" ", "")) >= 70) {
                    String object = line.split(":")[0];
                    boolean exists = false;
                    for (Pattern label : labels) {
                        if (label.getName().equals(object)) {
                            exists = true;
                            label.setCount(label.getCount() + 1);
                        }
                    }
                    if (!exists) {
                        labels.add(new Pattern(object, 1));
                    }
                }
            }
        }

    }
}