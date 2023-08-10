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
import com.fish.photoshare.models.EntranceModel;
import com.fish.photoshare.pojo.User;
import com.fish.photoshare.utils.FormatUtils;
import com.fish.photoshare.utils.HttpUtils;
import com.fish.photoshare.utils.ResourcesUtils;
import com.fish.photoshare.utils.SharedPreferencesUtils;
import com.fish.photoshare.utils.ToastUtils;
import com.fish.photoshare.utils.UserStateUtils;
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
    private EntranceModel entranceModel;
    private ResourcesUtils resourcesUtils;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entrance);
        initView();
        getSharedPreferencesData();
    }
    public void initView(){
        entranceModel = new EntranceModel();
        resourcesUtils = new ResourcesUtils(EntranceActivity.this);
        entranceModel.usernameInput = findViewById(R.id.et_username);
        entranceModel.passwordInput = findViewById(R.id.et_password);
        entranceModel.rememberPassword = findViewById(R.id.mc_remember);
        entranceModel.signInButton = findViewById(R.id.mb_signIn);
        entranceModel.registerButton = findViewById(R.id.mb_register);
        entranceModel.signInButton.setOnClickListener(this);
        entranceModel.registerButton.setOnClickListener(this);
    }

    private void getSharedPreferencesData() {
        String username = SharedPreferencesUtils.getString(EntranceActivity.this, resourcesUtils.USERNAME, null);
        String password = SharedPreferencesUtils.getString(EntranceActivity.this, resourcesUtils.PASSWORD, null);
        if ((username != null && password != null) && !TextUtils.isEmpty(username) && !TextUtils.isEmpty(password)) {
            entranceModel.usernameInput.setText(username);
            entranceModel.passwordInput.setText(password);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        String  username = entranceModel.usernameInput.getText().toString();
        String  password = entranceModel.passwordInput.getText().toString();
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
                Log.d("fishCat", "Login onFailure" + e.getMessage());
            }
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    // 将账密传到主页面，会在UserFragment渲染
                    Bundle bundle = new Bundle();
                    // 获取响应体内的数据，转化成字符串再转化成指定类型的数据
                    String body = response.body().string();
                    Result<User> result = HttpUtils.gson.fromJson(body, new TypeToken<Result<User>>(){}.getType());
                    Log.d("fishCat", "Login onResponse result data: " + result);
                    if (result.getCode() != 200) {
                        ToastUtils.show(EntranceActivity.this, result.getMsg());
                    } else {
                        // 获取到的数据没有异常就可以跳转到主界面，然后传递数据
                        Intent intent = new Intent(EntranceActivity.this, MainActivity.class);
                        bundle.putSerializable("loginResult", result.getData());
                        intent.putExtras(bundle);
                        User user = result.getData();
                        // 登陆后保存登陆状态，有修改再获取数据
                        if (entranceModel.rememberPassword.isChecked()) {
                            SharedPreferencesUtils.saveString(EntranceActivity.this, resourcesUtils.ID, user.getId());
                            SharedPreferencesUtils.saveString(EntranceActivity.this, resourcesUtils.USERNAME, username);
                            SharedPreferencesUtils.saveString(EntranceActivity.this, resourcesUtils.PASSWORD, password);
                            SharedPreferencesUtils.saveString(EntranceActivity.this, resourcesUtils.SEX, user.getSex());
                            SharedPreferencesUtils.saveString(EntranceActivity.this, resourcesUtils.AVATAR, user.getAvatar());
                            SharedPreferencesUtils.saveString(EntranceActivity.this, resourcesUtils.INTRODUCE, user.getIntroduce());
                            SharedPreferencesUtils.saveString(EntranceActivity.this, resourcesUtils.CREATE_TIME, user.getCreateTime().toString());
                            SharedPreferencesUtils.saveString(EntranceActivity.this, resourcesUtils.LAST_UPDATE_TIME, user.getLastUpdateTime().toString());
                        } else {
                            SharedPreferencesUtils.clear(EntranceActivity.this);
                        }
                        startActivity(intent);
                        finish();
                    }
                } else {
                    Log.d("fishCat", "signInHandler onResponse: 出现问题");
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
                        Result<Object> res = HttpUtils.gson.fromJson(body, new TypeToken<Result<Object>>(){}.getType());
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