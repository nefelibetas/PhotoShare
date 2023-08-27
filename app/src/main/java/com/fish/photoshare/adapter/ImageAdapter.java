package com.fish.photoshare.adapter;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.fish.photoshare.R;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {
    private Context context;
    private ArrayList<String> imageUrlList;

    public ImageAdapter(Context context, ArrayList<String> list) {
        this.context = context;
        this.imageUrlList = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_post_image_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Uri uri = Uri.parse(imageUrlList.get(position));
        new Handler(Looper.getMainLooper()).post(() -> Glide.with(context)
                .load(uri)
                .centerCrop()
                .into(holder.image));
    }

    @Override
    public int getItemCount() {
        return imageUrlList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ShapeableImageView image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.PostImageItem);
        }
    }
}
