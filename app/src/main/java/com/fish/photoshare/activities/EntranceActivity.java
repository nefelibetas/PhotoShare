package com.fish.photoshare.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;

import com.fish.photoshare.R;
import com.fish.photoshare.common.Api;
import com.fish.photoshare.utils.HttpUtils;
import com.fish.photoshare.utils.ToastUtils;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.textfield.TextInputEditText;

import java.io.IOException;
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
        HttpUtils.sendPostRequest(Api.LOGIN, params, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
                Log.d("info", "Login onFailure" + e.getMessage());
            }
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                Intent intent = new Intent(EntranceActivity.this, MainActivity.class);
                Bundle bundle = new Bundle();
                intent.putExtras(bundle);
                startActivity(intent);
                if (rememberPassword.isChecked()) {

                }
            }
        });
    }
    public void registerHandler(String username, String password){
        int length = password.length();
    }

}