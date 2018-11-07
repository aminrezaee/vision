package com.imagehashing.search.vision;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class SearchAlgorithmDialog extends SimpleDialog {
    SharedPreferences settings;
    int first = 0;

    public void setArgs(Context context) {
        initialize(context);
        settings = context.getSharedPreferences(context.getString(R.string.prefs), 0);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        container = (ViewGroup) inflater.inflate(R.layout.search_algorithm_dialog, container, false);
        TextView title = container.findViewById(R.id.title);
        title.setTypeface(font);
        title.setText(context.getString(R.string.way_of_search));

        Spinner spinner = container.findViewById(R.id.spinner);
        final String[] algorithms = {"Locality Sensitive Hashing", "Semantic Hashing", "Boosting"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, R.layout.spinner_text_view, algorithms) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                ((TextView) v.findViewById(R.id.text_of_spinner)).setTypeface(font);
                if (position == getCount()) {
                    ((TextView) v.findViewById(R.id.text_of_spinner)).setText("");
                    ((TextView) v.findViewById(R.id.text_of_spinner)).setHint(getItem(getCount())); //"Hint to be displayed"
                    ((TextView) v.findViewById(R.id.text_of_spinner)).setHintTextColor(getContext().getResources().getColor(R.color.black));
                }
                return v;
            }
        };
        spinner.setAdapter(adapter);
        int wayOfSearch = settings.getInt(context.getString(R.string.way_of_search), 0);
        spinner.setSelection(wayOfSearch);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (first > 0) {
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putInt(context.getString(R.string.way_of_search), position);
                    editor.commit();
                    Toast.makeText(context, context.getString(R.string.settings_saved), Toast.LENGTH_SHORT).show();
                    dismiss();
                } else {
                    first++;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        return container;
    }

}
