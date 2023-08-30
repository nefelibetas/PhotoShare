package com.fish.photoshare.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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
import com.fish.photoshare.activities.PostInformationActivity;
import com.fish.photoshare.common.Api;
import com.fish.photoshare.common.Result;
import com.fish.photoshare.common.onChangePostState;
import com.fish.photoshare.pojo.PostDetail;
import com.fish.photoshare.pojo.PostRecord;
import com.fish.photoshare.utils.HttpUtils;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.imageview.ShapeableImageView;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class StarAdapter extends RecyclerView.Adapter<StarAdapter.ViewHolder> implements onChangePostState {
    private Context context;
    private PostRecord postRecord;

    public StarAdapter(Context context, PostRecord postRecord) {
        this.context = context;
        this.postRecord = postRecord;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.star_item, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PostDetail detail = postRecord.getRecordDetail().get(position);
        holder.starItem.setOnClickListener(v -> {
            Intent intent = new Intent(context, PostInformationActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("detail", detail);
            intent.putExtras(bundle);
            context.startActivity(intent);
        });
        initView(holder, detail, position);
    }

    @Override
    public int getItemCount() {
        return postRecord == null ? 0 : postRecord.getRecordDetail().size();
    }

    protected void initView(ViewHolder holder, PostDetail detail, int position) {
        new Handler(Looper.getMainLooper()).post(() -> {
            if (detail.getImageUrlList().size() > 0) {
                Uri uri = Uri.parse(detail.getImageUrlList().get(0));
                Glide.with(context)
                        .load(uri)
                        .centerCrop()
                        .into(holder.PostImage);
            } else {
                Glide.with(context)
                        .load(R.drawable.ic_launcher_background)
                        .centerCrop()
                        .into(holder.PostImage);
            }
        });
        holder.textTitle.setText(detail.getTitle());
        holder.textContent.setText(detail.getContent());
        initClickListener(holder.unCollectButton, detail.getCollectId(), position);
    }

    protected void initClickListener(MaterialButton button, String collectId, int position) {
        HashMap<String, String> unCollectParams = new HashMap<>();
        unCollectParams.put("collectId", collectId);
        String newUrl = HttpUtils.getRequestHandler(Api.COLLECT_CANCEL, unCollectParams);
        button.setOnClickListener(v -> {
            HttpUtils.sendPostRequest(newUrl, null, new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    Log.e("fishCat", "取消收藏 onFailure: ", e);
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    if (response.isSuccessful()) {
                        Result<Object> result = HttpUtils.gson.fromJson(response.body().string(), HttpUtils.resultType);
                        if (result.getCode() != 200) {
                            Log.e("fishCat", "取消收藏 onResponse: ", new Throwable("取消收藏失败"));
                        } else {
                            onChangePostState(position);
                        }
                    }
                }
            });
        });
    }

    @Override
    public void onChangePostState(int position) {
        postRecord.getRecordDetail().remove(position);
        new Handler(Looper.getMainLooper()).post(() -> notifyDataSetChanged());
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final MaterialCardView starItem;
        private final ShapeableImageView PostImage;
        private final TextView textTitle;
        private final TextView textContent;
        private final MaterialButton unCollectButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            starItem = itemView.findViewById(R.id.starItem);
            PostImage = itemView.findViewById(R.id.PostImage);
            textTitle = itemView.findViewById(R.id.textTitle);
            textContent = itemView.findViewById(R.id.textContent);
            unCollectButton = itemView.findViewById(R.id.unCollectButton);
        }
    }
}
