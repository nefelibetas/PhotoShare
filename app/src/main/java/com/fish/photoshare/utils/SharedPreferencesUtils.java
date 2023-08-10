package com.fish.photoshare.utils;


import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.fish.photoshare.R;
import com.fish.photoshare.activities.MainActivity;
import com.fish.photoshare.pojo.User;

public class SharedPreferencesUtils {

    private static final String PREFERENCES_NAME = "sp_user_information";
    private static ResourcesUtils resourcesUtils;
    // 保存字符串
    public static void saveString(Context context, String key, String value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    // 获取字符串
    public static String getString(Context context, String key, String defaultValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(key, defaultValue);
    }

    // 保存整型
    public static void saveInt(Context context, String key, int value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    // 获取整型
    public static int getInt(Context context, String key, int defaultValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(key, defaultValue);
    }

    // 保存布尔型
    public static void saveBoolean(Context context, String key, boolean value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    // 获取布尔型
    public static boolean getBoolean(Context context, String key, boolean defaultValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(key, defaultValue);
    }

    // 删除指定键值对
    public static void remove(Context context, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(key);
        editor.apply();
    }

    // 清空所有数据
    public static void clear(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
    // 从SharedPreferences中读取全部数据并整合成User对象返回
    public static User getUser(Context context) {
        resourcesUtils = new ResourcesUtils(context);
        // value
        String userId = SharedPreferencesUtils.getString(context, resourcesUtils.ID, null);
        String username = SharedPreferencesUtils.getString(context, resourcesUtils.USERNAME, null);
        String password = SharedPreferencesUtils.getString(context, resourcesUtils.PASSWORD, null);
        String userAvatar = SharedPreferencesUtils.getString(context, resourcesUtils.AVATAR, null);
        String userIntroduce = SharedPreferencesUtils.getString(context, resourcesUtils.INTRODUCE, null);
        String userSex = SharedPreferencesUtils.getString(context, resourcesUtils.SEX, null);
        String userCreateTime = SharedPreferencesUtils.getString(context, resourcesUtils.CREATE_TIME, null);
        String userLastUpdateTime = SharedPreferencesUtils.getString(context, resourcesUtils.LAST_UPDATE_TIME, null);
        // 构造user,传递给UserFragment, 然后渲染
        User user = new User(userId, HttpUtils.APPID, username, password, userSex, userIntroduce, userAvatar, userCreateTime, userLastUpdateTime);
        return user;
    }
}