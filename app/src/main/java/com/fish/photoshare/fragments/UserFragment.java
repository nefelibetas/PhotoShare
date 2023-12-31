package com.fish.photoshare.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.fish.photoshare.R;
import com.fish.photoshare.activities.EntranceActivity;
import com.fish.photoshare.activities.MySelfActivity;
import com.fish.photoshare.activities.StarActivity;
import com.fish.photoshare.activities.UnPublishedActivity;
import com.fish.photoshare.activities.UserInformationActivity;
import com.fish.photoshare.pojo.User;
import com.fish.photoshare.utils.SharedPreferencesUtils;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.imageview.ShapeableImageView;
public class UserFragment extends Fragment implements View.OnClickListener {
    private User information;
    private TextView username;
    private ShapeableImageView avatar;
    public static UserFragment newInstance() {
        UserFragment fragment = new UserFragment();
        return fragment;
    }
    public UserFragment() {}
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        information = SharedPreferencesUtils.getUser(getContext());
    }
    @Override
    public void onStart() {
        super.onStart();
        information = SharedPreferencesUtils.getUser(getContext());
        dataHandler();
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_user, container, false);
        initView(rootView);
        dataHandler();
        return rootView;
    }
    private void initView(View rootView) {
        MaterialCardView editCard = rootView.findViewById(R.id.editCard);
        MaterialCardView starCard = rootView.findViewById(R.id.starCard);
        MaterialCardView unpublishedCard = rootView.findViewById(R.id.unpublishedCard);
        MaterialCardView publishCard = rootView.findViewById(R.id.publishedCard);
        MaterialCardView signOut = rootView.findViewById(R.id.signOut);
        username = rootView.findViewById(R.id.tv_username);
        avatar = rootView.findViewById(R.id.ic_avatar);
        editCard.setOnClickListener(this);
        starCard.setOnClickListener(this);
        unpublishedCard.setOnClickListener(this);
        publishCard.setOnClickListener(this);
        signOut.setOnClickListener(this);
    }
    private void dataHandler() {
        // 初始化从本地获取并通过校验得到的数据
        if (information != null) {
            String name = information.getUsername();
            if (name != null && !"".equals(name)) {
                username.setText(name);
            }
            String ava = information.getAvatar();
            if (ava == null) {
                avatar.setImageResource(R.drawable.ic_launcher_background);
            } else {
                Uri networkImageUri = Uri.parse(ava);
                new Handler(Looper.getMainLooper()).post(() ->
                        Glide.with(getActivity())
                                .load(networkImageUri)
                                .override(125, 125)
                                .centerCrop()
                                .into(avatar));
            }
        }
    }
    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.editCard) {
            // 编辑个人信息
            Intent intent = new Intent(getActivity(), UserInformationActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("information", information);
            intent.putExtras(bundle);
            startActivity(intent);
        } else if (id == R.id.starCard) {
            // 收藏
            Intent intent = new Intent(getActivity(), StarActivity.class);
            startActivity(intent);
        } else if (id == R.id.unpublishedCard) {
            // 仅保存未发布帖子
            Intent intent = new Intent(getActivity(), UnPublishedActivity.class);
            startActivity(intent);
        } else if (id == R.id.publishedCard) {
            // 发布的帖子
            Intent intent = new Intent(getActivity(), MySelfActivity.class);
            startActivity(intent);
        } else if (id == R.id.signOut) {
            Intent intent = new Intent(getActivity(), EntranceActivity.class);
            startActivity(intent);
            getActivity().finish();
        }
    }
}