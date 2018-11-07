package com.imagehashing.search.vision.listAdapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by amin on 31/01/2017.
 */

public abstract class DefaultRecyclerViewAdapter<ViewHolder extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<ViewHolder> {
    protected Context context;
    Typeface font;
    public DefaultRecyclerViewAdapter(Context context) {
        this.context = context;
        font = Typeface.createFromAsset(context.getAssets(), "fonts/IRANSansMobile_Light.ttf");
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }
}
