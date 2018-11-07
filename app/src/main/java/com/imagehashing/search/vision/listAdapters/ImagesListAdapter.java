package com.imagehashing.search.vision.listAdapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;

import com.bumptech.glide.Glide;
import com.bumptech.glide.MemoryCategory;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.imagehashing.search.vision.Designer;
import com.imagehashing.search.vision.R;
import com.imagehashing.search.vision.activities.ImageActivity;
import com.imagehashing.search.vision.activities.MainActivity;
import com.imagehashing.search.vision.dataModels.Image;

import java.util.List;

public class ImagesListAdapter extends DefaultRecyclerViewAdapter<ImagesListAdapter.ViewHolder> {

    private List<Image> images;
    private Designer designer;
    private int imageSize;
    public static String imagesPath = "/storage/emulated/0/DCIM/100MEDIA/Images/";
    boolean isDirect;
    public ImagesListAdapter(Context context, List<Image> images , boolean isDirect) {
        super(context);
        this.images = images;
        Glide.get(context).setMemoryCategory(MemoryCategory.HIGH);
        designer = new Designer(context);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager()
                .getDefaultDisplay()
                .getMetrics(displayMetrics);
        imageSize = (int) ((float) displayMetrics.widthPixels / 4.5);
        this.isDirect = isDirect;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_image, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.image.setScaleType(ImageView.ScaleType.CENTER_CROP);
        Glide.with(context).load(imagesPath + images.get(position).getName() + ".jpg").apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.RESOURCE).override(imageSize, imageSize))
                .into(holder.image);
        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isDirect) {
                    Intent intent = new Intent(context, MainActivity.class);
                    intent.putExtra(context.getString(R.string.id), images.get(position).getName());
                    context.startActivity(intent);
                } else {
                    showPopup(holder.image, position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;

        public ViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
        }
    }

    public void showPopup(View view, final int position) {
        PopupMenu popup = designer.getPopupMenuItem(view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.image_pop_up_list, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent;
                switch (item.getItemId()) {
                    case R.id.show_image:
                        intent = new Intent(context, ImageActivity.class);
                        intent.putExtra(context.getString(R.string.id), images.get(position).getName());
                        context.startActivity(intent);
                        break;
                    case R.id.search_image:
                        intent = new Intent(context, MainActivity.class);
                        intent.putExtra(context.getString(R.string.id), images.get(position).getName());
                        context.startActivity(intent);
                        break;
                }
                return false;
            }
        });
        popup.show();
    }

}
