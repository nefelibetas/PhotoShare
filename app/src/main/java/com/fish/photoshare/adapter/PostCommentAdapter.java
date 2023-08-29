package com.fish.photoshare.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class PostCommentAdapter extends RecyclerView.Adapter<PostCommentAdapter.ViewHolder> {
    private final Context context;
    private final ArrayList<CommentDetail> details;
    private ArrayList<CommentDetail> secondComments;
    public PostCommentAdapter(Context context, CommentRecord records) {
        this.context = context;
        this.details = records.getRecords();
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_comment_item, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CommentDetail comment = details.get(position);
        holder.commentUsername.setText(comment.getUserName());
        holder.commentContent.setText(comment.getContent());
        holder.recyclerListCommentSecond.setLayoutManager(new LinearLayoutManager(context));
        String shareId = comment.getShareId();
        HashMap<String, String> params = new HashMap<>();
        String commentId = comment.getId();
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
                    Result<CommentRecord> result = HttpUtils.gson.fromJson(response.body().string(), new TypeToken<Result<CommentRecord>>(){}.getType());
                    if (result.getCode() != 200) {
                        Log.e("fishCat", "初始化评论信息失败 onResponse: ", new Exception("初始化评论信息失败"));
                    } else {
                        if (result.getData() != null){
                            secondComments = result.getData().getRecords();
                            SecondCommentAdapter adapter = new SecondCommentAdapter(context, secondComments);
                            holder.recyclerListCommentSecond.setAdapter(adapter);
                        } else {
                            secondComments = null;
                            new Handler(Looper.getMainLooper()).post(() -> holder.bottomLayout.setVisibility(View.GONE));
                        }

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
        private final TextView commentUsername;
        private final TextView commentContent;
        private final ConstraintLayout bottomLayout;
        private final RecyclerView recyclerListCommentSecond;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            commentUsername = itemView.findViewById(R.id.commentUsername);
            commentContent = itemView.findViewById(R.id.commentContent);
            bottomLayout = itemView.findViewById(R.id.bottomLayout);
            recyclerListCommentSecond = itemView.findViewById(R.id.recyclerListCommentSecond);
        }
    }
}
