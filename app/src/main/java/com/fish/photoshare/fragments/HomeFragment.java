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
    private ArrayList<Post> postList;
    public HomeFragment() {}
    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        HomeAdapter adapter = new HomeAdapter( );
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        return rootView;
    }
}