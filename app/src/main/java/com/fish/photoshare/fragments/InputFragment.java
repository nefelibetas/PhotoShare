package com.fish.photoshare.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.SavedStateHandle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fish.photoshare.R;
import com.fish.photoshare.common.Api;
import com.fish.photoshare.common.CrossComponentHandler;
import com.fish.photoshare.common.OnCrossComponentClickListener;
import com.fish.photoshare.common.Result;
import com.fish.photoshare.pojo.User;
import com.fish.photoshare.utils.HttpUtils;
import com.fish.photoshare.utils.SharedPreferencesUtils;
import com.fish.photoshare.utils.ToastUtils;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class InputFragment extends Fragment {
    private static final String INPUT = "INPUT";
    private String input;
    private TextInputEditText inputEditText;
    private CrossComponentHandler handler;
    public InputFragment() {}
    public static InputFragment newInstance(String input) {
        InputFragment fragment = new InputFragment();
        Bundle args = new Bundle();
        args.putString(INPUT, input);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            input = getArguments().getString(INPUT);
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_input, container, false);
        initView(rootView);
        initCrossComponentListener();
        return rootView;
    }
    public void initView(View rootView) {
        inputEditText = rootView.findViewById(R.id.Input);
        inputEditText.setHint(input);
    }
    public void initCrossComponentListener() {
        if (handler == null) {
            handler = new CrossComponentHandler(new OnCrossComponentClickListener() {
                @Override
                public void OnCrossComponentClick(String code) {
                    if (code.equals("update")) {
//                        updateHandler();
                    }
                }
            });
        }
    }
    public void updateHandler() {
        HashMap<String, String> params = new HashMap<>();
        String userInputText = inputEditText.getText().toString();
        String id = SharedPreferencesUtils.getString(getContext(), getResources().getString(R.string.user_id), null);
        if (id != null && id.equals("")) {
            params.put(input, userInputText);
            params.put("id", id);
        } else {
            Log.d("fishCat", "updateHandler error in : " + id);
            ToastUtils.show(getContext(), "id为空");
        }
        HttpUtils.sendPostRequest(Api.UPDATE, params, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.d("fishCat", "updateHandler onFailure: " + e.getMessage());
            }
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String body = response.body().string();
                    Result<User> result = HttpUtils.gson.fromJson(body, new TypeToken<Result<Object>>(){}.getType());
                    if (result.getCode() == 500) {
                        Log.d("fishCat", "updateHandler onResponse 500: " + result);
                    } else {
                        String string = null;
                        String username = getResources().getString(R.string.user_name);
                        String introduce = getResources().getString(R.string.user_introduce);
                        if (input.equals("username")) {
                            string = SharedPreferencesUtils.getString(getContext(), username, null);
                        } else if (input.equals("introduce")) {
                            string = SharedPreferencesUtils.getString(getContext(), introduce, null);
                        }
                        if (string != null && !string.equals("")) {
                            SharedPreferencesUtils.saveString(getContext(), username, string);
                        }
                    }
                }
            }
        });
    }
}