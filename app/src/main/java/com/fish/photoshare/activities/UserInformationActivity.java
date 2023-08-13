package com.fish.photoshare.activities;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.bumptech.glide.Glide;
import com.fish.photoshare.R;
import com.fish.photoshare.common.GalleryAndCameraListenerForAvatar;
import com.fish.photoshare.common.Result;
import com.fish.photoshare.common.onSuccessBindUser;
import com.fish.photoshare.fragments.user.UserHomeFragment;
import com.fish.photoshare.pojo.Images;
import com.fish.photoshare.utils.FileUtils;
import com.fish.photoshare.utils.HttpUtils;
import com.fish.photoshare.utils.ResourcesUtils;
import com.fish.photoshare.utils.SharedPreferencesUtils;
import com.fish.photoshare.utils.ToastUtils;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class UserInformationActivity extends AppCompatActivity implements GalleryAndCameraListenerForAvatar, onSuccessBindUser {
    // launcher
    private ActivityResultLauncher galleryLauncher;
    private ActivityResultLauncher<Uri> cameraLauncher;
    // 用来拿到Key的
    private ResourcesUtils resourcesUtils;
    private FragmentManager manager;
    private UserHomeFragment userHomeFragment;
    // 用户头像，从Model传来的
    private ShapeableImageView user_avatar;
    // 返回按钮
    private ImageView iv_back;
    // 从本地选取的图片Uri
    private Uri ImageUri;
    // 回调方法
    private Callback uploadCallback;
    private Callback updateCallback;
    // 上传后返回的Image对象，有code和ImageList
    private Images images;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_information);
        initView();
        initCallBack();
        initLauncher();
        userHomeFragment = UserHomeFragment.newInstance(this);
        manager.beginTransaction()
                .add(R.id.userFragmentContainer, userHomeFragment)
                .commit();
    }
    public void initView() {
        // ResourcesUtils
        resourcesUtils = new ResourcesUtils(UserInformationActivity.this);
        // manager
        manager = getSupportFragmentManager();
        // 顶部返回按钮
        iv_back = findViewById(R.id.icon_back);
        initOnclickListener();
    }
    public void initOnclickListener() {
        iv_back.setOnClickListener(v -> {
            finish();
        });
    }
    public void initLauncher() {
        galleryLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), result -> {
            if (result != null) {
                ImageUri = result;
                File file = FileUtils.getFileFromUri(UserInformationActivity.this, ImageUri);
                ArrayList<File> fileList = new ArrayList<>();
                fileList.add(file);
                // 这里考虑到还有别的地方会用到上传文件和从Uri获取文件操作，因此进行封装;
                FileUtils.uploadImage(fileList, uploadCallback);
            }
        });
        cameraLauncher = registerForActivityResult(new ActivityResultContracts.TakePicture(), result -> {
            // 这里得到的是是否拍照成功的布尔值,所以定义了外部变量ImageUri接收Uri，在这里处理
            if (result) {
                File file = FileUtils.getFileFromUri(UserInformationActivity.this, ImageUri);
                ArrayList<File> fileList = new ArrayList<>();
                fileList.add(file);
                FileUtils.uploadImage(fileList, uploadCallback);
            }
        });
    }
    public void initCallBack() {
        updateCallback = new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.d("fishCat", "uploadImage onFailure: " + e.getMessage());
            }
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String body = response.body().string();
                    Result result = HttpUtils.gson.fromJson(body, HttpUtils.resultType);
                    if (result.getCode() != 200) {
                        ToastUtils.show(UserInformationActivity.this, result.getMsg());
                    } else {
                        String userAvatarUrl = images.getImageUrlList().get(0);
                        Log.d("fishCat", "updateCallback onResponse: " + result.toString());
                        SharedPreferencesUtils.saveString(UserInformationActivity.this, resourcesUtils.AVATAR, userAvatarUrl);
                        onSuccessBindUser(userAvatarUrl);
                    }
                }
            }
        };
        uploadCallback = new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.d("fishCat", "uploadImage onFailure: " + e.getMessage());
            }
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String body = response.body().string();
                    Result<Images> result = HttpUtils.gson.fromJson(body, new TypeToken<Result<Images>>(){}.getType());
                    if (result.getCode() != 200) {
                        ToastUtils.show(UserInformationActivity.this, result.getMsg());
                    } else {
                        images = result.getData();
                        Log.d("fishCat", "uploadCallback onResponse: " + images.toString());
                        String userAvatarUrl = images.getImageUrlList().get(0);
                        FileUtils.bindUser(userAvatarUrl, updateCallback, UserInformationActivity.this);
                    }
                }
            }
        };

    }
    @Override
    public void onOpenGallery(ShapeableImageView avatar) {
        if (user_avatar == null) {
            user_avatar = avatar;
        }
        galleryLauncher.launch("image/*");
    }
    @Override
    public void onOpenCamera(ShapeableImageView avatar) {
        if (user_avatar == null) {
            user_avatar = avatar;
        }
        cameraLauncher.launch(createImageUri(UserInformationActivity.this));
    }
    @Override
    public void onSuccessBindUser(String avatarUrl) {
        // 这里拿到网络Uri
        Uri networkImageUri = Uri.parse(avatarUrl);
        // 通过模拟主线程来修改UI
        new Handler(Looper.getMainLooper()).post(() -> Glide.with(UserInformationActivity.this)
                .load(networkImageUri)
                .override(80, 80)
                .centerCrop()
                .into(user_avatar));
    }
    public Uri createImageUri(Context context) {
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmm", Locale.getDefault()).format(new Date());
        String imageFileName = "IMG_" + timeStamp + ".jpg";
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, imageFileName);
        ImageUri = context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        return ImageUri;
    }
}