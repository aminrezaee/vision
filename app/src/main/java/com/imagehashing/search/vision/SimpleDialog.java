package com.imagehashing.search.vision;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.graphics.Typeface;

/**
 * Created by amin on 31/01/2017.
 */

public class SimpleDialog extends DialogFragment {

    protected Context context;
    protected Typeface font;
    protected Designer designer;

    public void initialize(Context context) {
        this.context = context;
        font = Typeface.createFromAsset(context.getAssets(), "fonts/IRANSansMobile.ttf");
        designer = new Designer(context);
    }
}
