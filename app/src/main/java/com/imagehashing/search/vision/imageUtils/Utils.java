package com.imagehashing.search.vision.imageUtils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;

import com.imagehashing.search.vision.dataModels.Image;
import com.imagehashing.search.vision.listAdapters.ImagesListAdapter;

public class Utils {

    public static Bitmap toGrayscale(Bitmap bitmap) {
        int width, height;
        height = bitmap.getHeight();
        width = bitmap.getWidth();
        Bitmap bmpGrayscale = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bmpGrayscale);
        Paint paint = new Paint();
        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.setSaturation(0);
        ColorMatrixColorFilter filter = new ColorMatrixColorFilter(colorMatrix);
        paint.setColorFilter(filter);
        canvas.drawBitmap(bitmap, 0, 0, paint);
        return bmpGrayscale;
    }

    public static Bitmap getPrefferedImageSize(int width, int height, Image image) {
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        Bitmap bitmap = BitmapFactory.decodeFile(ImagesListAdapter.imagesPath + image.getName(), bmOptions);
        if (bitmap == null)
            return null;
        bitmap = Bitmap.createScaledBitmap(bitmap, width, height, true);
        return bitmap;
    }



}
