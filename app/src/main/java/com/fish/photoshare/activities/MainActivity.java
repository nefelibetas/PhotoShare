package com.fish.photoshare.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.fish.photoshare.R;
import com.fish.photoshare.fragments.HomeFragment;
import com.fish.photoshare.fragments.PublishFragment;
import com.fish.photoshare.fragments.UserFragment;
import com.fish.photoshare.utils.ResourcesUtils;
import com.fish.photoshare.utils.SharedPreferencesUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private ResourcesUtils resourcesUtils;
    private FragmentManager manager;
    private HomeFragment homeFragment;
    private UserFragment userFragment;
    private PublishFragment publishFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        resourcesUtils = new ResourcesUtils(MainActivity.this);
        changeActivityHandler();
        publishFragment = PublishFragment.newInstance();
        userFragment = UserFragment.newInstance();
        initHomeFragment();
        initNavigation();
    }
    private void initNavigation() {
        BottomNavigationView mNavigationView = findViewById(R.id.navigation_bottom);
        mNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.menu_home) {
                return replaceFragment(homeFragment);
            } else if (itemId == R.id.menu_add) {
                return replaceFragment(publishFragment);
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
        String userIdKey = resourcesUtils.ID;
        // 用userId判断登陆状态
        String userId = SharedPreferencesUtils.getString(MainActivity.this, userIdKey, null);
        if (userId != null && !userId.equals("")) {

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
}