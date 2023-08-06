package com.fish.photoshare.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.fish.photoshare.R;
import com.fish.photoshare.common.Api;
import com.fish.photoshare.common.Result;
import com.fish.photoshare.pojo.User;
import com.fish.photoshare.utils.FormatUtils;
import com.fish.photoshare.utils.HttpUtils;
import com.fish.photoshare.utils.SharedPreferencesUtils;
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
        getSharedPreferencesData();
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

    private void getSharedPreferencesData() {
        String usernameKey = getResources().getString(R.string.user_name);
        String passwordKey = getResources().getString(R.string.user_password);
        String username = SharedPreferencesUtils.getString(EntranceActivity.this, usernameKey, null);
        String password = SharedPreferencesUtils.getString(EntranceActivity.this, passwordKey, null);
        if ((username != null && password != null) && !TextUtils.isEmpty(username) && !TextUtils.isEmpty(password)) {
            usernameInput.setText(username);
            passwordInput.setText(password);
        }
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

    private void signInHandler(String username, String password){
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
                    // 从string中获取Key
                    String userNameKey = getResources().getString(R.string.user_name);
                    String userPasswordKey = getResources().getString(R.string.user_password);
                    String userSexKey = getResources().getString(R.string.user_sex);
                    String userIdKey = getResources().getString(R.string.user_id);
                    String userAvatarKey = getResources().getString(R.string.user_avatar);
                    String userIntroduceKey = getResources().getString(R.string.user_introduce);
                    String createTimeKey = getResources().getString(R.string.user_createTime);
                    String lastUpdateTimeKey = getResources().getString(R.string.user_lastUpdateTime);
                    // 将账密传到主页面，会在UserFragment渲染
                    Bundle bundle = new Bundle();
                    // 获取响应体内的数据，转化成字符串再转化成指定类型的数据
                    String body = response.body().string();
                    Result<User> result = HttpUtils.gson.fromJson(body, new TypeToken<Result<User>>(){}.getType());
                    Log.d("fishCat", "Login onResponse result data: " + result);
                    if (result.getCode() == 500) {
                        ToastUtils.show(EntranceActivity.this, result.getMsg());
                    } else {
                        // 获取到的数据没有异常就可以跳转到主界面，然后传递数据
                        Intent intent = new Intent(EntranceActivity.this, MainActivity.class);
                        bundle.putString("loginResult", HttpUtils.gson.toJson(result.getData()));
                        intent.putExtras(bundle);
                        User user = result.getData();
                        // 登陆后保存登陆状态，有修改再获取数据
                        if (rememberPassword.isChecked()) {
                            SharedPreferencesUtils.saveString(EntranceActivity.this, userNameKey, username);
                            SharedPreferencesUtils.saveString(EntranceActivity.this, userPasswordKey, password);
                            SharedPreferencesUtils.saveString(EntranceActivity.this, userIdKey, user.getId());
                            SharedPreferencesUtils.saveString(EntranceActivity.this, userSexKey, user.getSex());
                            SharedPreferencesUtils.saveString(EntranceActivity.this, userAvatarKey, user.getAvatar());
                            SharedPreferencesUtils.saveString(EntranceActivity.this, userIntroduceKey, user.getIntroduce());
                            SharedPreferencesUtils.saveString(EntranceActivity.this, createTimeKey, user.getCreateTime().toString());
                            SharedPreferencesUtils.saveString(EntranceActivity.this, lastUpdateTimeKey, user.getLastUpdateTime().toString());
                        } else {
                            SharedPreferencesUtils.remove(EntranceActivity.this, userNameKey);
                            SharedPreferencesUtils.remove(EntranceActivity.this, userIdKey);
                            SharedPreferencesUtils.remove(EntranceActivity.this, userAvatarKey);
                            SharedPreferencesUtils.remove(EntranceActivity.this, userSexKey);
                            SharedPreferencesUtils.remove(EntranceActivity.this, userPasswordKey);
                            SharedPreferencesUtils.remove(EntranceActivity.this, userIntroduceKey);
                            SharedPreferencesUtils.remove(EntranceActivity.this, createTimeKey);
                            SharedPreferencesUtils.remove(EntranceActivity.this, lastUpdateTimeKey);
                        }
                        startActivity(intent);
                    }
                }
            }
        });
    }

    private void registerHandler(String username, String password){
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
                        Result<Object> res = HttpUtils.gson.fromJson(body, Result.class);
                        Log.d("fishCat", "register onResponse data: " + res);
                        if (res.getCode() == 500) {
                            ToastUtils.show(EntranceActivity.this, res.getMsg());
                        } else {
                            ToastUtils.show(EntranceActivity.this, "注册完成，请登陆");
                        }
                    }
                }
            });
        }
    }

}