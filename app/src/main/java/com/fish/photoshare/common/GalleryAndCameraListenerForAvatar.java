package com.fish.photoshare.common;

import com.google.android.material.imageview.ShapeableImageView;

public interface GalleryAndCameraListenerForAvatar {
    void onOpenGallery(ShapeableImageView avatar);
    void onOpenCamera(ShapeableImageView avatar);
}
