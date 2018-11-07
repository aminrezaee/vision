package com.imagehashing.search.vision.listAdapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

public class ConstraintsListAdapter extends DefaultRecyclerViewAdapter<ConstraintsListAdapter.ViewHolder> {

    public ConstraintsListAdapter(Context context) {
        super(context);
    }

    @NonNull
    @Override
    public ConstraintsListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ConstraintsListAdapter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
