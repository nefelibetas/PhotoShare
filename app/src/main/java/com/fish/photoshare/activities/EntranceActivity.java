package com.fish.photoshare.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;

import com.fish.photoshare.R;
import com.fish.photoshare.common.Api;
import com.fish.photoshare.common.Result;
import com.fish.photoshare.pojo.User;
import com.fish.photoshare.utils.FormatUtils;
import com.fish.photoshare.utils.HttpUtils;
import com.fish.photoshare.utils.ToastUtils;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class EntranceActivity extends AppCompatActivity implements View.OnClickListener {
    private TextInputEditText usernameInput;
    private TextInputEditText passwordInput;
    private MaterialCheckBox rememberPassword;
    private MaterialButton signInButton;
    private MaterialButton registerButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entrance);
        initView();
    }
    public void initView(){
        usernameInput = findViewById(R.id.et_username);
        passwordInput = findViewById(R.id.et_password);
        rememberPassword = findViewById(R.id.mc_remember);
        signInButton = findViewById(R.id.mb_signIn);
        registerButton = findViewById(R.id.mb_register);
        signInButton.setOnClickListener(this);
        registerButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        String  username = usernameInput.getText().toString();
        String  password = passwordInput.getText().toString();
        if (username.isEmpty() || password.isEmpty()){
            ToastUtils.show(EntranceActivity.this, "输入不能为空");
            return;
        }
        if (id == R.id.mb_signIn)
            signInHandler(username, password);
        else if (id == R.id.mb_register)
            registerHandler(username, password);
    }
    public void signInHandler(String username, String password){
        HashMap<String, String> params = new HashMap<>();
        params.put("username", username);
        params.put("password", password);
        String newUrl = HttpUtils.getRequestHandler(Api.LOGIN, params);
        HttpUtils.sendPostRequest(newUrl, null, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
                Log.d("fishCat", "Login onFailure" + e.getMessage());
            }
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    Intent intent = new Intent(EntranceActivity.this, MainActivity.class);
                    // 从string中获取Key
                    String fileNameKey = getResources().getString(R.string.shared_preferences_file_name);
                    String userNameKey = getResources().getString(R.string.user_name);
                    String userPasswordKey = getResources().getString(R.string.user_password);
                    // 将账密传到主页面，会在UserFragment渲染
                    Bundle bundle = new Bundle();
                    // 从返回体中获取字符串数据
                    String body = response.body().string();
                    Result<User> res = HttpUtils.gson.fromJson(body, HttpUtils.jsonType);
                    Log.d("fishCat", "Login onResponse result data: " + res);
                    if (res.getCode() == 500) {
                        ToastUtils.show(EntranceActivity.this, res.getMsg());
                    } else {
                        bundle.putString("loginResult", res.toString());
                        intent.putExtras(bundle);
                        // SharedPreferences存储账密，启动时自检是否保存有账密，有的话就直接登陆获取数据。
                        // 没有则跳转到此页面
                        SharedPreferences preferences = getSharedPreferences(fileNameKey, Context.MODE_PRIVATE);
                        SharedPreferences.Editor edit = preferences.edit();
                        if (rememberPassword.isChecked()) {
                            edit.putString(userNameKey, username);
                            edit.putString(userPasswordKey, password);
                            edit.apply();
                        } else {
                            edit.remove(userNameKey);
                            edit.remove(userPasswordKey);
                            edit.apply();
                        }
                        startActivity(intent);
                    }
                }
            }
        });
    }
    public void registerHandler(String username, String password){
        if (FormatUtils.checkPassword(password)) {
            HashMap<String, String> params = new HashMap<>();
            params.put("username", username);
            params.put("password", password);
            HttpUtils.sendPostRequest(Api.REGISTER, params, new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    Log.d("fishCat", "register onFailure: " + e.getMessage());
                }
                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    if (response.isSuccessful()) {
                        String body = response.body().string();
                        Result<Object> res = HttpUtils.gson.fromJson(body, HttpUtils.jsonType);
                        Log.d("fishCat", "register onResponse data: " + res);
                        if (res.getCode() == 500) {
                            ToastUtils.show(EntranceActivity.this, res.getMsg());
                        }
                    }
                }
            });
        }
    }

}