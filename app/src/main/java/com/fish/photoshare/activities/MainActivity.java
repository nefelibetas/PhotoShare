package com.fish.photoshare.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.fish.photoshare.R;
import com.fish.photoshare.common.UserCallBackHandler;
import com.fish.photoshare.fragments.HomeFragment;
import com.fish.photoshare.fragments.UserFragment;
import com.fish.photoshare.pojo.User;
import com.fish.photoshare.utils.SharedPreferencesUtils;
import com.fish.photoshare.utils.ToastUtils;
import com.fish.photoshare.utils.UserStateUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity implements UserCallBackHandler {
    private BottomNavigationView mNavigationView;
    private FragmentManager manager;
    private HomeFragment homeFragment;
    private UserFragment userFragment;
    private User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        changeActivityHandler();
        initHomeFragment();
        initNavigation();
        Bundle bundle = getIntent().getExtras();
        initUserFragment(bundle);
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
            User user = bundle.getSerializable("loginResult", User.class);
            userFragment = UserFragment.newInstance(user);
        }
    }

    private void changeActivityHandler() {
        String userIdKey = getResources().getString(R.string.user_id);
        // 如果在本地已经保存有用户的信息了，那就使用保存的信息渲染UserFragment
        user = SharedPreferencesUtils.getUser(MainActivity.this);
        // 用userId判断登陆状态
        String userId = SharedPreferencesUtils.getString(MainActivity.this, userIdKey, null);
        if (userId != null && !userId.equals("")) {
            // 对登陆状态进行校验，等待回调即可
            UserStateUtils.userInformationIsOkHandler(user, MainActivity.this, this);
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
    @Override
    public void onHandleSuccess() {
        userFragment = UserFragment.newInstance(user);
    }
    @Override
    public void onHandleFailure() {
        Intent intent = new Intent(MainActivity.this, EntranceActivity.class);
        startActivity(intent);
    }
}