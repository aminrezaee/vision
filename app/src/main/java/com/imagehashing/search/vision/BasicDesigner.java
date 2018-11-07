package com.imagehashing.search.vision;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.StateListDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.graphics.drawable.shapes.RoundRectShape;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.util.TypedValue;

import java.util.Arrays;

/**
 * Created by amin on 05/03/2017.
 */

public class BasicDesigner {

    protected Context context;
    protected SharedPreferences settings;
    protected int colorNumber;
    public static int[] colorIds = new int[]{R.color.orange, R.color.blue};
    public static int[] colorIds_light = new int[]{R.color.light_pink, R.color.light_orange, R.color.light_yellow, R.color.light_green,
            R.color.light_blue, R.color.light_purple};
//    public static int[] textSelectorColors = {R.color.text_selector_black_to_red, R.color.text_selector_black_to_orange,
//            R.color.text_selector_black_to_yellow, R.color.text_selector_black_to_green,
//            R.color.text_selector_black_to_blue, R.color.text_selector_black_to_purple};
    int lightGraySelector = R.color.light_gray;
    public static final int RECTANGLE = 0;
    public static final int CIRCLE = 1;

    public BasicDesigner(Context context) {
        this.context = context;
        settings = context.getSharedPreferences(context.getString(R.string.prefs), 0);
        colorNumber = settings.getInt(context.getString(R.string.toolbarColor), 1);
    }

    public void updateColorNumber() {
        colorNumber = settings.getInt(context.getString(R.string.toolbarColor), 1);
    }

    public int getLightGraySelector() {
        return lightGraySelector;
    }

    public int getPrimaryColorId() {
        return colorIds[colorNumber];
    }

    public int getPrimaryColorIdLight() {
        return colorIds_light[colorNumber];
    }

    public int getAcceptColor() {
        return R.color.accept_color;
    }

    public int getSecondaryColorId() {
        return colorIds[(colorNumber + 1) % colorIds.length];
    }

    public int getSecondaryColorIdLight() {
        return colorIds_light[(colorNumber + 1) % colorIds_light.length];
    }

//    public int getPrimaryTextColorSelectorId() {
//        return textSelectorColors[colorNumber];
//    }
//
//    public int getSecondaryTextColorSelectorId() {
//        return textSelectorColors[(colorNumber + 1) % textSelectorColors.length];
//    }

    public static int convertDiptoPix(Context context, float dip) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, context.getResources().getDisplayMetrics());
    }

    public static int convertPixToDip(Context context, float px) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, px
                , context.getResources().getDisplayMetrics());
    }

//    public static Bitmap getFontBitmap(Context context, String text, float fontSizeSP, int typeFaceNumber, int color) {
//
//        int fontSizePX = convertDiptoPix(context, fontSizeSP);
//        int pad = (fontSizePX / 9);
//        Paint paint = new Paint();
//        SharedPreferences settings = context.getSharedPreferences(
//                context.getString(R.string.shared_preferences_key1), 0);
//        String[] farsiFonts = context.getResources().getStringArray(R.array.farsi_fonts);
//        Typeface typeface;
//        typeface = Typeface.createFromAsset(context.getAssets(), "fonts/IRANSansMobile.ttf");
//        switch (typeFaceNumber) {
//            case 0:
//                typeface = Typeface.createFromAsset(context.getAssets(),
//                        context.getString(R.string.font_folder) +
//                                farsiFonts[settings.getInt(context.getString(R.string.farsiFontMeaning), 0)]);
//                break;
//            case 1:
//                typeface = Typeface.createFromAsset(context.getAssets(),
//                        context.getString(R.string.font_folder) +
//                                farsiFonts[settings.getInt(context.getString(R.string.farsiFontWord), 0)]);
//                break;
//            case 2:
//                typeface = Typeface.createFromAsset(context.getAssets(),
//                        context.getString(R.string.font_folder) +
//                                farsiFonts[settings.getInt(context.getString(R.string.arabiFont), 0)]);
//                break;
//            case 3:
//                typeface = Typeface.createFromAsset(context.getAssets(),
//                        context.getString(R.string.font_folder) +
//                                farsiFonts[settings.getInt(context.getString(R.string.englishFont), 0)]);
//                break;
//            case 4:
//                typeface = Typeface.createFromAsset(context.getAssets(), "fonts/IRANSansMobile.ttf");
//                break;
//            case 5:
//                typeface = Typeface.createFromAsset(context.getAssets(), "fonts/arial.ttf");
//                break;
//        }
//
//        paint.setAntiAlias(true);
//        paint.setTypeface(typeface);
//        if (color == 0)
//
//        {
//            paint.setColor(context.getResources().getColor(R.color.white));
//        } else if (color == 1)
//
//        {
//            paint.setColor(context.getResources().getColor(R.color.black));
//        }
//
//        paint.setTextSize(fontSizePX);
//
//        int textWidth = (int) (paint.measureText(text) + pad * 2);
//        int height = (int) (fontSizePX / 0.75);
//        Bitmap bitmap = Bitmap.createBitmap(textWidth, height, Bitmap.Config.ARGB_4444);
//        Canvas canvas = new Canvas(bitmap);
//        canvas.drawText(text, (float) pad, fontSizePX, paint);
//        return bitmap;
//    }

    public Drawable getAdaptiveBackgroundDrawable(int pressedColorId, int radius, int shape, int normalColorId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return new RippleDrawable(ContextCompat.getColorStateList(context, lightGraySelector),
                    getColoredShape(radius, shape, normalColorId), null);
        } else {
            return getStateListDrawable(getColoredShape(radius, shape, normalColorId), getColoredShape(radius, shape, pressedColorId));
        }
    }

    public ShapeDrawable getColoredShape(int radius, int shape, int colorId) {
        ShapeDrawable shapeDrawable = null;
        switch (shape) {
            case 0:
                float[] radiuses = new float[8];
                int radiusInDp = convertPixToDip(context, radius);
                Arrays.fill(radiuses, radiusInDp);
                RoundRectShape roundRectShape = new RoundRectShape(radiuses, null, null);
                shapeDrawable = new ShapeDrawable(roundRectShape);
                break;
            case 1:
                OvalShape ovalShape = new OvalShape();
                shapeDrawable = new ShapeDrawable(ovalShape);
                break;
        }
        shapeDrawable.getPaint().setColor(Color.parseColor(context.getResources().getString(colorId)));
        return shapeDrawable;
    }

    public Drawable getStateListDrawable(Drawable normal, Drawable selected) {
        StateListDrawable states = new StateListDrawable();
        states.addState(new int[]{android.R.attr.state_pressed}, selected);
        states.addState(new int[]{android.R.attr.state_focused}, selected);
        states.addState(new int[]{android.R.attr.state_activated}, selected);
        states.addState(new int[]{}, normal);
        return states;
    }
}
