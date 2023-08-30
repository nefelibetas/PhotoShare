package com.fish.photoshare.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
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
import com.fish.photoshare.utils.ResourcesUtils;
import com.fish.photoshare.utils.SharedPreferencesUtils;
import com.fish.photoshare.utils.ToastUtils;
import com.google.android.material.card.MaterialCardView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> {
    private final Context context;
    private PostRecord records;
    private final ResourcesUtils resourcesUtils;
    public final onChangePostState listener;

    public HomeAdapter(Context Context, PostRecord records, onChangePostState listener) {
        this.context = Context;
        this.records = records;
        this.listener = listener;
        this.resourcesUtils = new ResourcesUtils(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_home_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PostDetail detail = records.getRecordDetail().get(position);
        initView(holder, detail, position);
    }

    protected void initView(ViewHolder holder, PostDetail detail, int position) {
        holder.userNameText.setText(detail.getUsername());
        holder.TitleText.setText(detail.getTitle());
        // 当前用户是否点赞
        boolean hasLike = detail.isHasLike();
        if (hasLike) {
            new Handler(Looper.getMainLooper()).post(() -> Glide.with(context)
                    .load(R.drawable.baseline_thumb_up_alt_24)
                    .centerCrop()
                    .into(holder.UpIcon));
        } else {
            new Handler(Looper.getMainLooper()).post(() -> Glide.with(context)
                    .load(R.drawable.outline_thumb_up_off_alt_24)
                    .centerCrop()
                    .into(holder.UpIcon));
        }
        // 点赞数量
        String likeNum = detail.getLikeNum();
        if (likeNum != null && !likeNum.equals("0")) {
            holder.UpNumber.setText(likeNum);
        } else {
            holder.UpNumber.setText("0");
        }
        // 当前用户是否关注该作者
        boolean hasFocus = detail.isHasFocus();
        if (hasFocus) {
            holder.Focus.setBackgroundColor(ContextCompat.getColor(context, R.color.prompt_transparent));
            holder.FocusText.setText("已关注");
        }
        // 是否收藏
        boolean hasCollect = detail.isHasCollect();
        if (hasCollect) {
            new Handler(Looper.getMainLooper()).post(() -> Glide.with(context)
                    .load(R.drawable.baseline_star_24)
                    .centerCrop()
                    .into(holder.StarIcon));
        } else {
            new Handler(Looper.getMainLooper()).post(() -> Glide.with(context)
                    .load(R.drawable.outline_star_border_24)
                    .centerCrop()
                    .into(holder.StarIcon));
        }
        // 收藏数量
        String collectNum = detail.getCollectNum();
        if (collectNum != null && !collectNum.equals("0")) {
            holder.StarNumber.setText(collectNum);
        } else {
            holder.StarNumber.setText("0");
        }
        initImage(holder.RecyclerListImagePost, detail.getImageUrlList());
        initClickListener(holder, detail, position);
    }

    protected void initImage(RecyclerView recyclerView, ArrayList<String> list) {
        GridLayoutManager manager = new GridLayoutManager(this.context, 3);
        recyclerView.setLayoutManager(manager);
        ImageAdapter imageAdapter = new ImageAdapter(context, list);
        recyclerView.setAdapter(imageAdapter);
    }

    protected void initClickListener(ViewHolder holder, PostDetail detail, int position) {
        // 点击卡片
        holder.PostItem.setOnClickListener(v -> {
            Intent intent = new Intent(context, PostInformationActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("detail", detail);
            intent.putExtras(bundle);
            context.startActivity(intent);
        });
        // 点击关注
        holder.Focus.setOnClickListener(v -> {
            String focusUserId = detail.getPUserId();
            String userId = SharedPreferencesUtils.getString(context, resourcesUtils.ID, null);
            if (detail.isHasFocus()) {
                HashMap<String, String> unFocusParams = new HashMap<>();
                unFocusParams.put("userId", userId);
                unFocusParams.put("focusUserId", focusUserId);
                String newUrl = HttpUtils.getRequestHandler(Api.FOCUS_CANCEL, unFocusParams);
                HttpUtils.sendPostRequest(newUrl, null, new Callback() {
                    @Override
                    public void onFailure(@NonNull Call call, @NonNull IOException e) {
                        Log.e("fishCat", "取消关注 onFailure: ", e);
                    }

                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                        if (response.isSuccessful()) {
                            Result<Object> result = HttpUtils.gson.fromJson(response.body().string(), HttpUtils.resultType);
                            if (result.getCode() != 200) {
                                Log.e("fishCat", "取消关注 onResponse: ", new Exception("取消关注"));
                            } else {
                                new Handler(Looper.getMainLooper()).post(() -> {
                                    int color = ContextCompat.getColor(context, R.color.blue);
                                    holder.Focus.setBackgroundTintList(ColorStateList.valueOf(color));
                                    holder.FocusText.setText("关注");
                                    detail.setHasFocus(false);
                                    Log.d("fishCat", "取消关注 onResponse: " + result);
                                });
                            }
                        }
                    }
                });
            } else {
                // 先判断是不是在关注自己
                if (userId.equals(focusUserId)) {
                    ToastUtils.show(context, "不能关注自己");
                } else {
                    HashMap<String, String> focusParams = new HashMap<>();
                    focusParams.put("userId", userId);
                    focusParams.put("focusUserId", focusUserId);
                    String newUrl = HttpUtils.getRequestHandler(Api.FOCUS, focusParams);
                    HttpUtils.sendPostRequest(newUrl, null, new Callback() {
                        @Override
                        public void onFailure(@NonNull Call call, @NonNull IOException e) {
                            Log.e("fishCat", "关注 onFailure: ", e);
                        }

                        @Override
                        public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                            if (response.isSuccessful()) {
                                Result<Object> result = HttpUtils.gson.fromJson(response.body().string(), HttpUtils.resultType);
                                if (result.getCode() != 200) {
                                    Log.e("fishCat", "关注 onResponse: ", new Exception("关注"));
                                } else {
                                    new Handler(Looper.getMainLooper()).post(() -> {
                                        int color = ContextCompat.getColor(context, R.color.prompt_transparent);
                                        holder.Focus.setBackgroundTintList(ColorStateList.valueOf(color));
                                        holder.FocusText.setText("已关注");
                                        detail.setHasFocus(true);
                                        Log.d("fishCat", "关注 onResponse: " + result);
                                    });
                                }
                            }
                        }
                    });
                }
            }
        });
        // 点击收藏
        holder.StarCard.setOnClickListener(v -> {
            if (detail.isHasCollect()) {
                HashMap<String, String> unCollectParams = new HashMap<>();
                unCollectParams.put("collectId", detail.getCollectId());
                String newUrl = HttpUtils.getRequestHandler(Api.COLLECT_CANCEL, unCollectParams);
                HttpUtils.sendPostRequest(newUrl, null, new Callback() {
                    @Override
                    public void onFailure(@NonNull Call call, @NonNull IOException e) {
                        Log.e("fishCat", "取消收藏失败 onFailure: ", e);
                    }
                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                        if (response.isSuccessful()) {
                            Result<Object> result = HttpUtils.gson.fromJson(response.body().string(), HttpUtils.resultType);
                            if (result.getCode() != 200) {
                                Log.e("fishCat", "取消收藏 onResponse: ", new Exception("取消收藏失败"));
                            } else {
                                new Handler(Looper.getMainLooper()).post(() -> {
                                    holder.StarIcon.setImageResource(R.drawable.outline_star_border_24);
                                    int starNumber = Integer.parseInt(holder.StarNumber.getText().toString()) - 1;
                                    String starNumberText = starNumber + "";
                                    holder.StarNumber.setText(starNumberText);
                                    detail.setHasCollect(false);
                                    detail.setCollectId(null);
                                });
                            }
                        }
                    }
                });
            } else {
                HashMap<String, String> collectParams = new HashMap<>();
                String id = SharedPreferencesUtils.getString(context, resourcesUtils.ID, null);
                collectParams.put("userId", id);
                collectParams.put("shareId", detail.getId());
                String newUrl = HttpUtils.getRequestHandler(Api.COLLECT, collectParams);
                HttpUtils.sendPostRequest(newUrl, null, new Callback() {
                    @Override
                    public void onFailure(@NonNull Call call, @NonNull IOException e) {
                        Log.e("fishCat", "收藏失败 onFailure: ", e);
                    }

                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                        if (response.isSuccessful()) {
                            Result<Object> result = HttpUtils.gson.fromJson(response.body().string(), HttpUtils.resultType);
                            if (result.getCode() != 200) {
                                Log.e("fishCat", "收藏 onResponse: ", new Exception("收藏失败"));
                            } else {
                                new Handler(Looper.getMainLooper()).post(() -> {
                                    holder.StarIcon.setImageResource(R.drawable.baseline_star_24);
                                    int starNumber = Integer.parseInt(holder.StarNumber.getText().toString()) + 1;
                                    String starNumberText = starNumber + "";
                                    holder.StarNumber.setText(starNumberText);
                                    detail.setHasCollect(true);
                                    listener.onChangePostState(position);
                                });
                            }
                        }
                    }
                });
            }
        });
        // 点赞
        holder.UpCard.setOnClickListener(v -> {
            if (detail.isHasLike()) {
                HashMap<String, String> unLikeParams = new HashMap<>();
                unLikeParams.put("likeId", detail.getLikeId());
                Log.d("fishCat", "取消点赞 detail: " + detail);
                String newUrl = HttpUtils.getRequestHandler(Api.LIKE_CANCEL, unLikeParams);
                HttpUtils.sendPostRequest(newUrl, null, new Callback() {
                    @Override
                    public void onFailure(@NonNull Call call, @NonNull IOException e) {
                        Log.e("fishCat", "取消点赞 onFailure: ", e);
                    }
                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                        if (response.isSuccessful()) {
                            String body = response.body().string();
                            Result<Object> result = HttpUtils.gson.fromJson(body, HttpUtils.resultType);
                            if (result.getCode() != 200) {
                                Log.e("fishCat", "取消点赞 onResponse: " + response, new Exception("取消点赞失败"));
                            } else {
                                new Handler(Looper.getMainLooper()).post(() -> {
                                    holder.UpIcon.setImageResource(R.drawable.outline_thumb_up_off_alt_24);
                                    int upNumber = Integer.parseInt(holder.UpNumber.getText().toString()) - 1;
                                    String upNumberText = upNumber + "";
                                    holder.UpNumber.setText(upNumberText);
                                    detail.setHasLike(false);
                                    detail.setLikeId(null);
                                });
                            }
                        }
                    }
                });
            } else {
                // 点赞
                HashMap<String, String> likeParams = new HashMap<>();
                String id = SharedPreferencesUtils.getString(context, resourcesUtils.ID, null);
                likeParams.put("userId", id);
                likeParams.put("shareId", detail.getId());
                String newUrl = HttpUtils.getRequestHandler(Api.LIKE, likeParams);
                HttpUtils.sendPostRequest(newUrl, null, new Callback() {
                    @Override
                    public void onFailure(@NonNull Call call, @NonNull IOException e) {
                        Log.e("fishCat", "点赞 onFailure: ", e);
                    }

                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                        if (response.isSuccessful()) {
                            String body = response.body().string();
                            Result<Object> result = HttpUtils.gson.fromJson(body, HttpUtils.resultType);
                            if (result.getCode() != 200) {
                                Log.e("fishCat", "点赞 onResponse: " + result, new Exception("点赞失败"));
                            } else {
                                new Handler(Looper.getMainLooper()).post(() -> {
                                    holder.UpIcon.setImageResource(R.drawable.baseline_thumb_up_alt_24);
                                    int upNumber = Integer.parseInt(holder.UpNumber.getText().toString()) + 1;
                                    String upNumberText = upNumber + "";
                                    holder.UpNumber.setText(upNumberText);
                                    detail.setHasLike(true);
                                    listener.onChangePostState(position);
                                });
                            }
                        }
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return records.getRecordDetail().size();
    }

    public void updateData(PostRecord records) {
        this.records = records;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final MaterialCardView PostItem;
        private final TextView userNameText;
        private final MaterialCardView Focus;
        private final TextView FocusText;
        private final TextView TitleText;
        private final RecyclerView RecyclerListImagePost;
        private final CardView StarCard;
        private final ImageView StarIcon;
        private final TextView StarNumber;
        private final CardView UpCard;
        private final ImageView UpIcon;
        private final TextView UpNumber;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            PostItem = itemView.findViewById(R.id.home_postItem);
            // 顶部元素获取
            userNameText = itemView.findViewById(R.id.home_username_text);
            Focus = itemView.findViewById(R.id.home_focus);
            FocusText = itemView.findViewById(R.id.home_focus_text);
            TitleText = itemView.findViewById(R.id.home_title_text);
            // 第二层recycleView
            RecyclerListImagePost = itemView.findViewById(R.id.home_recyclerListImage);
            // 下方按钮获取
            StarCard = itemView.findViewById(R.id.starCard);
            StarIcon = itemView.findViewById(R.id.home_starIcon);
            StarNumber = itemView.findViewById(R.id.home_starNumber_text);
            UpCard = itemView.findViewById(R.id.upCard);
            UpIcon = itemView.findViewById(R.id.upIcon);
            UpNumber = itemView.findViewById(R.id.home_upNumber_text);
        }
    }
}
