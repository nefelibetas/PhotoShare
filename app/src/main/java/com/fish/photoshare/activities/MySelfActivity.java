package com.fish.photoshare.activities;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fish.photoshare.R;
import com.fish.photoshare.adapter.MyselfAdapter;
import com.fish.photoshare.common.Api;
import com.fish.photoshare.common.Result;
import com.fish.photoshare.pojo.Record;
import com.fish.photoshare.utils.HttpUtils;
import com.fish.photoshare.utils.ResourcesUtils;
import com.fish.photoshare.utils.SharedPreferencesUtils;
import com.fish.photoshare.utils.ToastUtils;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MySelfActivity extends AppCompatActivity {
    private ResourcesUtils resourcesUtils;
    private Callback changeCallback;
    private Callback deleteCallback;
    private RecyclerView recyclerListMyself;
    private MyselfAdapter myselfAdapter;
    private ImageView back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myself);
        initCallback();
        initParams();
        getData();
    }
    public void initCallback() {
        changeCallback = new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.d("fishCat", "changeCallback onFailure: " + e.getMessage());
            }
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String body = response.body().string();
                    Result<Object> result = HttpUtils.gson.fromJson(body, HttpUtils.resultType);
                    if (result.getCode() != 200) {
                        Log.d("fishCat", "changeCallback onResponse: code is not 200 and result " + result);
                    } else {
                        Log.d("fishCat", "onResponse: " + result);
                        new Handler(Looper.getMainLooper()).post(() -> {
                            ToastUtils.show(MySelfActivity.this, "发布成功");
                        });
                    }
                }
            }
        };
        deleteCallback = new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.d("fishCat", "deleteCallback onFailure: " + e.getMessage());
            }
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String body = response.body().string();
                    Result<Object> result = HttpUtils.gson.fromJson(body, HttpUtils.resultType);
                    if (result.getCode() != 200) {
                        Log.d("fishCat", "deleteCallback onResponse: code is not 200 and result " + result);
                    } else {
                        Log.d("fishCat", "onResponse: " + result);
                        new Handler(Looper.getMainLooper()).post(() -> {
                            ToastUtils.show(MySelfActivity.this, "删除成功");
                        });
                    }
                }
            }
        };
    }
    public void initParams() {
        resourcesUtils = new ResourcesUtils(MySelfActivity.this);
        recyclerListMyself = findViewById(R.id.recyclerListMyself);
        recyclerListMyself.setLayoutManager(new LinearLayoutManager(MySelfActivity.this));
        back = findViewById(R.id.icon_back);
        back.setOnClickListener(v -> {
            finish();
        });
    }
    public void getData() {
        HashMap<String, String> params = new HashMap<>();
        String id = SharedPreferencesUtils.getString(MySelfActivity.this, resourcesUtils.ID, null);
        params.put("userId", id);
        HttpUtils.sendGetRequest(Api.MYSELF, params, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.d("fishCat", "myselfCallback onFailure: " + e.getMessage());
            }
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String body = response.body().string();
                    Result<Record> result = HttpUtils.gson.fromJson(body, new TypeToken<Result<Record>>(){}.getType());
                    if (result.getCode() != 200) {
                        Log.d("fishCat", "myselfCallback onResponse: code is not 200");
                    } else {
                        Record records = result.getData();
                        new Handler(Looper.getMainLooper()).post(() -> {
                            myselfAdapter = new MyselfAdapter(MySelfActivity.this, changeCallback, deleteCallback, records);
                            recyclerListMyself.setAdapter(myselfAdapter);
                        });
                    }
                }
            }
        });
    }
}