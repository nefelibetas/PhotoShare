package com.fish.photoshare.fragments.user;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.fish.photoshare.R;
import com.fish.photoshare.common.GalleryAndCameraListenerForAvatar;
import com.fish.photoshare.models.UserHomeFragmentModel;
import com.fish.photoshare.pojo.User;
import com.fish.photoshare.utils.SharedPreferencesUtils;

public class UserHomeFragment extends Fragment {
    private UserHomeFragmentModel userHomeFragmentModel;
    private User user;
    private GalleryAndCameraListenerForAvatar galleryAndCameraListenerForAvatar;
    public UserHomeFragment() {}
    public UserHomeFragment(GalleryAndCameraListenerForAvatar listener) {
        galleryAndCameraListenerForAvatar = listener;
    }
    public static UserHomeFragment newInstance(GalleryAndCameraListenerForAvatar listener) {
        UserHomeFragment fragment = new UserHomeFragment(listener);
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        user = SharedPreferencesUtils.getUser(getContext());
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_user_home, container, false);
        initView(rootView);
        return rootView;
    }
    public void initView(View rootView) {
        // initModel
        userHomeFragmentModel = new UserHomeFragmentModel(getContext());
        userHomeFragmentModel.initModel(rootView, galleryAndCameraListenerForAvatar);
        userHomeFragmentModel.initOnClickListener();
        userHomeFragmentModel.initData(user);
    }
}