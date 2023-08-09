package com.fish.photoshare.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fish.photoshare.R;
import com.fish.photoshare.common.OnFragmentChangeListener;
import com.fish.photoshare.pojo.User;
import com.fish.photoshare.utils.ToastUtils;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.imageview.ShapeableImageView;

public class UserHomeFragment extends Fragment {
    private MaterialCardView avatarCard;
    private MaterialCardView usernameCard;
    private MaterialCardView sexCard;
    private MaterialCardView introduceCard;
    private ShapeableImageView avatar;
    private TextView username;
    private TextView sex;
    private TextView introduce;
    private BottomSheetDialog dialog;
    private MaterialButton cameraButton;
    private MaterialButton ImageButton;
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
        initOnClickListener();
        initData();
        return rootView;
    }
    public void initView(View rootView) {
        // fragmentManager
        manager = getParentFragmentManager();
        // CardView
        avatarCard = rootView.findViewById(R.id.card_avatar);
        usernameCard = rootView.findViewById(R.id.card_username);
        sexCard = rootView.findViewById(R.id.card_sex);
        introduceCard = rootView.findViewById(R.id.card_introduce);
        // 需要设置内容的对象
        avatar = rootView.findViewById(R.id.edit_avatar);
        username = rootView.findViewById(R.id.edit_username);
        sex = rootView.findViewById(R.id.edit_sex);
        introduce = rootView.findViewById(R.id.edit_introduce);
        // 底部弹窗
        dialog = new BottomSheetDialog(getActivity());
        dialog.setContentView(R.layout.bottom_sheet_layout);
        // 弹窗按钮
        cameraButton = dialog.findViewById(R.id.cameraButton);
        ImageButton =  dialog.findViewById(R.id.ImageButton);
    }
    public void initOnClickListener() {
        // 头像
        avatarCard.setOnClickListener(v -> {
            dialog.show();
        });
        // 用户名
        usernameCard.setOnClickListener(v -> {
            manager.beginTransaction()
                    .replace(R.id.userFragmentContainer, InputFragment.newInstance(user.getUsername()))
                    .commit();
            listener.onFragmentChanged("修改用户名", true);
        });
        // 性别
        sexCard.setOnClickListener(v -> {
            ToastUtils.show(getActivity(), "性别");
        });
        // 自我介绍
        introduceCard.setOnClickListener(v -> {
            ToastUtils.show(getActivity(), "自我介绍");
        });
        // 头像对话框下面的两个按钮
        cameraButton.setOnClickListener(v -> {

        });
        ImageButton.setOnClickListener(v -> {

        });

    }
    public void initData() {
        if (user != null) {
            Log.d("fishCat", "initData: " + user.toString());
            String userAvatar = user.getAvatar();
            if (userAvatar != null) {

            }
            String string_username = user.getUsername();
            if (string_username != null && !string_username.equals("")) {
                username.setText(string_username);
            }
            String userSex = user.getSex();
            if (userSex != null && !userSex.equals("")) {
                sex.setText(userSex);
            }
            String userIntroduce = user.getIntroduce();
            if (userIntroduce != null && !userIntroduce.equals("")) {
                introduce.setText(userIntroduce);
            }
        }
    }
}