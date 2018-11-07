package com.imagehashing.search.vision.methods;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.imagehashing.search.vision.Cholesky;
import com.imagehashing.search.vision.MatrixReadWriteHelper;
import com.imagehashing.search.vision.dataModels.Constraint;
import com.imagehashing.search.vision.dataModels.Image;
import com.imagehashing.search.vision.dataModels.Image_;
import com.imagehashing.search.vision.dataModels.Pattern;
import com.imagehashing.search.vision.imageUtils.Utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;
import java.util.List;

import Jama.CholeskyDecomposition;
import Jama.Matrix;
import io.objectbox.Box;

public class LocalitySensitiveHasing {

    private int dimentions;
    private int imageWidth;
    private int imageHeight;
    private List<String> allPatterns;
    private Box<Image> imageBox;
    private Matrix AMatrix;
    private List<Image> images;
    Context context;
    Box<Constraint> constraintBox;

    public LocalitySensitiveHasing(Box<Image> imageBox, Context context, Box<Constraint> constraintBox, int imageHeight, int imageWidth) {
        this.imageBox = imageBox;
        this.images = imageBox.getAll().subList(0, 10);
        this.constraintBox = constraintBox;
        this.context = context;
        this.imageHeight = imageHeight;
        this.imageWidth = imageWidth;
        this.dimentions = imageWidth;
        AMatrix = Matrix.identity(dimentions, dimentions);
    }

    public void setAMatrix() {
        MatrixReadWriteHelper matrixReadWriteHelper = new MatrixReadWriteHelper(context);
        boolean fileExists = matrixReadWriteHelper.fileExists("AMatrix.txt");
        if (true) { //(!fileExists) {
            double lambda = 0;
            double gama = 0.2;
            double u = 0.1;
            double l = 1;
            List<Constraint> constraints = constraintBox.getAll();
            for (Constraint constraint : constraints) {
                boolean isSimilar = constraint.isSimilar();
                Matrix jMatrix = getImageVector(constraint.getFirstImage().getTarget());
                Matrix xIMatrix = getImageVector(constraint.getSecondImage().getTarget());
                double similarityThreshold = l;
                Matrix difference = xIMatrix.minus(jMatrix);
                Matrix transpose = difference.transpose();
                int similarityMeasured = transpose.times(AMatrix).times(difference);
                double similarity = -1;
                if (isSimilar)
                    similarity = 1;

                double alpha = Math.min(lambda,
                        (similarity / 2) * ((1.0 / P.get(0, 0)) - gama / similarityThreshold));
                double beta = (similarity * alpha) / (1.0 - similarity * alpha * P.get(0, 0));
                if (isSimilar) {
                    u = (similarityThreshold * gama) / (gama + similarityThreshold * alpha * similarityThreshold);
                } else {
                    l = (similarityThreshold * gama) / (gama + similarityThreshold * alpha * similarityThreshold);
                }
                lambda = lambda - alpha;

                Matrix term = AMatrix.times(beta).times(difference).times(transpose).times(AMatrix);
                AMatrix = AMatrix.plus(term);
            }
            try {
                matrixReadWriteHelper.WriteMatrix(AMatrix, "AMatrix.txt");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                AMatrix = matrixReadWriteHelper.readMatrix("AMatrix.txt", dimentions);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Cholesky decomposition = new Cholesky(AMatrix);
    }


    public boolean CholeskyDecomposition(Matrix var1) {
        double[][] var2 = var1.getArray();
        int n = var1.getRowDimension();
        double[][] L = new double[n][n];
        boolean isspd = var1.getColumnDimension() == n;

        for (int var3 = 0; var3 < n; ++var3) {
            double[] var4 = L[var3];
            double var5 = 0.0D;

            int var7;
            for (var7 = 0; var7 < var3; ++var7) {
                double[] var8 = L[var7];
                double var9 = 0.0D;

                for (int var11 = 0; var11 < var7; ++var11) {
                    var9 += var8[var11] * var4[var11];
                }

                var4[var7] = var9 = (var2[var3][var7] - var9) / L[var7][var7];
                var5 += var9 * var9;
                isspd = (var2[var7][var3] == var2[var3][var7]) && isspd;
                if (!isspd) {
                    Log.d("aaa", "CholeskyDecomposition " + (var2[var7][var3]));
                    return false;
                }
            }

            var5 = var2[var3][var3] - var5;
            isspd &= var5 > 0.0D;
            L[var3][var3] = Math.sqrt(Math.max(var5, 0.0D));

            for (var7 = var3 + 1; var7 < n; ++var7) {
                L[var3][var7] = 0.0D;
            }
        }
        return isspd;
    }

    public double getHammingDistance(Matrix featureVector1, Matrix featureVector2) {
        double distance = 0;
        Matrix difference = featureVector1.minus(featureVector2);
        for (int i = 0; i < difference.getRowDimension(); i++) {
            for (int j = 0; j < difference.getColumnDimension(); j++) {
                distance = distance + Math.abs(difference.get(i, j));
            }
        }
        return distance;
    }

    public void LSH() {
        boolean isDoneBefore = images.get(0).getLSHHashKey() != null;
        if (true) {//(!isDoneBefore) {
            MatrixReadWriteHelper readWriteHelper = new MatrixReadWriteHelper(context);
            Matrix GMatrix = null;
            if (true) {//(!readWriteHelper.fileExists("GMatrix.txt")) {
                setAMatrix();
                GMatrix = AMatrix.chol().getL();
                try {
                    readWriteHelper.WriteMatrix(GMatrix, "GMatrix.txt");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    GMatrix = readWriteHelper.readMatrix("GMatrix.txt", dimentions);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            for (Image image : images) {
                image.setLSHHashKey("");
                image.setBits(new BitSet());
            }
            ArrayList<Matrix> matrices = new ArrayList<>();
            for (int j = 0; j < images.size(); j++) {
                Image image = images.get(j);
                Matrix xMatrix = getImageVector(image);
                matrices.add(xMatrix);
            }
            for (int i = 0; i < 400; i++) {
                Matrix random = Matrix.random(dimentions, 1);
                for (int j = 0; j < images.size(); j++) {
                    Image image = images.get(j);
                    image.getBits().set(i, getHashKey(GMatrix, matrices.get(j), random));
                }
            }
            for (Image image : images) {
                try {
                    image.setLSHHashKey(new String(image.getBits().toByteArray(), "UTF-8"));
                    imageBox.put(image);

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
            Log.d("aaa", "LSH: done");
        } else {
            Log.d("aaa", "LSH: codes were generated!");
        }
    }

    public boolean getHashKey(Matrix GMatrix, Matrix x, Matrix random) {
        Matrix result = random.transpose().times(GMatrix).times(x);
        return result.get(0, 0) > 0;
    }

    private Matrix getImageVector(Image image) {
        Matrix matrix = null;
        double[][] featureVector = new double[imageWidth][imageHeight];
        Bitmap bitmap = Utils.toGrayscale(Utils.getPrefferedImageSize(imageWidth, imageHeight, image));
        for (int i = 0; i < imageWidth; i++) {
            for (int j = 0; j < imageHeight; j++) {
                featureVector[i][j] = (double) bitmap.getPixel(i, j);
            }
        }
        matrix = new Matrix(featureVector, imageWidth, imageHeight);
        return matrix;
    }

    public List<Image> getSimilarImages(Image image) {
        List<Image> similarImages = imageBox.query().equal(Image_.LSHHashKey, image.getLSHHashKey()).build().find();
        return similarImages;
    }
}
