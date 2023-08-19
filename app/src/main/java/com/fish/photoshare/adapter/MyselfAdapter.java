package com.fish.photoshare.adapter;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.fish.photoshare.R;
import com.fish.photoshare.common.Api;
import com.fish.photoshare.pojo.Record;
import com.fish.photoshare.pojo.RecordDetail;
import com.fish.photoshare.utils.HttpUtils;
import com.fish.photoshare.utils.ResourcesUtils;
import com.fish.photoshare.utils.SharedPreferencesUtils;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.Callback;

public class MyselfAdapter extends RecyclerView.Adapter<MyselfAdapter.ViewHolder> {
    private Context context;
    private ResourcesUtils resourcesUtils;
    private Callback changeCallback;
    private Callback deleteCallback;
    private Record records;
    public MyselfAdapter() {}
    public MyselfAdapter(Context context, Callback changeCallback, Callback deleteCallback, Record records) {
        this.context = context;
        this.changeCallback = changeCallback;
        this.deleteCallback = deleteCallback;
        this.records = records;
        this.resourcesUtils = new ResourcesUtils(context);
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.myself_item, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RecordDetail detail = records.getRecordDetail().get(position);
        String id = SharedPreferencesUtils.getString(context, resourcesUtils.ID, null);
        HashMap<String, String> params = new HashMap<>();
        // 点击即可查看
        holder.myselfCard.setOnClickListener(v -> {

        });
        // 发布
        holder.publishButton.setOnClickListener(v -> {
            params.put("id", detail.getId());
            params.put("pUserId", id);
            params.put("imageCode", String.valueOf(detail.getImageCode()));
            params.put("title", detail.getTitle());
            params.put("content", detail.getContent());
            HttpUtils.sendPostRequest(Api.CHANGE, params, changeCallback);
            params.clear();
        });
        // 删除
        holder.deleteButton.setOnClickListener(v -> {
            params.put("shareId", detail.getId());
            params.put("userId", id);
            HttpUtils.sendPostRequest(Api.DELETE, params, deleteCallback);
            params.clear();
        });
        // 图片选第一张
        if (detail.getImageUrlList() != null && detail.getImageUrlList().size() > 0) {
            Uri uri = Uri.parse(detail.getImageUrlList().get(0));
            new Handler(Looper.getMainLooper()).post(() ->
                    Glide.with(context)
                    .load(uri)
                    .centerCrop()
                    .into(holder.PostImage));
        } else {
            new Handler(Looper.getMainLooper()).post(() -> {
                Glide.with(context)
                        .load(R.drawable.ic_launcher_background)
                        .centerCrop()
                        .into(holder.PostImage);
            });
        }
        holder.TextTitle.setText(detail.getTitle());
        holder.TextContent.setText(detail.getContent());
    }
    @Override
    public int getItemCount() {
        return records.getRecordDetail().size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // myself_item.xml中有标签并且需要到的元素
        private MaterialCardView myselfCard;
        private ShapeableImageView PostImage;
        private TextView TextTitle;
        private TextView TextContent;
        private MaterialButton publishButton;
        private MaterialButton deleteButton;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            myselfCard = itemView.findViewById(R.id.myselfCard);
            PostImage = itemView.findViewById(R.id.PostImage);
            TextTitle = itemView.findViewById(R.id.textContent);
            TextContent = itemView.findViewById(R.id.textTitle);
            publishButton = itemView.findViewById(R.id.publishButton);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }
}
