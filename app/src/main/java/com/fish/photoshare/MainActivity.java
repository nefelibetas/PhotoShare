package com.fish.photoshare;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.fish.photoshare.fragments.HomeFragment;
import com.fish.photoshare.fragments.UserFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;


public class MainActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener {
    private BottomNavigationView mNavigationView;
    private FragmentManager manager;

    private HomeFragment homeFragment;
    private UserFragment userFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initNavigation();
        manager = getSupportFragmentManager();
        homeFragment = new HomeFragment();
        userFragment = new UserFragment();
        manager.beginTransaction()
                .add(R.id.fragment_container, homeFragment)
                .commit();
    }
    private void initNavigation(){
        mNavigationView = findViewById(R.id.navigation_bottom);
        mNavigationView.setOnItemSelectedListener(this);
    }



    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_home){
            manager.beginTransaction()
                    .replace(R.id.fragment_container, homeFragment)
                    .commit();
            return true;
        } else if (item.getItemId() == R.id.menu_add) {
            // todo： 跳转页面到 发布新帖子
            return true;
        } else if (item.getItemId() == R.id.menu_user) {
            manager.beginTransaction()
                    .replace(R.id.fragment_container, userFragment)
                    .commit();
            return true;
        }
        return false;
    }
}