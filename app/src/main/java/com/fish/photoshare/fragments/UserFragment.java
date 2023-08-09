package com.fish.photoshare.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fish.photoshare.R;
import com.fish.photoshare.activities.MainActivity;
import com.fish.photoshare.activities.UserInformationActivity;
import com.fish.photoshare.common.Api;
import com.fish.photoshare.pojo.User;
import com.fish.photoshare.utils.HttpUtils;
import com.fish.photoshare.utils.ToastUtils;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.imageview.ShapeableImageView;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.HashMap;

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
    private static final String USER_INFORMATION = "userInformation";
    private User information;
    private MaterialCardView editCard;
    private MaterialCardView settingCard;
    private MaterialCardView starCard;
    private TextView username;
    private ShapeableImageView avatar;
    public static UserFragment newInstance(User information) {
        UserFragment fragment = new UserFragment();
        Bundle args = new Bundle();
        args.putString(USER_INFORMATION, HttpUtils.gson.toJson(information));
        fragment.setArguments(args);
        return fragment;
    }

    public UserFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            String info = getArguments().getString(USER_INFORMATION);
            information = HttpUtils.gson.fromJson(info, User.class);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_user, container, false);
        initView(rootView);
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
        dataHandler();
    }

    private void dataHandler() {
        if (information != null) {
            Log.d("fishCat", "dataHandler: " + information);
            username.setText(information.getUsername());
            if (information.getAvatar() == null) {
                avatar.setImageResource(R.drawable.ic_launcher_background);
            } else {
                HttpUtils.getImageResources(Api.BASIC_URL, new Callback() {
                    @Override
                    public void onFailure(@NonNull Call call, @NonNull IOException e) {
                        Log.d("fishCat", "getImage onFailure: " + e.getMessage());
                    }
                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                        if (response.isSuccessful()) {
                            InputStream in = response.body().byteStream();
                            Bitmap bitmap = BitmapFactory.decodeStream(in);
                            avatar.setImageBitmap(bitmap);
                        } else {
                            Log.d("fishCat", "getImage onResponse failure");
                        }
                    }
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

        } else if (id == R.id.settingCard) {

        }
    }
}