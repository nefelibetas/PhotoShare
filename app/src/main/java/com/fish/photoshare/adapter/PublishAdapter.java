package com.fish.photoshare.adapter;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.fish.photoshare.R;
import com.google.android.material.imageview.ShapeableImageView;

import java.io.File;
import java.util.ArrayList;

public class PublishAdapter extends RecyclerView.Adapter<PublishAdapter.ViewHolder> {
    private Context context;
    private ArrayList<File> fileList;
    private ArrayList<Uri> uriList;
    public PublishAdapter() {}
    public PublishAdapter(Context mContext, ArrayList<File> files) {
        context = mContext;
        fileList = files;
        uriList = new ArrayList<>();
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_publis_item, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        File file = fileList.get(position);
        Uri uri = FileProvider.getUriForFile(context, "com.fish.photoshare.provider", file);
        holder.closeImage.setOnClickListener(v -> {
            if (position != RecyclerView.NO_POSITION) {
                fileList.remove(position);
                uriList.remove(position);
                notifyDataSetChanged();
            }
        });
        uriList.add(uri);
        new Handler(Looper.getMainLooper()).post(() -> Glide.with(context)
                .load(uri)
                .override(120, 120)
                .centerCrop()
                .into(holder.selectedImage));
    }
    @Override
    public int getItemCount() {
        return fileList.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ShapeableImageView selectedImage;
        private final ShapeableImageView closeImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            selectedImage = itemView.findViewById(R.id.selectedImage);
            closeImage = itemView.findViewById(R.id.closeButton);
        }
    }
}
