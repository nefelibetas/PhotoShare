package com.fish.photoshare.activities;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ImageView;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.fish.photoshare.R;
import com.fish.photoshare.common.CrossComponentListener;
import com.fish.photoshare.fragments.user.UserHomeFragment;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class UserInformationActivity extends AppCompatActivity implements CrossComponentListener {
    private ActivityResultLauncher galleryLauncher;
    private ActivityResultLauncher<Uri> cameraLauncher;
    private FragmentManager manager;
    private UserHomeFragment userHomeFragment;
    private ImageView iv_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_information);
        initView();
        initLauncher();
        userHomeFragment = UserHomeFragment.newInstance(this);
        manager.beginTransaction()
                .add(R.id.userFragmentContainer, userHomeFragment)
                .commit();
    }
    public void initView() {
        // manager
        manager = getSupportFragmentManager();
        // 顶部返回按钮
        iv_back = findViewById(R.id.icon_back);
        initOnclickListener();
    }
    public void initLauncher() {
        galleryLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), result -> {
            // todo: 在这里处理选择的图片 URI
            if (result != null) {
                // todo: 对选择的图片 URI 进行处理
            }
        });
        cameraLauncher = registerForActivityResult(new ActivityResultContracts.TakePicture(), result -> {
            if (result != null) {
                // todo: 处理拍照结果
            }
        });
    }
    public void initOnclickListener() {
        iv_back.setOnClickListener(v -> {
            finish();
        });
    }
    private Uri createImageUri() {
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmm", Locale.getDefault()).format(new Date());
        String imageFileName = "IMG_" + timeStamp + ".jpg";
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, imageFileName);
        return getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
    }
    @Override
    public void onOpenGallery() {
        galleryLauncher.launch("image/*");
    }
    @Override
    public void onOpenCamera() {
        cameraLauncher.launch(createImageUri());
    }
}