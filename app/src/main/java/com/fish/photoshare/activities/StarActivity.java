package com.fish.photoshare.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.fish.photoshare.R;
import com.fish.photoshare.common.Api;
import com.fish.photoshare.common.Result;
import com.fish.photoshare.pojo.Record;
import com.fish.photoshare.utils.HttpUtils;
import com.fish.photoshare.utils.ResourcesUtils;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class StarActivity extends AppCompatActivity {
    private Callback starCallback;
    private ResourcesUtils resourcesUtils;
    private Integer current = 0;
    private Integer size = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_star);
        initParams();
        initCallback();
        getData();
    }
    public void initCallback() {
        starCallback = new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.d("fishCat", "starCallback onFailure: " + e.getMessage());
            }
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String body = response.body().string();
                    Result<Record> result = HttpUtils.gson.fromJson(body, new TypeToken<Result<Record>>(){}.getType());
                    if (result.getCode() != 200) {
                        Log.d("fishCat", "callback onResponse: code is not 200");
                    } else {
                        Record record = result.getData();
                        Log.d("fishCat", "starCallback onResponse: " + record.toString());
                    }
                }
            }
        };
    }
    public void initParams() {
        resourcesUtils = new ResourcesUtils(StarActivity.this);
    }
    public void getData(){
        HashMap<String, String> params = new HashMap<>();
        String id = resourcesUtils.ID;
        params.put("userId", id);
        // putData into map
        HttpUtils.sendPostRequest(Api.MYSELF, params, starCallback);
    }
}