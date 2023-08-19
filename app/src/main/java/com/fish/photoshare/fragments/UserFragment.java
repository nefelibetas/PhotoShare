package com.fish.photoshare.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.fish.photoshare.R;
import com.fish.photoshare.activities.MySelfActivity;
import com.fish.photoshare.activities.StarActivity;
import com.fish.photoshare.activities.UserInformationActivity;
import com.fish.photoshare.common.Api;
import com.fish.photoshare.pojo.User;
import com.fish.photoshare.utils.HttpUtils;
import com.fish.photoshare.utils.SharedPreferencesUtils;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.imageview.ShapeableImageView;

import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
public class UserFragment extends Fragment implements View.OnClickListener {
    private User information;
    private MaterialCardView editCard;
    private MaterialCardView unpublishedCard;
    private MaterialCardView starCard;
    private MaterialCardView publishCard;
    private TextView username;
    private ShapeableImageView avatar;
    public static UserFragment newInstance() {
        UserFragment fragment = new UserFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
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
        editCard = rootView.findViewById(R.id.editCard);
        starCard = rootView.findViewById(R.id.starCard);
        unpublishedCard = rootView.findViewById(R.id.unpublishedCard);
        publishCard = rootView.findViewById(R.id.publishedCard);
        username = rootView.findViewById(R.id.tv_username);
        avatar = rootView.findViewById(R.id.ic_avatar);
        editCard.setOnClickListener(this);
        starCard.setOnClickListener(this);
        unpublishedCard.setOnClickListener(this);
        publishCard.setOnClickListener(this);
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
                new Handler(Looper.getMainLooper()).post(() -> {
                    Glide.with(getActivity())
                            .load(networkImageUri)
                            .override(125, 125)
                            .centerCrop()
                            .into(avatar);
                });
            }
        }
    }
    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.editCard) {
            Intent intent = new Intent(getActivity(), UserInformationActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("information", information);
            intent.putExtras(bundle);
            startActivity(intent);
        } else if (id == R.id.starCard) {
            Intent intent = new Intent(getActivity(), StarActivity.class);
            Bundle bundle = new Bundle();
            // putData
            intent.putExtras(bundle);
            startActivity(intent);
        } else if (id == R.id.unpublishedCard) {
            Intent intent = new Intent(getActivity(), MySelfActivity.class);
            Bundle bundle = new Bundle();
            intent.putExtras(bundle);
            startActivity(intent);
        } else if (id == R.id.publishedCard) {

        }
    }
