package com.fish.photoshare.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.fish.photoshare.R;
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

/*
    {
      "code": 200,
      "msg": "成功",
      "data": {
        "imageCode": "1687851662531760128",
        "imageUrlList": [
          "https://guet-lab.oss-cn-hangzhou.aliyuncs.com/api/2023/08/05/01104271-6f79-4d73-98ec-930bc97b5413.png"
        ]
      }
    }
* */

public class UserFragment extends Fragment implements View.OnClickListener {
    private User information;
    private MaterialCardView editCard;
    private MaterialCardView settingCard;
    private MaterialCardView starCard;
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
        settingCard = rootView.findViewById(R.id.settingCard);
        username = rootView.findViewById(R.id.tv_username);
        avatar = rootView.findViewById(R.id.ic_avatar);
        editCard.setOnClickListener(this);
        starCard.setOnClickListener(this);
        settingCard.setOnClickListener(this);
    }
    private void dataHandler() {
        // 初始化从本地获取并通过校验得到的数据
        if (information != null) {
            String name = information.getUsername();
            if (name != null && !"".equals(name)) {
                username.setText(name);
            }
            String avt = information.getAvatar();
            if (avt == null && !"".equals(avt)) {
                avatar.setImageResource(R.drawable.ic_launcher_background);
            } else {
                // todo: 从网络获取头像
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

        } else if (id == R.id.settingCard) {

        }
    }
}