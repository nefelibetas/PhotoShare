package com.fish.photoshare.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.fish.photoshare.R;
import com.fish.photoshare.common.OnFragmentChangeListener;
import com.fish.photoshare.models.UserHomeFragmentModel;
import com.fish.photoshare.pojo.User;

public class UserHomeFragment extends Fragment {
    private UserHomeFragmentModel userHomeFragmentModel;
    private FragmentManager manager;
    private OnFragmentChangeListener listener;
    private User user;
    public UserHomeFragment() {}
    public static UserHomeFragment newInstance(User user) {
        UserHomeFragment fragment = new UserHomeFragment();
        Bundle args = new Bundle();
        args.putSerializable("user", user);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentChangeListener) {
            listener = (OnFragmentChangeListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnFragmentChangeListener");
        }
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.user = getArguments().getSerializable("user", User.class);
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_user_home, container, false);
        initView(rootView);
        return rootView;
    }
    public void initView(View rootView) {
        // fragmentManager
        manager = getParentFragmentManager();
        // initModel
        userHomeFragmentModel = new UserHomeFragmentModel();
        userHomeFragmentModel.initModel(rootView, getContext());
        userHomeFragmentModel.initOnClickListener(getContext());
        userHomeFragmentModel.initData(user);
    }
}