package com.fish.photoshare.fragments;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.fish.photoshare.R;
import com.fish.photoshare.adapter.PublishAdapter;
import com.fish.photoshare.common.Api;
import com.fish.photoshare.common.GalleryAndCameraListenerForPublish;
import com.fish.photoshare.common.RequestHandler;
import com.fish.photoshare.common.Result;
import com.fish.photoshare.common.onButtonClick;
import com.fish.photoshare.models.PublishModel;
import com.fish.photoshare.pojo.Images;
import com.fish.photoshare.utils.FileUtils;
import com.fish.photoshare.utils.HttpUtils;
import com.fish.photoshare.utils.ResourcesUtils;
import com.fish.photoshare.utils.SharedPreferencesUtils;
import com.fish.photoshare.utils.ToastUtils;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class PublishFragment extends Fragment implements GalleryAndCameraListenerForPublish, onButtonClick {
    private PublishModel publishModel;
    private ResourcesUtils resourcesUtils;
    private String ImageCode;
    private ActivityResultLauncher galleryLauncher;
    private ActivityResultLauncher<Uri> cameraLauncher;
    private ArrayList<File> fileList = null;
    private Uri imageUri;
    private Callback uploadCallback;
    private Callback saveCallback;
    private Callback saveAndPostCallback;
    private PublishAdapter adapter = null;
    private int flag = 0;
    private HashMap<String, String> saveParams = new HashMap<>();
    public PublishFragment() {}
    public static PublishFragment newInstance() {
        PublishFragment fragment = new PublishFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_publish, container, false);
        resourcesUtils = new ResourcesUtils(getContext());
        initLauncher();
        initCallback();
        initModel(rootView);
        return rootView;
    }
    public void initModel(View rootView) {
        if (fileList == null) {
            fileList = new ArrayList<>();
        }
        if (adapter == null) {
            adapter = new PublishAdapter(getContext(), fileList);
        }
        publishModel = new PublishModel(getContext(), adapter, this, this);
        publishModel.initView(rootView);
        publishModel.initOnClickListener();
        publishModel.setAdapter();
    }
    public void initLauncher() {
        galleryLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), result -> {
            if (result != null) {
                File file = FileUtils.getFileFromUri(getContext(), result);
                if (fileList.size() <= 9) {
                    fileList.add(file);
                    adapter.notifyDataSetChanged();
                } else {
                    ToastUtils.show(getContext(), "最多上传九张");
                }
            }
        });
        cameraLauncher = registerForActivityResult(new ActivityResultContracts.TakePicture(), result -> {
            if (result) {
                File file = FileUtils.getFileFromUri(getContext(), imageUri);
                if (fileList.size() <= 8) {
                    fileList.add(file);
                    adapter.notifyDataSetChanged();
                } else {
                    ToastUtils.show(getContext(), "最多上传九张");
                }
            }
        });
    }
    public void initCallback() {
        saveCallback = new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.d("fishCat", "saveCallback onFailure: " + e.getMessage());
            }
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String body = response.body().string();
                    Result<Object> result = HttpUtils.gson.fromJson(body, HttpUtils.resultType);
                    if (result.getCode() != 200) {
                        Log.d("fishCat", "saveCallback onResponse not 200: " + result);
                    } else {
                        new Handler(Looper.getMainLooper()).post(() -> {
                            fileList.clear();
                            publishModel.clear();
                            adapter.notifyDataSetChanged();
                        });
                    }
                }
            }
        };
        saveAndPostCallback = new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.d("fishCat", "saveCallback onFailure: " + e.getMessage());
            }
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String body = response.body().string();
                    Result<Object> result = HttpUtils.gson.fromJson(body, HttpUtils.resultType);
                    if (result.getCode() != 200) {
                        Log.d("fishCat", "saveAndPostCallback onResponse not 200: " + result);
                    } else {
                        new Handler(Looper.getMainLooper()).post(() -> {
                            fileList.clear();
                            publishModel.clear();
                            adapter.notifyDataSetChanged();
                        });
                    }
                }
            }
        };
        uploadCallback = new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.d("fishCat", "uploadCallback onFailure: " + e.getMessage());
            }
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String body = response.body().string();
                   Result<Images> result = HttpUtils.gson.fromJson(body, new TypeToken<Result<Images>>(){}.getType());
                   if (result.getCode() != 200) {
                       Log.d("fishCat", "uploadCallback onResponse !200: " + result);
                   } else {
                       Images images = result.getData();
                       ImageCode = images.getImageCode();
                       new Handler(Looper.getMainLooper()).post(() -> {
                            ToastUtils.show(getContext(), "上传成功");
                       });
                       if (flag == 0) {
                           saveRequest();
                       } else if (flag == 1) {
                           saveAndPostRequest();
                       }
                   }
                }
            }
        };
    }
    public void saveRequest() {
        HashMap<String, String> params = new HashMap<>();
        String idKey = resourcesUtils.ID;
        String textTitleInput = publishModel.titleInput.getText().toString();
        if (textTitleInput == null || "".equals(textTitleInput)) {
            ToastUtils.show(getContext(), "标题未输入");
            return;
        }
        String textContentInput = publishModel.contentInput.getText().toString();
        if (textContentInput == null || "".equals(textContentInput)) {
            ToastUtils.show(getContext(), "内容未输入");
            return;
        }
        String id = SharedPreferencesUtils.getString(getContext(), idKey, null);
        params.put("id", id);
        params.put("pUserId", id);
        params.put("title", textTitleInput);
        params.put("content", textContentInput);
        params.put("imageCode", ImageCode);
        saveParams = params;
        HttpUtils.sendPostRequest(Api.SAVE, params, saveCallback);
    }
    // 调用上方保存请求后，再发起一个修改保存状态为发布状态的请求
    public void saveAndPostRequest() {
        HttpUtils.sendPostRequest(Api.ADD, saveParams, saveAndPostCallback);
    }
    @Override
    public void onOpenGallery() {
        galleryLauncher.launch("image/*");
    }
    @Override
    public void onOpenCamera() {
        cameraLauncher.launch(createImageUri(getContext()));
    }
    public Uri createImageUri(Context context) {
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmm", Locale.getDefault()).format(new Date());
        String imageFileName = "IMG_" + timeStamp + ".jpg";
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, imageFileName);
        imageUri = context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        return imageUri;
    }
    // 点击的回调函数
    @Override
    public void onSaveClick() {
        if (fileList.size() != 0) {
            flag = 0;
            FileUtils.uploadImage(fileList, uploadCallback);
        } else
            ToastUtils.show(getContext(), "图片未选择");

    }
    @Override
    public void onSaveAndPostClick() {
        if (fileList.size() != 0) {
            flag = 1;
            FileUtils.uploadImage(fileList, uploadCallback);
        } else
            ToastUtils.show(getContext(), "图片未选择");

    }
}