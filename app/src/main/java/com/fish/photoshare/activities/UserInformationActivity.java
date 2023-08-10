package com.fish.photoshare.activities;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.fish.photoshare.R;
import com.fish.photoshare.fragments.user.UserHomeFragment;

public class UserInformationActivity extends AppCompatActivity {
    private FragmentManager manager;
    private UserHomeFragment userHomeFragment;
    private ImageView iv_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_information);
        initView();
        userHomeFragment = UserHomeFragment.newInstance();
        manager.beginTransaction()
                .add(R.id.userFragmentContainer, userHomeFragment)
                .commit();
    }
    public void initView() {
        // manager
        manager = getSupportFragmentManager();
        // 顶部返回按钮
        iv_back = findViewById(R.id.icon_back);
        initOnclickListener();
    }
    public void initOnclickListener() {
        iv_back.setOnClickListener(v -> {
            finish();
        });
    }
}