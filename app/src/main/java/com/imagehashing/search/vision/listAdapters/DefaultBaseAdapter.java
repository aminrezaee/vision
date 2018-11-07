package com.imagehashing.search.vision.listAdapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;

/**
 * Created by amin on 31/01/2017.
 */

public class DefaultBaseAdapter<Data> extends BaseAdapter {

    ArrayAdapter<Data> datas;
    protected Context context;
    protected Typeface font;

    public DefaultBaseAdapter(Context context) {
        this.context = context;
        font = Typeface.createFromAsset(context.getAssets(), "fonts/IRANSansMobile_Light.ttf");
    }

    @Override
    public int getCount() {
        return datas.getCount();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }
}
