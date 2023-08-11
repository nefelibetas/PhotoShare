package com.fish.photoshare.activities;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.hardware.usb.UsbRequest;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.bumptech.glide.Glide;
import com.fish.photoshare.R;
import com.fish.photoshare.common.CrossComponentListener;
import com.fish.photoshare.fragments.user.UserHomeFragment;
import com.fish.photoshare.utils.ResourcesUtils;
import com.fish.photoshare.utils.SharedPreferencesUtils;
import com.google.android.material.imageview.ShapeableImageView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class UserInformationActivity extends AppCompatActivity implements CrossComponentListener{
    // launcher
    private ActivityResultLauncher galleryLauncher;
    private ActivityResultLauncher<Uri> cameraLauncher;
    private ResourcesUtils resourcesUtils;
    private FragmentManager manager;
    private UserHomeFragment userHomeFragment;
    private ShapeableImageView user_avatar;
    private ImageView iv_back;
    private Uri ImageUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_information);
        initView();
        initLauncher();
        userHomeFragment = UserHomeFragment.newInstance();
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
        // todo: 从这里得到的Uri，可以直接定位到文件，并且可以拿到网络资源，因此，从服务器上拿图片就用这个
        // todo: 然后从本地传上的图片需要从Uri转化为File对象
        // todo: 参数：fileList 类型为 ArrayList<File> 然后放进去，所以要做的就是把Uri转化为File
        // todo: 然后保存在本地，每次加载就读取，用同样的方法，把UserFragment的头像一同改变
        galleryLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), result -> {
            if (result != null) {
                String realPath = getRealPathFromUri(result);
                File image = new File(realPath);
                ArrayList<File> fileList = new ArrayList<>();
                fileList.add(image);
                //Glide.with(UserInformationActivity.this)
                        //.load(result)
                        //.override(80,80)
                        //.centerCrop()
                // updateAvatar(fileList);
            }
        });
        cameraLauncher = registerForActivityResult(new ActivityResultContracts.TakePicture(), result -> {
            // 这里得到的是是否拍照成功的布尔值,所以定义了外部变量ImageUri接收Uri，在这里处理
            if (result != null) {
                String realPath = getRealPathFromUri(ImageUri);
                File image = new File(realPath);
                ArrayList<File> fileList = new ArrayList<>();
                fileList.add(image);
                //Glide.with(UserInformationActivity.this)
                        //.load(result)
                        //.override(80,80)
                        //.centerCrop()
                // updateAvatar(fileList);
            }
        });
    }
    public void updateAvatar(ArrayList<File> fileList) {
        HashMap<String, String> params = new HashMap<>();
        String id = SharedPreferencesUtils.getString(UserInformationActivity.this, resourcesUtils.ID, null);
        params.put("id", id);

    }
    @Override
    public void onOpenGallery(ShapeableImageView avatar) {
        if (user_avatar == null) {
            user_avatar = avatar;
        }
        galleryLauncher.launch("image/**");
    }
    @Override
    public void onOpenCamera(ShapeableImageView avatar) {
        if (user_avatar == null) {
            user_avatar = avatar;
        }
        cameraLauncher.launch(createImageUri());
    }
    private Uri createImageUri() {
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmm", Locale.getDefault()).format(new Date());
        String imageFileName = "IMG_" + timeStamp + ".jpg";
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, imageFileName);
        ImageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        return ImageUri;
    }
    // 通过Uri查询真实路径
    private String getRealPathFromUri(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String filePath = cursor.getString(columnIndex);
        cursor.close();
        return filePath;
    }
}