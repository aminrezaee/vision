package com.imagehashing.search.vision.fragments;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.imagehashing.search.vision.BasicDesigner;
import com.imagehashing.search.vision.Designer;
import com.imagehashing.search.vision.R;
import com.imagehashing.search.vision.dataModels.SettingItem;
import com.imagehashing.search.vision.listAdapters.SettingsListAdapter;

import java.util.ArrayList;

public class SettingsFragment extends Fragment {
    FragmentHandler fragmentHandler;
    private int position;
    FragmentManager manager;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.fragmentHandler = (FragmentHandler) activity;
    }

    public void setManager(FragmentManager manager) {
        this.manager = manager;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        container = (ViewGroup) inflater.inflate(R.layout.fragment_settings, null);
        Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/IRANSansMobile_Light.ttf");
        TextView title = container.findViewById(R.id.title);
        title.setTypeface(font);
        RecyclerView list = container.findViewById(R.id.list);
        list.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(), 1);
        list.setLayoutManager(layoutManager);
        ArrayList<SettingItem> settingItems = new ArrayList<>();
        settingItems.add(new SettingItem(getString(R.string.way_of_search), R.drawable.style));
        settingItems.add(new SettingItem(getString(R.string.theme), R.drawable.palette));
        SettingsListAdapter adapter = new SettingsListAdapter(getActivity(), settingItems, manager);
        list.setAdapter(adapter);

        return container;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        switch (position) {
            case 0:
                break;
            case 3:
                break;
            case 4:
                break;
        }
    }
}
