package com.fish.photoshare.utils;

import android.os.NetworkOnMainThreadException;

import com.fish.photoshare.common.Result;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

public class HttpUtils <T> {
    public static final String APPID = "d4be705c704c434c868938cc000d5d61";
    private static final String APPSECRET = "489343d4c32945471492e945cdfe9833caf7f";
    private static final int TIMEOUT = 120;
    private static MediaType MEDIA_TYPE_JSON;
    private static MediaType MEDIA_TYPE_FORM_DATA;
    private static OkHttpClient client;
    private static Headers GeneralHeaders;
    private static Headers JsonHeaders;
    private static Headers FormHeaders;
    public static Gson gson;
    public static Type resultType;
    static {
        client = new OkHttpClient.Builder()
                .readTimeout(TIMEOUT, TimeUnit.SECONDS)
                .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(TIMEOUT, TimeUnit.SECONDS)
                .build();
        gson = new Gson();
        GeneralHeaders = new Headers.Builder()
                .add("Accept", "application/json, text/plain, */*")
                .add("appId", APPID)
                .add("appSecret", APPSECRET)
                .build();
        JsonHeaders = new Headers.Builder()
                .add("Accept", "application/json, text/plain, */*")
                .add("appId", APPID)
                .add("appSecret", APPSECRET)
                .add("Content-Type", "application/json")
                .build();
        FormHeaders = new Headers.Builder()
                .add("Accept", "application/json, text/plain, */*")
                .add("appId", APPID)
                .add("appSecret", APPSECRET)
                .add("Content-Type", "multipart/form-data")
                .build();
        MEDIA_TYPE_JSON = MediaType.Companion.parse("application/json; charset=utf-8");
        MEDIA_TYPE_FORM_DATA = MediaType.Companion.parse("multipart/form-data; charset=utf-8");
        resultType = new TypeToken<Result<Object>>(){}.getType();
    }
    // get请求url构造器
    public static String getRequestHandler(String url, HashMap<String, String> params ) {
        StringBuffer getUrl = new StringBuffer(url);
        getUrl.append("?");
        StringBuffer sb = new StringBuffer();
        for (String key : params.keySet()) {
            sb.append(key);
            sb.append("=");
            sb.append(params.get(key));
            sb.append("&");
        }
        if (sb.lastIndexOf("&") == sb.length()) {
            sb.setLength(sb.length() - 1);
        }
        getUrl.append(sb);
        return getUrl.toString();
    }
    // 发送get请求
    public static void sendGetRequest(String url, HashMap<String, String> params, Callback callback) {
        new Thread(()->{
            String getUrl = getRequestHandler(url, params);
            Request request = new Request.Builder()
                    .url(getUrl)
                    .headers(GeneralHeaders)
                    .get()
                    .build();
            try {
                client.newCall(request).enqueue(callback);
            } catch (NetworkOnMainThreadException e) {
                e.printStackTrace();
            }
        }).start();
    }
    // json内容体的post请求
    public static <T> void sendPostRequest(String url, HashMap<String, T> params, Callback callback) {
        new Thread(()->{
            String body = gson.toJson(params);
            Request request = new Request.Builder()
                    .url(url)
                    .headers(JsonHeaders)
                    .post(RequestBody.Companion.create(body, MEDIA_TYPE_JSON))
                    .build();
            try {
                client.newCall(request).enqueue(callback);
            } catch (NetworkOnMainThreadException e) {
                e.printStackTrace();
            }
        }).start();
    }
    // 表单构造器，专用于图片上传
    public static RequestBody FormDataHandler (ArrayList<File> fileList) {
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);
        RequestBody body = null;
        for (int i = 0; i < fileList.size(); i++){
            File file = fileList.get(i);
            try {
                byte[] bytes = MyFileUtils.convertFileToBytes(file);
                builder.addFormDataPart("fileList", file.getName(), RequestBody.Companion.create(bytes, MEDIA_TYPE_FORM_DATA));
                body = builder.build();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return body;
    }
    // 专用于图片上传的post请求
    public static void sendPostRequestForFormData(String url, ArrayList<File> fileList, Callback callback) {
        new Thread(()->{
            RequestBody body = FormDataHandler(fileList);
            Request request = new Request.Builder()
                    .url(url)
                    .headers(FormHeaders)
                    .post(body)
                    .build();
            try {
                client.newCall(request).enqueue(callback);
            } catch (NetworkOnMainThreadException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
