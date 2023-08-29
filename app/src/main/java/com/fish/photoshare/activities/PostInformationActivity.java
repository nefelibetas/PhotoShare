package com.fish.photoshare.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.fish.photoshare.R;
import com.fish.photoshare.adapter.ImageAdapter;
import com.fish.photoshare.adapter.PostCommentAdapter;
import com.fish.photoshare.common.Api;
import com.fish.photoshare.common.Result;
import com.fish.photoshare.pojo.CommentRecord;
import com.fish.photoshare.pojo.PostDetail;
import com.fish.photoshare.utils.HttpUtils;
import com.fish.photoshare.utils.ResourcesUtils;
import com.fish.photoshare.utils.SharedPreferencesUtils;
import com.google.android.material.button.MaterialButton;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class PostInformationActivity extends AppCompatActivity {
    private ResourcesUtils resourcesUtils;
    private PostDetail detail;
    private CommentRecord commentRecord;
    private PostCommentAdapter commentAdapter;
    private boolean isInit = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        resourcesUtils = new ResourcesUtils(PostInformationActivity.this);
        Bundle bundle = getIntent().getExtras();
        detail = bundle.getSerializable("detail", PostDetail.class);
        setContentView(R.layout.activity_post_information);
        initCommentData();
    }
    protected void initCommentData() {
        HashMap<String, String> params = new HashMap<>();
        params.put("shareId", detail.getId());
        HttpUtils.sendGetRequest(Api.FIRST, params, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.e("fishCat", "初始化评论信息 onFailure: ", e);
            }
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                     Result<CommentRecord> result = HttpUtils.gson.fromJson(response.body().string(), new TypeToken<Result<CommentRecord>>(){}.getType());
                    if (result.getCode() != 200) {
                        Log.e("fishCat", "初始化评论信息失败 onResponse: ", new Exception("初始化评论信息失败"));
                    } else {
                        if (result.getData() != null)
                            commentRecord = result.getData();
                        else
                            commentRecord = null;
                        new Handler(Looper.getMainLooper()).post(() -> {
                            if (!isInit) {
                                initView();
                            } else {
                                commentAdapter.notifyDataSetChanged();
                            }
                        });
                    }
                }
            }
        });
    }
    protected void setMain() {
        TextView titleText = findViewById(R.id.PostTitle);
        titleText.setText(detail.getTitle());
        TextView contentText = findViewById(R.id.PostContent);
        contentText.setText(detail.getContent());
    }
    protected void setRecyclerList() {
        RecyclerView recyclerListPostImages = findViewById(R.id.PostImages);
        ImageAdapter imageAdapter = new ImageAdapter(PostInformationActivity.this, detail.getImageUrlList());
        recyclerListPostImages.setLayoutManager(new GridLayoutManager(PostInformationActivity.this, 3));
        recyclerListPostImages.setAdapter(imageAdapter);
        RecyclerView recyclerListPostComment = findViewById(R.id.PostComment);
        recyclerListPostComment.setLayoutManager(new LinearLayoutManager(PostInformationActivity.this));
        commentAdapter = new PostCommentAdapter(PostInformationActivity.this, commentRecord);
        recyclerListPostComment.setAdapter(commentAdapter);
    }
    protected void commentHandler() {
        EditText postCommentInput = findViewById(R.id.PostCommentInput);
        MaterialButton postBottomButton = findViewById(R.id.PostBottomButton);
        postBottomButton.setOnClickListener(v -> {
            String comment = postCommentInput.getText().toString();
            HashMap<String, String> params = new HashMap<>();
            params.put("content", comment);
            String id = SharedPreferencesUtils.getString(PostInformationActivity.this, resourcesUtils.ID, null);
            params.put("userId", id);
            params.put("shareId", detail.getId());
            String username = SharedPreferencesUtils.getString(PostInformationActivity.this, resourcesUtils.USERNAME, null);
            params.put("userName", username);
            HttpUtils.sendPostRequest(Api.FIRST, params, new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    Log.e("fishCat", "onFailure: ", e);
                }
                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    if (response.isSuccessful()) {
                        Result<Objects> result = HttpUtils.gson.fromJson(response.body().string(), HttpUtils.resultType);
                        if (result.getCode() != 200) {
                            Log.e("fishCat", "发布一级评论出错 onResponse: ", new Exception("发布一级评论出错"));
                        } else {
                            Log.d("fishCat", "发布一级评论成功 onResponse: " + result);
                            postCommentInput.setText("");
                            initCommentData();
                        }
                    }
                }
            });
        });
    }
    protected void initView() {
        ImageView back = findViewById(R.id.icon_back);
        back.setOnClickListener(v -> {
            finish();
        });
        setMain();
        setRecyclerList();
        commentHandler();
        isInit = true;
    }
}