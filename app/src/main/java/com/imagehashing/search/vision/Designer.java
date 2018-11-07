package com.imagehashing.search.vision;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.imagehashing.search.vision.views.CustomTypefaceSpan;

import java.util.Arrays;

/**
 * Created by amin rezaee on 11/12/2015.
 */
public class Designer extends BasicDesigner {

    public Designer(Context context) {
        super(context);
    }

    public PopupMenu getPopupMenuItem(View view) {
        Context wrapper = new ContextThemeWrapper(context, R.style.pupup_menu_style);
        return new PopupMenu(wrapper, view);
    }

    public void setToolbarColor(Toolbar toolbar) {
        toolbar.setBackgroundColor(ContextCompat.getColor(context, getPrimaryColorId()));
    }

    public void setFloatButtonColor(FloatingActionButton button, int drawableId) {
        button.setBackgroundTintList(ContextCompat.getColorStateList(context, drawableId));
    }

    public void setViewBackground(View view, int drawableId, boolean isSelector, int shape, int radius) {
        Drawable drawable;
        if (isSelector) {
            drawable = getAdaptiveBackgroundDrawable(lightGraySelector, radius, shape, drawableId);
            view.setClickable(true);
        } else {
            drawable = getColoredShape(radius, shape, drawableId);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            view.setBackground(drawable);
        } else {
            view.setBackgroundDrawable(drawable);
        }
    }

    public void setCardSelector(CardView card, int drawableId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            card.setBackground(getAdaptiveBackgroundDrawable(lightGraySelector, 10, 0, drawableId));
        } else {
            card.setCardBackgroundColor(ContextCompat.getColorStateList(context, drawableId));
        }
    }

    public void setTextColor(TextView textView, int drawableId, boolean isSelector) {
        if (isSelector) {
            textView.setTextColor(ContextCompat.getColorStateList(context, drawableId));
        } else {
            textView.setTextColor(ContextCompat.getColor(context, drawableId));
        }
    }

    public void setViewBorder(View view, int backgroundColorId, int borderColorId, int shape, int radius) {
        GradientDrawable shapedrawable = new GradientDrawable();
        if (shape == RECTANGLE) {
            float[] radiuses = new float[8];
            int radiusInDp = convertPixToDip(context, radius);
            Arrays.fill(radiuses, radiusInDp);
            shapedrawable.setShape(GradientDrawable.RECTANGLE);
            shapedrawable.setCornerRadii(radiuses);
        } else {
            shapedrawable.setShape(GradientDrawable.OVAL);
        }
        shapedrawable.setColor(ContextCompat.getColor(context, backgroundColorId));
        shapedrawable.setStroke(2, ContextCompat.getColor(context, borderColorId));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            view.setBackground(shapedrawable);
        } else {
            view.setBackgroundDrawable(shapedrawable);
        }
    }

    public void setIconColor(Drawable drawable, int colorId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            DrawableCompat.setTint(drawable, ContextCompat.getColor(context, colorId));

        } else {
            drawable.mutate().setColorFilter(ContextCompat.getColor(context, colorId), PorterDuff.Mode.SRC_IN);
        }
    }

    public void setBottomNavigationItemColor(BottomNavigationView bottomNavigationView) {

        ColorStateList colorStateList = new ColorStateList(new int[][]{
                new int[]{-android.R.attr.state_enabled}, // disabled
                new int[]{android.R.attr.state_checked}, // checked
                new int[]{android.R.attr.state_pressed},  // pressed
                new int[]{}  // pressed
        },
                new int[]{
                        ContextCompat.getColor(context, R.color.dark_gray),
                        ContextCompat.getColor(context, getPrimaryColorId()),
                        ContextCompat.getColor(context, getPrimaryColorId()),
                        ContextCompat.getColor(context, R.color.dark_gray)
                });

        bottomNavigationView.setItemIconTintList(colorStateList);
        bottomNavigationView.setItemTextColor(ContextCompat.getColorStateList(context, R.color.dark_gray));
        Menu menu = bottomNavigationView.getMenu();
        for (int i = 0; i < menu.size(); i++) {
            MenuItem menuItem = menu.getItem(i);
            //for applying a font to subMenu ...
            SubMenu subMenu = menuItem.getSubMenu();
            if (subMenu != null && subMenu.size() > 0) {
                for (int j = 0; j < subMenu.size(); j++) {
                    MenuItem subMenuItem = subMenu.getItem(j);
                    applyFontToMenuItem(subMenuItem);
                }
            }
            //the method we have create in activity
            applyFontToMenuItem(menuItem);
        }
    }

    private void applyFontToMenuItem(MenuItem menuItem) {
        Typeface font = Typeface.createFromAsset(context.getAssets(), "fonts/IRANSansMobile_Light.ttf");
        SpannableString mNewTitle = new SpannableString(menuItem.getTitle());
        mNewTitle.setSpan(new CustomTypefaceSpan("", font), 0, mNewTitle.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        menuItem.setTitle(mNewTitle);
    }
}