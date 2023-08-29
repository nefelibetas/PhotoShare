package com.fish.photoshare.adapter;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.fish.photoshare.R;
import com.fish.photoshare.common.Api;
import com.fish.photoshare.pojo.PostDetail;
import com.fish.photoshare.pojo.PostRecord;
import com.fish.photoshare.utils.HttpUtils;
import com.fish.photoshare.utils.ResourcesUtils;
import com.fish.photoshare.utils.SharedPreferencesUtils;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.HashMap;

import okhttp3.Callback;


public class UnPublishedAdapter extends RecyclerView.Adapter<UnPublishedAdapter.ViewHolder> {
    private Context context;
    private PostRecord records;
    private Callback changeCallback;
    private Callback deleteCallback;
    private ResourcesUtils resourcesUtils;

    public UnPublishedAdapter() {
    }

    public UnPublishedAdapter(Context context, PostRecord records, Callback changeCallback, Callback deleteCallback) {
        this.context = context;
        this.records = records;
        this.changeCallback = changeCallback;
        this.deleteCallback = deleteCallback;
        resourcesUtils = new ResourcesUtils(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.unpublished_item, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PostDetail detail = records.getRecordDetail().get(position);
        HashMap<String, String> publishParam = new HashMap<>();
        HashMap<String, String> deleteParam = new HashMap<>();
        String id = SharedPreferencesUtils.getString(context, resourcesUtils.ID, null);
        // 点击即可查看
        holder.myselfCard.setOnClickListener(v -> {

        });
        // 删除
        holder.deleteButton.setOnClickListener(v -> {
            deleteParam.put("userId", id);
            deleteParam.put("shareId", detail.getId());
            String newUrl = HttpUtils.getRequestHandler(Api.DELETE, deleteParam);
            HttpUtils.sendPostRequest(newUrl, null, deleteCallback);
            records.getRecordDetail().remove(detail);
            notifyDataSetChanged();
        });
        // 发布
        holder.publishButton.setOnClickListener(v -> {
            publishParam.put("id", detail.getId());
            publishParam.put("pUserId", id);
            publishParam.put("imageCode", String.valueOf(detail.getImageCode()));
            publishParam.put("title", detail.getTitle());
            publishParam.put("content", detail.getContent());
            HttpUtils.sendPostRequest(Api.CHANGE, publishParam, changeCallback);
            records.getRecordDetail().remove(detail);
            notifyDataSetChanged();
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
        return records == null ? 0 : records.getRecordDetail().size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final MaterialCardView myselfCard;
        private final ShapeableImageView PostImage;
        private final TextView TextTitle;
        private final TextView TextContent;
        private final MaterialButton publishButton;
        private final MaterialButton deleteButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            myselfCard = itemView.findViewById(R.id.myselfCard);
            PostImage = itemView.findViewById(R.id.PostImage);
            TextTitle = itemView.findViewById(R.id.textTitle);
            TextContent = itemView.findViewById(R.id.textContent);
            publishButton = itemView.findViewById(R.id.publishButton);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }
}
