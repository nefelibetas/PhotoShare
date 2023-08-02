package com.fish.photoshare.utils;

import android.os.NetworkOnMainThreadException;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class HttpUtils {
    private static final String APPID = "d4be705c704c434c868938cc000d5d61";
    private static final String APPSECRET = "489343d4c32945471492e945cdfe9833caf7f";
    private static final int TIMEOUT = 120;
    private static MediaType MEDIA_TYPE_JSON;
    private static OkHttpClient client;
    private static Headers headers;
    private static Gson gson;
    static {
        client = new OkHttpClient.Builder()
                .readTimeout(TIMEOUT, TimeUnit.SECONDS)
                .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(TIMEOUT, TimeUnit.SECONDS)
                .build();
        gson = new Gson();
        headers = new Headers.Builder()
                .add("Accept", "application/json, text/plain, */*")
                .add("appId", APPID)
                .add("appSecret", APPSECRET)
                .add("Content-Type", "application/json")
                .build();
        MEDIA_TYPE_JSON = MediaType.Companion.parse("application/json; charset=utf-8");
    }
    public static void sendGetRequest(String url, HashMap<String, String> params, Callback callback) {
        new Thread(()->{
            StringBuffer stringBuffer = new StringBuffer(url);
            stringBuffer.append("?");
            for (String key : params.keySet()) {
                stringBuffer.append(key + "=" + params.get(key));
            }
            Request request = new Request.Builder()
                    .url(url)
                    .headers(headers)
                    .get()
                    .build();
            try {
                client.newCall(request).enqueue(callback);
            } catch (NetworkOnMainThreadException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public static void sendPostRequest(String url, HashMap<String, String> params, Callback callback) {
        new Thread(()->{
            String body = gson.toJson(params);
            Request request = new Request.Builder()
                    .url(url)
                    .headers(headers)
                    .post(RequestBody.Companion.create(body, MEDIA_TYPE_JSON))
                    .build();
            try {
                client.newCall(request).enqueue(callback);
            } catch (NetworkOnMainThreadException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
