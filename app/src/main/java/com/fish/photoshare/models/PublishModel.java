package com.fish.photoshare.models;

import android.content.Context;
import android.view.View;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fish.photoshare.R;
import com.fish.photoshare.adapter.PublishAdapter;
import com.fish.photoshare.common.GalleryAndCameraListenerForPublish;
import com.fish.photoshare.common.onButtonClick;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;

public class PublishModel {
    private TextInputEditText titleInput;
    private TextInputEditText contentInput;
    private MaterialCardView uploadImageCard;
    private MaterialButton saveButton;
    private MaterialButton saveAndPostButton;
    private RecyclerView recyclerListPublish;
    private BottomSheetDialog imageDialog;
    private MaterialButton cameraButton;
    private MaterialButton ImageButton;
    private Context context;
    private GalleryAndCameraListenerForPublish galleryAndCameraListenerForPublish;
    private onButtonClick clickFunction;
    private PublishAdapter publishAdapter;
    private GridLayoutManager manager;
    public PublishModel() {}
    public PublishModel(Context mContext, PublishAdapter adapter, GalleryAndCameraListenerForPublish listener, onButtonClick click) {
        this.context = mContext;
        this.galleryAndCameraListenerForPublish = listener;
        this.clickFunction = click;
        this.publishAdapter = adapter;
    }
    public void initView(View rootView) {
        titleInput = rootView.findViewById(R.id.titleInput);
        contentInput = rootView.findViewById(R.id.titleInput);
        uploadImageCard = rootView.findViewById(R.id.uploadImages);
        saveButton = rootView.findViewById(R.id.saveButton);
        saveAndPostButton = rootView.findViewById(R.id.saveAndPostButton);
        recyclerListPublish = rootView.findViewById(R.id.recyclerListPublish);
        imageDialog = new BottomSheetDialog(context);
        imageDialog.setContentView(R.layout.bottom_sheet_avatar_dialog);
        cameraButton = imageDialog.findViewById(R.id.cameraButton);
        ImageButton = imageDialog.findViewById(R.id.ImageButton);
    }
    public void initOnClickListener() {
        uploadImageCard.setOnClickListener(v -> {
            imageDialog.show();
        });
        cameraButton.setOnClickListener(v -> {
            galleryAndCameraListenerForPublish.onOpenCamera();
            imageDialog.cancel();
        });
        ImageButton.setOnClickListener(v -> {
            galleryAndCameraListenerForPublish.onOpenGallery();
            imageDialog.cancel();
        });
        // 保存按钮
        saveButton.setOnClickListener(v -> {
            clickFunction.onClick();
        });
        saveAndPostButton.setOnClickListener(v -> {
            clickFunction.onClick();
        });
    }
    public void setAdapter() {
        manager = new GridLayoutManager(context, 3);
        recyclerListPublish.setLayoutManager(manager);
        recyclerListPublish.setAdapter(publishAdapter);
    }
}
