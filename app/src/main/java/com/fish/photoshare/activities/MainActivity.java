package com.fish.photoshare.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.fish.photoshare.R;
import com.fish.photoshare.fragments.HomeFragment;
import com.fish.photoshare.fragments.UserFragment;
import com.fish.photoshare.pojo.User;
import com.fish.photoshare.utils.HttpUtils;
import com.fish.photoshare.utils.SharedPreferencesUtils;
import com.fish.photoshare.utils.ToastUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView mNavigationView;
    private FragmentManager manager;
    private HomeFragment homeFragment;
    private UserFragment userFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initHomeFragment();
        Bundle bundle = getIntent().getExtras();
        initUserFragment(bundle);
        initNavigation();
    }



    private void initNavigation() {
        mNavigationView = findViewById(R.id.navigation_bottom);
        mNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.menu_home) {
                return replaceFragment(homeFragment);
            } else if (itemId == R.id.menu_add) {
                ToastUtils.show(MainActivity.this, "还没有考虑好怎么做");
                // todo： 跳转页面到 发布新帖子
                return true;
            } else if (itemId == R.id.menu_user) {
                return replaceFragment(userFragment);
            }
            return false;
        });
    }

    private void initHomeFragment() {
        manager = getSupportFragmentManager();
        homeFragment = HomeFragment.newInstance(null, null);
        manager.beginTransaction()
                .add(R.id.home_fragment_container, homeFragment)
                .commit();
    }

    public void initUserFragment(Bundle bundle) {
        if (bundle != null) {
            String result = bundle.getString("loginResult");
            User user = HttpUtils.gson.fromJson(result, User.class);
            userFragment = UserFragment.newInstance(user);
        } else {
            changeActivityHandler();
        }
    }

    // 废弃，有点问题
    private void changeActivityHandler() {
        // 获取Key
        String userIdKey = getResources().getString(R.string.user_id);
        String userPasswordKey = getResources().getString(R.string.user_password);
        String usernameKey = getResources().getString(R.string.user_name);
        String userIntroduceKey = getResources().getString(R.string.user_introduce);
        String userSexKey = getResources().getString(R.string.user_sex);
        String userAvatarKey = getResources().getString(R.string.user_avatar);
        String createTimeKey = getResources().getString(R.string.user_createTime);
        String lastUpdateTimeKey = getResources().getString(R.string.user_lastUpdateTime);
        // 用userId判断登陆状态
        String userId = SharedPreferencesUtils.getString(MainActivity.this, userIdKey, null);
        if (userId != null && !TextUtils.isEmpty(userId)) {
            // 如果在本地已经保存有用户的信息了，那就使用保存的信息渲染UserFragment
            String username = SharedPreferencesUtils.getString(MainActivity.this, usernameKey, null);
            String password = SharedPreferencesUtils.getString(MainActivity.this, userPasswordKey, null);
            String userAvatar = SharedPreferencesUtils.getString(MainActivity.this, userAvatarKey, null);
            String userIntroduce = SharedPreferencesUtils.getString(MainActivity.this, userIntroduceKey, null);
            String userSex = SharedPreferencesUtils.getString(MainActivity.this, userSexKey, null);
            String userCreateTime = SharedPreferencesUtils.getString(MainActivity.this, createTimeKey, null);
            String userLastUpdateTime = SharedPreferencesUtils.getString(MainActivity.this, lastUpdateTimeKey, null);
            // 构造user,传递给UserFragment, 然后渲染
            User user = new User(userId, HttpUtils.APPID, username, password, userSex, userIntroduce, userAvatar, userCreateTime, userLastUpdateTime);
            userFragment = UserFragment.newInstance(user);
        } else {
            Intent intent = new Intent(MainActivity.this, EntranceActivity.class);
            startActivity(intent);
        }
    }

    public boolean replaceFragment(Fragment fragment) {
        if (fragment != null) {
            manager.beginTransaction()
                    .replace(R.id.home_fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }
}