<<<<<<< Updated upstream
package com.fish.photoshare.utils;public class HttpUtils {
=======
package com.fish.photoshare.utils;

import java.util.HashMap;

import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class HttpUtils {
    private static final String APPID = "d4be705c704c434c868938cc000d5d61";
    private static final String APPSECRET = "489343d4c32945471492e945cdfe9833caf7f";
    private static OkHttpClient client;
    private static Headers headers;
    private static HashMap<String, String> Urls;
    static {
        client = new OkHttpClient();
        headers = new Headers.Builder()
                .add("Accept", "application/json, text/plain, */*")
                .add("appId", APPID)
                .add("appSecret", APPSECRET)
                .add("Content-Type", "application/json")
                .build();
    }
>>>>>>> Stashed changes
}
