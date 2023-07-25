package com.fish.photoshare.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.fish.photoshare.POJO.Post;

import java.util.ArrayList;

public class HomeAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<Post> postList;

    public HomeAdapter(){}
    public HomeAdapter(Context mContext, ArrayList<Post> list){
        this.mContext = mContext;
        this.postList = list;
    }

    @Override
    public int getCount() {
        return postList.size();
    }

    @Override
    public Object getItem(int position) {
        return postList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {

        } else {

        }
        return null;
    }
}
