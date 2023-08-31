package com.fish.photoshare.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;

import com.fish.photoshare.common.Api;
import com.fish.photoshare.common.RequestHandler;
import com.fish.photoshare.common.Result;
import com.fish.photoshare.pojo.User;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class UserStateUtils {
    public static void userInformationIsOkHandler(User user, Context context, RequestHandler userRequestHandler) {
        String username = user.getUsername();
        String password = user.getPassword();
        HashMap<String, String> params = new HashMap<>();
        params.put("username", username);
        params.put("password", password);
        String newUrl = HttpUtils.getRequestHandler(Api.LOGIN, params);
        HttpUtils.sendPostRequest(newUrl, null, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.e("fishCat", "userInformationIsOkHandler onFailure: ", e);
            }
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                // 没有出现异常
                if (response.isSuccessful()) {
                    String body = response.body().string();
                    Result<User> result = HttpUtils.gson.fromJson(body, new TypeToken<Result<User>>() {}.getType());
                    // 没有成功登陆
                    if (result.getCode() != 200) {
                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.post(() -> ToastUtils.show(context, result.getMsg()));
                        userRequestHandler.onFailure();
                    } else {
                        String id = result.getData().getId();
                        userRequestHandler.onSuccess(id);
                    }
                } else {
                    Log.d("fishCat", "userInformationIsOkHandler onResponse: " + "出现了问题" );
                }
            }
        });
    }
}
