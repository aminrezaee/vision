package com.imagehashing.search.vision;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.imagehashing.search.vision.activities.MainActivity;

/**
 * Created by amin rezaee on 10/5/2015.
 */
public class ColorChoozerDialog extends SimpleDialog {

    SharedPreferences settings;

    public void setArgs(Context context) {
        initialize(context);
        settings = context.getSharedPreferences(context.getString(R.string.prefs), 0);
    }

    Dismisser dismisser = new Dismisser() {
        @Override
        public void dismiss() {
            ColorChoozerDialog.this.dismiss();
            Intent intent = new Intent(context, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        container = (ViewGroup) inflater.inflate(R.layout.color_choozer_dialog, container, false);
        TextView title = container.findViewById(R.id.title);
        title.setTypeface(font);
        title.setText(context.getString(R.string.theme));
        LinearLayout one = container.findViewById(R.id.one);
        LinearLayout two = container.findViewById(R.id.two);
        designer.setViewBackground(one, R.color.orange, true, BasicDesigner.CIRCLE, 0);
        designer.setViewBackground(two, R.color.blue, true, BasicDesigner.CIRCLE, 0);
        one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = settings.edit();
                editor.putInt(context.getString(R.string.toolbarColor), 0);
                editor.commit();
                dismisser.dismiss();
            }
        });

        two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = settings.edit();
                editor.putInt(context.getString(R.string.toolbarColor), 1);
                editor.commit();
                dismisser.dismiss();
            }
        });
        return container;
    }

}
