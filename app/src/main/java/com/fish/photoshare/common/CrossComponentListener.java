package com.fish.photoshare.common;

import com.google.android.material.imageview.ShapeableImageView;

public interface CrossComponentListener {
    void onOpenGallery(ShapeableImageView avatar);
    void onOpenCamera(ShapeableImageView avatar);
    void onSuccessBindUser(String avatarUrl);
}
