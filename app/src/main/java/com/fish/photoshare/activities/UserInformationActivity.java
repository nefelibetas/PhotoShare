package com.fish.photoshare.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fish.photoshare.R;
import com.fish.photoshare.common.OnFragmentChangeListener;
import com.fish.photoshare.fragments.UserHomeFragment;
import com.fish.photoshare.pojo.User;
import com.fish.photoshare.utils.ToastUtils;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.dialog.MaterialDialogs;
import com.google.android.material.imageview.ShapeableImageView;

public class UserInformationActivity extends AppCompatActivity implements OnFragmentChangeListener {
    private FragmentManager manager;
    private UserHomeFragment userHomeFragment;
    private ImageView iv_back;
    private TextView tv_title;
    private TextView tv_save;
    private User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_information);
        initView();
        dataHandler(getIntent());
        manager.beginTransaction()
                .add(R.id.userFragmentContainer, userHomeFragment)
                .commit();
    }
    public void dataHandler(Intent intent) {
        Bundle bundle = intent.getExtras();
        user = bundle.getSerializable("information", User.class);
        if (user != null) {
            userHomeFragment = UserHomeFragment.newInstance(user);
        }
    }
    public void initView() {
        // manager
        manager = getSupportFragmentManager();
        // 顶部标题和保存
        tv_title = findViewById(R.id.edit_title);
        tv_save = findViewById(R.id.edit_save);
        tv_save.setVisibility(View.INVISIBLE);
        // 顶部返回按钮
        iv_back = findViewById(R.id.icon_back);
        iv_back.setOnClickListener(v -> {
            Fragment fragment = manager.findFragmentById(R.id.userFragmentContainer);
            if (fragment instanceof UserHomeFragment)
                finish();
            else {
                manager.beginTransaction()
                        .replace(R.id.userFragmentContainer, userHomeFragment)
                        .commit();
                tv_save.setVisibility(View.INVISIBLE);
                onFragmentChanged("修改个人信息", false);
            }

        });
    }

    @Override
    public void onFragmentChanged(String title, boolean flag) {
        tv_title.setText(title);
        if (flag)
            tv_save.setVisibility(View.VISIBLE);
        else
            tv_save.setVisibility(View.INVISIBLE);

    }
}