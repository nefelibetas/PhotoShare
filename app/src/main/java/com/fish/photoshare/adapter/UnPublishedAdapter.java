package com.fish.photoshare.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fish.photoshare.pojo.Record;
import com.fish.photoshare.utils.ResourcesUtils;

import okhttp3.Callback;


public class UnPublishedAdapter extends RecyclerView.Adapter<UnPublishedAdapter.ViewHolder> {
    private Context context;
    private Record record;
    private Callback changeCallback;
    private Callback deleteCallback;
    private ResourcesUtils resourcesUtils;
    public UnPublishedAdapter(){}
    public UnPublishedAdapter(Context context, Record record, Callback changeCallback, Callback deleteCallback) {
        this.context = context;
        this.record = record;
        this.changeCallback = changeCallback;
        this.deleteCallback = deleteCallback;
        resourcesUtils = new ResourcesUtils(context);
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }
    @Override
    public int getItemCount() {
        return record.getRecordDetail().size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
