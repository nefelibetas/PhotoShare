package com.fish.photoshare.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fish.photoshare.R;
import com.google.android.material.textfield.TextInputEditText;

public class InputFragment extends Fragment {
    private static final String INPUT = "INPUT";
    private String input;
    private TextInputEditText inputEditText;
    public InputFragment() {}
    public static InputFragment newInstance(String username) {
        InputFragment fragment = new InputFragment();
        Bundle args = new Bundle();
        args.putString(INPUT, username);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            input = getArguments().getString(INPUT);
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_input, container, false);
        initView(rootView);
        return rootView;
    }
    public void initView(View rootView) {
        inputEditText = rootView.findViewById(R.id.Input);
        inputEditText.setHint(input);
    }
}