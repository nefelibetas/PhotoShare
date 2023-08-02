<<<<<<< Updated upstream
package com.fish.photoshare;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;
=======
package com.fish.photoshare.activities;

import android.os.Bundle;
>>>>>>> Stashed changes

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

<<<<<<< Updated upstream
=======
import com.fish.photoshare.R;
>>>>>>> Stashed changes
import com.fish.photoshare.fragments.HomeFragment;
import com.fish.photoshare.fragments.UserFragment;
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
        initFragment();
        initNavigation();
    }

    private void initNavigation(){
        mNavigationView = findViewById(R.id.navigation_bottom);
        mNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.menu_home) {
                return replaceFragment(homeFragment, itemId);
            } else if (itemId == R.id.menu_add) {
                ToastUtils.show(MainActivity.this, "还没有考虑好怎么做");
                // todo： 跳转页面到 发布新帖子
                return true;
            } else if (itemId == R.id.menu_user) {
                return replaceFragment(userFragment, itemId);
            }
            return false;
        });
    }

    private void initFragment(){
        manager = getSupportFragmentManager();
        homeFragment = HomeFragment.newInstance(null, null);
        userFragment = UserFragment.newInstance(null, null);
        manager.beginTransaction()
<<<<<<< Updated upstream
                .add(R.id.fragment_container, homeFragment)
=======
                .add(R.id.home_fragment_container, homeFragment)
>>>>>>> Stashed changes
                .commit();
    }

    public boolean replaceFragment(Fragment fragment, int itemId) {
        if (fragment != null) {
            manager.beginTransaction()
<<<<<<< Updated upstream
                    .replace(R.id.fragment_container, fragment)
=======
                    .replace(R.id.home_fragment_container, fragment)
>>>>>>> Stashed changes
                    .commit();
            return true;
        }
        return false;
    }
}