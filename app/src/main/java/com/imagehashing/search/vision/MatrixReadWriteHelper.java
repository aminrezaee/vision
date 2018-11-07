package com.imagehashing.search.vision;

import android.content.Context;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import Jama.Matrix;

public class MatrixReadWriteHelper {

    Context context;
    public static String path = "/data/data/com.imagehashing.search.vision/files/";

    public MatrixReadWriteHelper(Context context) {
        this.context = context;
    }

    public boolean fileExists(String fileName) {
        File file = new File(path + fileName);
        return file.exists();
    }

    public void WriteMatrix(Matrix matrix, String fileName) throws IOException {
        File file = context.getFileStreamPath(fileName);
        FileWriter fileWriter = new FileWriter(file);
        BufferedWriter writer = new BufferedWriter(fileWriter);
        for (int i = 0; i < matrix.getRowDimension(); i++) {
            String line = extractLineFromNumbers(matrix, i);
            writer.write(line);
            writer.newLine();
        }
        writer.close();
        fileWriter.close();
    }

    public Matrix readMatrix(String fileName, int dimention) throws IOException {
        Matrix matrix = new Matrix(dimention, dimention);
        File file = context.getFileStreamPath(fileName);
        FileReader fileReader = new FileReader(file);
        BufferedReader reader = new BufferedReader(fileReader);
        String line;
        int i = 0;
        while ((line = reader.readLine()) != null) {
            extractNumbersFromLine(line, matrix, i);
            i++;
        }
        reader.close();
        fileReader.close();
        return matrix;
    }

    private void extractNumbersFromLine(String line, Matrix matrix, int i) {
        String[] numbers = line.replace("\n", "").split(" ");
        for (int j = 0; j < matrix.getColumnDimension(); j++) {
            matrix.set(i, j, Double.parseDouble(numbers[j]));
        }
    }


    private String extractLineFromNumbers(Matrix matrix, int i) {
        String line = "";
        for (int j = 0; j < matrix.getColumnDimension(); j++) {
            line = line + matrix.get(i, j) + " ";
        }
        return line;
    }
}
