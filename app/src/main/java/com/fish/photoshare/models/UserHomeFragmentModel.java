package com.fish.photoshare.models;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;

import com.fish.photoshare.R;
import com.fish.photoshare.common.OnFragmentChangeListener;
import com.fish.photoshare.fragments.InputFragment;
import com.fish.photoshare.pojo.User;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.HashMap;

public class UserHomeFragmentModel {
    // 个人信息页面的卡片
    private MaterialCardView avatarCard;
    private MaterialCardView usernameCard;
    private MaterialCardView sexCard;
    private MaterialCardView introduceCard;
    private FrameLayout femaleLayout;
    private FrameLayout maleLayout;
    // 需要用到的展示板
    private ShapeableImageView avatar;
    private TextView username;
    private TextView sex;
    private TextView introduce;
    private ShapeableImageView female;
    private ShapeableImageView male;
    // 对话框
    private BottomSheetDialog avatarDialog;
    private BottomSheetDialog sexDialog;
    // 对话框里面的按钮
    private MaterialButton cameraButton;
    private MaterialButton imageButton;
    private MaterialButton confirmButton;
    private int chooseSex;
    public UserHomeFragmentModel() {}
    public void initModel(View rootView, Context context) {
        // 卡片
        avatarCard = rootView.findViewById(R.id.card_avatar);
        usernameCard = rootView.findViewById(R.id.card_username);
        sexCard = rootView.findViewById(R.id.card_sex);
        introduceCard = rootView.findViewById(R.id.card_introduce);

        // 需要设置内容的对象
        avatar = rootView.findViewById(R.id.edit_avatar);
        username = rootView.findViewById(R.id.edit_username);
        sex = rootView.findViewById(R.id.edit_sex);
        introduce = rootView.findViewById(R.id.edit_introduce);
        // 弹窗相关
        avatarDialog = new BottomSheetDialog(context);
        avatarDialog.setContentView(R.layout.bottom_sheet_layout);
        sexDialog = new BottomSheetDialog(context);
        sexDialog.setContentView(R.layout.middle_sex_dialog);
        // 弹窗中的按钮 和 一些元素
        cameraButton = avatarDialog.findViewById(R.id.cameraButton);
        imageButton = avatarDialog.findViewById(R.id.ImageButton);
        // 性别对话框的内部元素
        maleLayout = sexDialog.findViewById(R.id.layout_male);
        femaleLayout = sexDialog.findViewById(R.id.layout_female);
        confirmButton = sexDialog.findViewById(R.id.confirm_button);
        male = sexDialog.findViewById(R.id.ic_male);
        female = sexDialog.findViewById(R.id.ic_female);
    }

    public void initOnClickListener(Context context, FragmentManager manager, OnFragmentChangeListener listener) {
        // 头像
        avatarCard.setOnClickListener(v -> {
            avatarDialog.show();
        });
        // 性别
        sexCard.setOnClickListener(v -> {
            sexDialog.show();
        });
        // 用户名
        usernameCard.setOnClickListener(v -> {
            manager.beginTransaction()
                    .replace(R.id.userFragmentContainer, InputFragment.newInstance("username"))
                    .commit();
            listener.onFragmentChanged("修改用户名", true);
        });
        // 自我介绍
        introduceCard.setOnClickListener(v -> {
            manager.beginTransaction()
                    .replace(R.id.userFragmentContainer, InputFragment.newInstance("introduce"))
                    .commit();
            listener.onFragmentChanged("修改自我介绍", true);
        });
        // 头像对话框下面的两个按钮
        cameraButton.setOnClickListener(v -> {

        });
        imageButton.setOnClickListener(v -> {

        });
        // 性别对话框下面的
        confirmButton.setOnClickListener(v -> {
            HashMap<String, Integer> params = new HashMap<>();
            params.put("sex", chooseSex);

        });
        int colorTransparent = ContextCompat.getColor(context, R.color.transparent);
        int colorFemale = ContextCompat.getColor(context, R.color.female);
        int colorMale = ContextCompat.getColor(context, R.color.male);
        femaleLayout.setOnClickListener(v -> {
            chooseSex = 0;
            // 还原其他样式
            maleLayout.setBackgroundColor(colorTransparent);
            male.setImageResource(R.drawable.baseline_male_24);
            // 设置对应样式
            femaleLayout.setBackgroundColor(colorFemale);
            female.setImageResource(R.drawable.baseline_female_white_24);
        });
        maleLayout.setOnClickListener(v -> {
            chooseSex = 1;
            // 还原其他样式
            femaleLayout.setBackgroundColor(colorTransparent);
            female.setImageResource(R.drawable.baseline_female_24);
            // 设置对应样式
            maleLayout.setBackgroundColor(colorMale);
            male.setImageResource(R.drawable.baseline_male_white_24);
        });
    }

    public void initData(User user) {
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
                if (userSex.equals("null"))
                    sex.setText("未选择");
                else if (userSex.equals("1"))
                    sex.setText("男");
                else if (userSex.equals("0"))
                    sex.setText("女");
            }
            String userIntroduce = user.getIntroduce();
            if (userIntroduce != null && !userIntroduce.equals("")) {
                introduce.setText(userIntroduce);
            }
        }
    }
}