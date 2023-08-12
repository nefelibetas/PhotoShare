package com.fish.photoshare.models;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.fish.photoshare.R;
import com.fish.photoshare.activities.UserInformationActivity;
import com.fish.photoshare.common.Api;
import com.fish.photoshare.common.CrossComponentListener;
import com.fish.photoshare.common.Result;
import com.fish.photoshare.pojo.User;
import com.fish.photoshare.utils.HttpUtils;
import com.fish.photoshare.utils.ResourcesUtils;
import com.fish.photoshare.utils.SharedPreferencesUtils;
import com.fish.photoshare.utils.ToastUtils;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class UserHomeFragmentModel implements Serializable {
    // 个人信息页面的卡片
    private MaterialCardView avatarCard;
    private MaterialCardView usernameCard;
    private MaterialCardView sexCard;
    private MaterialCardView introduceCard;
    private FrameLayout femaleLayout;
    private FrameLayout maleLayout;
    // 需要用到的展示板
    private ShapeableImageView avatar;
    public TextView username;
    private TextView sex;
    public TextView introduce;
    private ShapeableImageView female;
    private ShapeableImageView male;
    private TextInputEditText usernameInput;
    private TextInputEditText introduceInput;
    // 对话框
    private BottomSheetDialog avatarDialog;
    private BottomSheetDialog sexDialog;
    private BottomSheetDialog usernameDialog;
    private BottomSheetDialog introduceDialog;
    // 对话框里面的按钮
    private MaterialButton cameraButton;
    private MaterialButton imageButton;
    private MaterialButton sexConfirmButton;
    private MaterialButton usernameConfirmButton;
    private MaterialButton introduceConfirmButton;
    private ResourcesUtils resourcesUtils;
    private CrossComponentListener crossComponentListener;
    private Handler handler;
    private Context context;
    private int chooseSex;
    public UserHomeFragmentModel() {}
    public UserHomeFragmentModel(Context mContext) {
        this.context = mContext;
    }
    public void initModel(View rootView, CrossComponentListener listener) {
        // 监听器
        crossComponentListener = listener;
        // 初始化资源Utils
        resourcesUtils = new ResourcesUtils(context);
        // 更新ui的handler
        handler = new Handler(Looper.getMainLooper());
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
        // 用户名对话框
        usernameDialog = new BottomSheetDialog(context);
        usernameDialog.setContentView(R.layout.bottom_sheet_username_dialog);
        usernameInput = usernameDialog.findViewById(R.id.usernameInput);
        usernameConfirmButton = usernameDialog.findViewById(R.id.confirm_button);
        // 自我介绍对话框
        introduceDialog = new BottomSheetDialog(context);
        introduceDialog.setContentView(R.layout.bottom_sheet_introduce_dialog);
        introduceInput = introduceDialog.findViewById(R.id.introduceInput);
        introduceConfirmButton = introduceDialog.findViewById(R.id.confirm_button);
        // 头像对话框中的按钮和一些元素
        avatarDialog = new BottomSheetDialog(context);
        avatarDialog.setContentView(R.layout.bottom_sheet_avatar_dialog);
        cameraButton = avatarDialog.findViewById(R.id.cameraButton);
        imageButton = avatarDialog.findViewById(R.id.ImageButton);
        // 性别对话框的内部元素
        sexDialog = new BottomSheetDialog(context);
        sexDialog.setContentView(R.layout.bottom_sheet_sex_dialog);
        maleLayout = sexDialog.findViewById(R.id.layout_male);
        femaleLayout = sexDialog.findViewById(R.id.layout_female);
        sexConfirmButton = sexDialog.findViewById(R.id.confirm_button);
        male = sexDialog.findViewById(R.id.ic_male);
        female = sexDialog.findViewById(R.id.ic_female);
    }
    public void initOnClickListener() {
        // 头像
        avatarCard.setOnClickListener(v -> {
            avatarDialog.show();
        });
        // 使用回调函数在UserInformationActivity打开相机和相册
        cameraButton.setOnClickListener(v -> {
            crossComponentListener.onOpenCamera(avatar);
            avatarDialog.cancel();
        });
        imageButton.setOnClickListener(v -> {
            crossComponentListener.onOpenGallery(avatar);
            avatarDialog.cancel();
        });
        // 性别
        sexCard.setOnClickListener(v -> {
            sexDialog.show();
        });
        // 用户名
        usernameCard.setOnClickListener(v -> {
            usernameDialog.show();
        });
        usernameConfirmButton.setOnClickListener(v -> {
            String val = usernameInput.getText().toString();
            confirmHandler("username", val);
        });
        // 自我介绍
        introduceCard.setOnClickListener(v -> {
            introduceDialog.show();
        });
        introduceConfirmButton.setOnClickListener(v -> {
            String val = introduceInput.getText().toString();
            confirmHandler("introduce", val);
        });
        // 性别对话框下面的
        sexConfirmButton.setOnClickListener(v -> {
            confirmHandler("sex", String.valueOf(chooseSex));
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
    // 对话框的确定按钮的事件
    public void confirmHandler(String key, String value) {
        HashMap<String, String> params = new HashMap<>();
        String id = SharedPreferencesUtils.getString(context, resourcesUtils.ID, null);
        params.put("id", id);
        params.put(key, value);
        HttpUtils.sendPostRequest(Api.UPDATE, params, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.d("fishCat", "confirmButton onFailure: " + e.getMessage());
            }
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String body = response.body().string();
                    Result result = HttpUtils.gson.fromJson(body, new TypeToken<Result<Object>>(){}.getType());
                    if (result.getCode() != 200) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                ToastUtils.show(context, result.getMsg());
                            }
                        });
                    } else {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                switch (key){
                                    case "sex" :
                                        SharedPreferencesUtils.saveString(context, resourcesUtils.SEX, String.valueOf(value));
                                        int i = Integer.parseInt(value);
                                        if (i == 1) {
                                            sex.setText("男");
                                        } else if (i == 0) {
                                            sex.setText("女");
                                        }
                                        sexDialog.cancel();
                                        break;
                                    case "username" :
                                        SharedPreferencesUtils.saveString(context, resourcesUtils.USERNAME, String.valueOf(value));
                                        username.setText(value);
                                        usernameDialog.cancel();
                                        break;
                                    case "introduce" :
                                        SharedPreferencesUtils.saveString(context, resourcesUtils.INTRODUCE, String.valueOf(value));
                                        introduce.setText(value);
                                        introduceDialog.cancel();
                                        break;
                                }
                            }
                        });
                    }
                }
            }
        });
    }
    public void initData(User user) {
        if (user != null) {
            String userAvatar = user.getAvatar();
            if (userAvatar != null) {
                Uri uri = Uri.parse(userAvatar);
                Glide.with(context)
                        .load(uri)
                        .override(80, 80)
                        .centerCrop()
                        .into(avatar);
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