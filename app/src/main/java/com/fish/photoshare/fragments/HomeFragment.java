package com.fish.photoshare.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.fish.photoshare.R;
import com.fish.photoshare.adapter.HomeAdapter;
import com.fish.photoshare.pojo.Post;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private ArrayList<Post> postList;

    public HomeFragment() {}

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public void mockData(){
        String[] titles = getResources().getStringArray(R.array.titles);
        String[] authors = getResources().getStringArray(R.array.authors);
        String[] images = getResources().getStringArray(R.array.images);

        int len = 0;
        len = Math.max(titles.length, authors.length);
        len = Math.max(len, images.length);
        for (int i = 0; i < len; i++) {
            String title = titles[i];
            String author = authors[i];
            int imageId = Integer.parseInt(images[i]);
            Post post = new Post(title, author, imageId);
            postList.add(post);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        HomeAdapter adapter = new HomeAdapter( );
        return inflater.inflate(R.layout.fragment_home, container, false);
    }
}