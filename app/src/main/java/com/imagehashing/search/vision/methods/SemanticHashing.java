package com.imagehashing.search.vision.methods;

import com.imagehashing.search.vision.dataModels.Image;
import com.imagehashing.search.vision.dataModels.Pattern;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import Jama.Matrix;
import io.objectbox.Box;

public class SemanticHashing {
    private int dimentions;
    private List<String> allPatterns;
    private List<Image> images;
    private Box<Image> imageBox;

    public SemanticHashing(Box<Image> imageBox) {
        images = imageBox.getAll();
        this.imageBox = imageBox;
        allPatterns = new ArrayList<>();
        for (Image image : images) {
            for (Pattern pattern : image.patterns) {
                if (!allPatterns.contains(pattern.getName())) {
                    allPatterns.add(pattern.getName());
                }
            }
        }
        this.dimentions = allPatterns.size();
    }

    public List<Image> getSimilarImages(double radius, Image inputImage) {
        Matrix featureVector = getFeatureVector(inputImage);
        List<Image> similarImages = new ArrayList<>();
        for (Image image : images) {
            Matrix vector = getFeatureVector(image);
            double distance = getDistance(featureVector, vector);
            if (distance <= radius) {
                similarImages.add(image);
            }
        }
        return similarImages;
    }


    public void generateHashKeys() {


    }

    public double getDistance(Matrix featureVector1, Matrix featureVector2) {
        double distance = 0;
        Matrix difference = featureVector1.minus(featureVector2);
        for (int i = 0; i < difference.getRowDimension(); i++) {
            for (int j = 0; j < difference.getColumnDimension(); j++) {
                distance = distance + Math.abs(difference.get(i, j));
            }
        }
        return distance;
    }

    private Matrix getFeatureVector(Image image) {
        Matrix matrix = null;
        double[][] featureVector = new double[dimentions][2];
        String features = image.getSemanticHashKey();
        if (features == null) {
            features = "";
            List<Pattern> patterns = image.patterns;
            for (int i = 0; i < featureVector.length; i++) {
                featureVector[i][0] = 0;
                featureVector[i][1] = 0;
            }
            for (int i = 0; i < dimentions; i++) {
                String currentPattern = allPatterns.get(i);
                for (Pattern pattern : patterns) {
                    if (pattern.getName().equals(currentPattern)) {
                        featureVector[i][0] = 1;
//                    if (pattern.getCount() >= 3)
//                        featureVector[i][0] = 2;
                    }
                }
            }
            for (int i = 0; i < featureVector.length; i++) {
                features = features + featureVector[i][0];
            }
            image.setSemanticHashKey(features);
            imageBox.put(image);
        } else {
            for (int i = 0; i < features.length(); i++) {
                int number = Character.getNumericValue(features.charAt(i));
                featureVector[i][0] = number;
            }
        }
        matrix = new Matrix(featureVector, dimentions, 1);
        return matrix;
    }

}
