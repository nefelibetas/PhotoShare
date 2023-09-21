package com.fish.photoshare.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fish.photoshare.R;
import com.fish.photoshare.adapter.HomeAdapter;
import com.fish.photoshare.common.Api;
import com.fish.photoshare.common.Result;
import com.fish.photoshare.common.onChangePostState;
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

public class HomeFragment extends Fragment implements onChangePostState {
    private ResourcesUtils resourcesUtils;
    private HomeAdapter homeAdapter;
    private RecyclerView HomeRecyclerList;
    private PostRecord record;
    public HomeFragment() {
    }

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }
    public void initView(View rootView) {
        HomeRecyclerList = rootView.findViewById(R.id.recyclerListHome);
        HomeRecyclerList.setLayoutManager(new LinearLayoutManager(getContext()));
        homeAdapter = null;
    }
    public void getShare() {
        String id = SharedPreferencesUtils.getString(getContext(), resourcesUtils.ID, null);
        HashMap<String, String> params = new HashMap<>();
        params.put("userId", id);
        HttpUtils.sendGetRequest(Api.SHARE, params, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.e("fishCat", "getShare onFailure: " + e.getMessage());
            }
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String body = response.body().string();
                    Result<PostRecord> result = HttpUtils.gson.fromJson(body, new TypeToken<Result<PostRecord>>() {
                    }.getType());
                    if (result.getCode() != 200) {
                        Log.e("fishCat", "getShare onResponse: " + result);
                    } else {
                        record = result.getData();
                        new Handler(Looper.getMainLooper()).post(() -> {
                            homeAdapter = new HomeAdapter(getContext(), record, HomeFragment.this);
                            HomeRecyclerList.setAdapter(homeAdapter);
                        });
                    }
                }
            }
        });
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        resourcesUtils = new ResourcesUtils(getContext());
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        initView(rootView);
        getShare();
        return rootView;
    }
    @Override
    public void onChangePostState(int position) {
        String id = SharedPreferencesUtils.getString(getContext(), resourcesUtils.ID, null);
        HashMap<String, String> params = new HashMap<>();
        params.put("userId", id);
        HttpUtils.sendGetRequest(Api.SHARE, params, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.e("fishCat", "getShare onFailure: " + e.getMessage());
            }
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String body = response.body().string();
                    Result<PostRecord> result = HttpUtils.gson.fromJson(body, new TypeToken<Result<PostRecord>>() {
                    }.getType());
                    if (result.getCode() != 200) {
                        Log.e("fishCat", "getShare onResponse: " + result);
                    } else {
                        new Handler(Looper.getMainLooper()).post(() -> {
                            record = result.getData();
                            homeAdapter.updateData(record);
                            homeAdapter.notifyItemChanged(position);
                        });
                    }
                }
            }
        });
    }
}