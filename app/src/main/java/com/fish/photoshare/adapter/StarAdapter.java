package com.fish.photoshare.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fish.photoshare.R;
import com.fish.photoshare.pojo.Record;
import com.google.android.material.imageview.ShapeableImageView;

public class StarAdapter extends RecyclerView.Adapter<StarAdapter.ViewHolder> {
    private Context context;
    private Record record;
    public StarAdapter(){}
    public StarAdapter(Context context, Record record) {
        this.context = context;
        this.record = record;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.star_item, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }
    @Override
    public int getItemCount() {
        return 0;
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ShapeableImageView PostImage;
        private TextView textTitle;
        private TextView textContent;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            PostImage = itemView.findViewById(R.id.PostImage);
            textTitle = itemView.findViewById(R.id.textTitle);
            textContent = itemView.findViewById(R.id.textContent);
        }
    }
}
