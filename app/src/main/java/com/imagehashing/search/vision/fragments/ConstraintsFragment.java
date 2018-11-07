package com.imagehashing.search.vision.fragments;

import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.imagehashing.search.vision.R;

public class ConstraintsFragment extends Fragment {

    public ConstraintsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        container = (ViewGroup) inflater.inflate(R.layout.fragment_constraints, null);
        Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/IRANSansMobile_Light.ttf");
        TextView title = container.findViewById(R.id.title);
        title.setTypeface(font);
        return container;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }
}
