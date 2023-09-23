package com.fish.photoshare.activities;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fish.photoshare.R;
import com.fish.photoshare.adapter.StarAdapter;
import com.fish.photoshare.common.Api;
import com.fish.photoshare.common.Result;
import com.fish.photoshare.pojo.PostRecord;
import com.fish.photoshare.utils.HttpUtils;
import com.fish.photoshare.utils.ResourcesUtils;
import com.fish.photoshare.utils.SharedPreferencesUtils;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class StarActivity extends AppCompatActivity {
    private ResourcesUtils resourcesUtils;
    private RecyclerView recyclerListStar;
    private StarAdapter starAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_star);
        initView();
        getData();
    }

    public void initView() {
        resourcesUtils = new ResourcesUtils(StarActivity.this);
        recyclerListStar = findViewById(R.id.recyclerListStar);
        recyclerListStar.setLayoutManager(new LinearLayoutManager(StarActivity.this));
        starAdapter = null;
        ImageView back = findViewById(R.id.icon_back);
        TextView title_text = findViewById(R.id.title_text);
        title_text.setText("收藏帖子");
        back.setOnClickListener(v -> {
            finish();
        });
    }

    public void getData() {
        HashMap<String, String> params = new HashMap<>();
        String id = SharedPreferencesUtils.getString(StarActivity.this, resourcesUtils.ID, null);
        params.put("userId", id);
        HttpUtils.sendGetRequest(Api.COLLECT, params, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.d("fishCat", "starCallback onFailure: " + e.getMessage());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String body = response.body().string();
                    Result<PostRecord> result = HttpUtils.gson.fromJson(body, new TypeToken<Result<PostRecord>>() {
                    }.getType());
                    if (result.getCode() != 200) {
                        Log.d("fishCat", "starCallback onResponse: code is not 200");
                    } else {
                        PostRecord records = result.getData();
                        if (records != null) {
                            starAdapter = new StarAdapter(StarActivity.this, records);
                        } else {
                            starAdapter = new StarAdapter(StarActivity.this, null);
                        }
                        new Handler(Looper.getMainLooper()).post(() -> {
                            recyclerListStar.setAdapter(starAdapter);
                        });
                    }
                }
            }
        });
    }
}