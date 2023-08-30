package com.fish.photoshare.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fish.photoshare.R;
import com.fish.photoshare.common.Api;
import com.fish.photoshare.common.Result;
import com.fish.photoshare.pojo.CommentDetail;
import com.fish.photoshare.pojo.CommentRecord;
import com.fish.photoshare.utils.HttpUtils;
import com.fish.photoshare.utils.ResourcesUtils;
import com.fish.photoshare.utils.SharedPreferencesUtils;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class PostCommentAdapter extends RecyclerView.Adapter<PostCommentAdapter.ViewHolder> {
    private final ResourcesUtils resourcesUtils;
    private final Context context;
    private ArrayList<CommentDetail> details;
    private ArrayList<CommentDetail> secondComments;

    public PostCommentAdapter(Context context, CommentRecord records) {
        this.context = context;
        this.details = records.getRecords();
        resourcesUtils = new ResourcesUtils(context);
    }

    public void setDetails(ArrayList<CommentDetail> details) {
        this.details = details;
        new Handler(Looper.getMainLooper()).post(() -> notifyDataSetChanged());
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_comment_item, parent, false);
        return new ViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CommentDetail comment = details.get(position);
        holder.commentUsername.setText(comment.getUserName());
        holder.commentContent.setText(comment.getContent());
        holder.recyclerListCommentSecond.setLayoutManager(new LinearLayoutManager(context));
        initCommentData(holder, comment.getShareId(), comment.getId());
        initDialog(holder, position);
    }

    protected void initDialog(ViewHolder holder, int position) {
        CommentDetail comment = details.get(position);
        holder.commentBody.setOnClickListener(v -> {
            holder.secondCommentInput.setHint("回复 " + comment.getUserName() + " : ");
            holder.dialog.show();
            Log.d("fishCat", "position: " + position);
        });
        holder.confirmButton.setOnClickListener(v -> {
            HashMap<String, String> params = getParamsHandler(comment);
            String content = holder.secondCommentInput.getText().toString();
            params.put("content", content);
            Log.d("fishCat", "查看commentId 放进 params 后: " + comment.getId());
            Log.d("fishCat", "查看参数 : " + params);
            HttpUtils.sendPostRequest(Api.SECOND, params, new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    Log.e("fishCat", "发布二级评论失败 onFailure: ", e);
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    if (response.isSuccessful()) {
                        Result<Objects> result = HttpUtils.gson.fromJson(response.body().string(), HttpUtils.resultType);
                        if (result.getCode() != 200) {
                            Log.e("fishCat", "发布二级评论出错 onResponse: " + result, new Exception("发布二级评论出错"));
                        } else {
                            Log.d("fishCat", "发布二级评论成功 onResponse: " + result);
                            initCommentData(holder, comment.getShareId(), comment.getId());
                        }
                        params.clear();
                    }
                }
            });
            holder.dialog.cancel();
            holder.secondCommentInput.setText("");
        });
    }

    protected HashMap<String, String> getParamsHandler(CommentDetail comment) {
        HashMap<String, String> params = new HashMap<>();
        params.put("shareId", comment.getShareId());
        params.put("userId", SharedPreferencesUtils.getString(context, resourcesUtils.ID, null));
        params.put("userName", SharedPreferencesUtils.getString(context, resourcesUtils.USERNAME, null));
        if (comment.getParentCommentId() == null) {
            params.put("parentCommentId", comment.getId());
        } else {
            params.put("parentCommentId", comment.getParentCommentId());
        }
        if (comment.getParentCommentUserId() == null) {
            params.put("parentCommentUserId", comment.getpUserId());
        } else {
            params.put("parentCommentUserId", comment.getParentCommentUserId());
        }
        if (comment.getReplyCommentId() == null) {
            params.put("replyCommentId", comment.getId());
        } else {
            params.put("replyCommentId", comment.getReplyCommentId());
        }
        if (comment.getReplyCommentUserId() == null) {
            params.put("replyCommentUserId", comment.getpUserId());
        } else {
            params.put("replyCommentUserId", comment.getReplyCommentUserId());
        }
        return params;
    }

    protected void initCommentData(ViewHolder holder, String shareId, String commentId) {
        HashMap<String, String> params = new HashMap<>();
        params.put("shareId", shareId);
        params.put("commentId", commentId);
        HttpUtils.sendGetRequest(Api.SECOND, params, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.e("fishCat", "获取二级评论失败 onFailure: ", e);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    Result<CommentRecord> result = HttpUtils.gson.fromJson(response.body().string(), new TypeToken<Result<CommentRecord>>() {
                    }.getType());
                    if (result.getCode() != 200) {
                        Log.e("fishCat", "初始化评论信息失败 onResponse: ", new Exception("初始化评论信息失败"));
                    } else {
                        if (result.getData() != null) {
                            secondComments = result.getData().getRecords();
                            if (holder.adapter == null) {
                                holder.adapter = new SecondCommentAdapter(context, secondComments);
                            } else
                                holder.adapter.setDetails(secondComments);
                            new Handler(Looper.getMainLooper()).post(() -> {
                                holder.bottomLayout.setVisibility(View.VISIBLE);
                                holder.recyclerListCommentSecond.setAdapter(holder.adapter);
                            });
                        } else {
                            new Handler(Looper.getMainLooper()).post(() -> holder.bottomLayout.setVisibility(View.GONE));
                        }
                        params.clear();
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return details == null ? 0 : details.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ConstraintLayout commentBody;
        private final TextView commentUsername;
        private final TextView commentContent;
        private final ConstraintLayout bottomLayout;
        private final RecyclerView recyclerListCommentSecond;
        private SecondCommentAdapter adapter = null;
        private BottomSheetDialog dialog;
        private final EditText secondCommentInput;
        private final MaterialButton confirmButton;

        public ViewHolder(@NonNull View itemView, Context context) {
            super(itemView);
            commentBody = itemView.findViewById(R.id.topAndMiddleLayout);
            commentUsername = itemView.findViewById(R.id.commentUsername);
            commentContent = itemView.findViewById(R.id.commentContent);
            bottomLayout = itemView.findViewById(R.id.bottomLayout);
            recyclerListCommentSecond = itemView.findViewById(R.id.recyclerListCommentSecond);
            dialog = new BottomSheetDialog(context);
            dialog.setContentView(R.layout.bottom_sheet_comment);
            secondCommentInput = dialog.findViewById(R.id.secondCommentInput);
            confirmButton = dialog.findViewById(R.id.confirm_button);
        }
    }
}
