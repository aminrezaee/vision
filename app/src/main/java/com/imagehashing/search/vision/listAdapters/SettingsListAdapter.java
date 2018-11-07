package com.imagehashing.search.vision.listAdapters;

import android.app.FragmentManager;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.imagehashing.search.vision.BasicDesigner;
import com.imagehashing.search.vision.ColorChoozerDialog;
import com.imagehashing.search.vision.Designer;
import com.imagehashing.search.vision.R;
import com.imagehashing.search.vision.SearchAlgorithmDialog;
import com.imagehashing.search.vision.dataModels.SettingItem;

import java.util.ArrayList;

public class SettingsListAdapter extends DefaultRecyclerViewAdapter<SettingsListAdapter.ViewHolder> {

    ArrayList<SettingItem> settingItems;
    Designer designer;
    FragmentManager fragmentManager;

    public SettingsListAdapter(Context context, ArrayList<SettingItem> settingItems, FragmentManager fragmentManager) {
        super(context);
        this.settingItems = settingItems;
        designer = new Designer(context);
        this.fragmentManager = fragmentManager;
    }

    @NonNull
    @Override
    public SettingsListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_setting, parent, false);
        return new SettingsListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SettingsListAdapter.ViewHolder holder, int position) {
        holder.text.setTypeface(font);
        designer.setViewBackground(holder.itemView, R.color.white, true, BasicDesigner.RECTANGLE, 0);
        holder.text.setText(settingItems.get(position).getText());
        Drawable icon = ContextCompat.getDrawable(context, settingItems.get(position).getImageId());
        designer.setIconColor(icon, designer.getPrimaryColorId());
        holder.image.setImageDrawable(icon);
        switch (position) {
            case 0:
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SearchAlgorithmDialog dialog = new SearchAlgorithmDialog();
                        dialog.setArgs(context);
                        dialog.show(fragmentManager, "title");
                    }
                });
                break;
            case 1:
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ColorChoozerDialog dialog = new ColorChoozerDialog();
                        dialog.setArgs(context);
                        dialog.show(fragmentManager, "title");
                    }
                });
                break;
        }
    }

    @Override
    public int getItemCount() {
        return settingItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView text;

        public ViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.icon);
            text = itemView.findViewById(R.id.text);
        }
    }
}
