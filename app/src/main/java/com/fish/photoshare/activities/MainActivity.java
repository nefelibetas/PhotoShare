package com.fish.photoshare.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.fish.photoshare.R;
import com.fish.photoshare.common.CallBackHandler;
import com.fish.photoshare.fragments.HomeFragment;
import com.fish.photoshare.fragments.UserFragment;
import com.fish.photoshare.pojo.User;
import com.fish.photoshare.utils.SharedPreferencesUtils;
import com.fish.photoshare.utils.ToastUtils;
import com.fish.photoshare.utils.UserStateUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity implements CallBackHandler {
    private BottomNavigationView mNavigationView;
    private FragmentManager manager;
    private HomeFragment homeFragment;
    private UserFragment userFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        changeActivityHandler();
        initHomeFragment();
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
        homeFragment = HomeFragment.newInstance();
        manager.beginTransaction()
                .add(R.id.home_fragment_container, homeFragment)
                .commit();
    }
    private void changeActivityHandler() {
        String userIdKey = getResources().getString(R.string.user_id);
        // 如果在本地已经保存有用户的信息,则进行校验
        User user = SharedPreferencesUtils.getUser(MainActivity.this);
        // 用userId判断登陆状态
        String userId = SharedPreferencesUtils.getString(MainActivity.this, userIdKey, null);
        if (userId != null && !userId.equals("")) {
            // 对登陆状态进行校验，等待回调即可
            UserStateUtils.userInformationIsOkHandler(user, MainActivity.this, this);
        } else {
            Intent intent = new Intent(MainActivity.this, EntranceActivity.class);
            startActivity(intent);
            finish();
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
    @Override
    public void onHandleSuccess() {
        // 校验成功则生成Fragment
        userFragment = UserFragment.newInstance();
    }
    @Override
    public void onHandleFailure() {
        // 校验不成功跳转到登陆注册页面
        Intent intent = new Intent(MainActivity.this, EntranceActivity.class);
        startActivity(intent);
        finish();
    }
}