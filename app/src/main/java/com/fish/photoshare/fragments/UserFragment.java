package com.fish.photoshare.fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fish.photoshare.R;
import com.fish.photoshare.utils.ToastUtils;
import com.google.android.material.card.MaterialCardView;

public class UserFragment extends Fragment implements View.OnClickListener {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private MaterialCardView editCard;
    private MaterialCardView settingCard;
    private MaterialCardView starCard;

    public static UserFragment newInstance(String param1, String param2) {
        UserFragment fragment = new UserFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public UserFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_user, container, false);
        editCard = rootView.findViewById(R.id.editCard);
        starCard = rootView.findViewById(R.id.starCard);
        settingCard = rootView.findViewById(R.id.settingCard);
        editCard.setOnClickListener(this);
        starCard.setOnClickListener(this);
        settingCard.setOnClickListener(this);
        return rootView;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        String msg = null;
        if (id == R.id.editCard) {
            msg = "修改信息";
        } else if (id == R.id.starCard) {
            msg = "收藏页面";
        } else if (id == R.id.settingCard) {
            msg = "设置界面";
        }
        ToastUtils.show(getContext(), msg);
    }
}