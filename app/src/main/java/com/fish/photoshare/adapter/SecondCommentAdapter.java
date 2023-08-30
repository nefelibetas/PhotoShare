package com.fish.photoshare.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fish.photoshare.R;
import com.fish.photoshare.pojo.CommentDetail;

import java.util.ArrayList;

public class SecondCommentAdapter extends RecyclerView.Adapter<SecondCommentAdapter.ViewHolder> {
    private final Context context;
    private ArrayList<CommentDetail> details;

    public void setDetails(ArrayList<CommentDetail> details) {
        this.details = details;
        new Handler(Looper.getMainLooper()).post(() -> notifyDataSetChanged());
    }

    public SecondCommentAdapter(Context context, ArrayList<CommentDetail> details) {
        this.context = context;
        this.details = details;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.second_comment_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CommentDetail comment = details.get(position);
        holder.commentUsername.setText(comment.getUserName() + " :");
        holder.commentContent.setText(comment.getContent());
    }

    @Override
    public int getItemCount() {
        return details == null ? 0 : details.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView commentUsername;
        private TextView commentContent;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            commentUsername = itemView.findViewById(R.id.commentUsername);
            commentContent = itemView.findViewById(R.id.commentContent);
        }
    }
}
