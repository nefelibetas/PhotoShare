package com.fish.photoshare.utils;

import android.content.Context;

import com.fish.photoshare.R;

public class ResourcesUtils {
    public String ID;
    public String USERNAME;
    public String PASSWORD;
    public String INTRODUCE;
    public String SEX;
    public String AVATAR;
    public String CREATE_TIME;
    public String LAST_UPDATE_TIME;
    private Context context;
    public ResourcesUtils() {}
    public ResourcesUtils(Context mContext) {
        context = mContext;
        ID = context.getResources().getString(R.string.user_id);
        PASSWORD = context.getResources().getString(R.string.user_password);
        USERNAME = context.getResources().getString(R.string.user_name);
        INTRODUCE = context.getResources().getString(R.string.user_introduce);
        SEX = context.getResources().getString(R.string.user_sex);
        AVATAR = context.getResources().getString(R.string.user_avatar);
        CREATE_TIME = context.getResources().getString(R.string.user_createTime);
        LAST_UPDATE_TIME = context.getResources().getString(R.string.user_lastUpdateTime);
    }
}
