package com.fish.photoshare.common;

import java.io.Serializable;

public class onChangePostHandler implements Serializable {
    public final onChangePostState listener;
    public onChangePostHandler(onChangePostState listener) {
        this.listener = listener;
    }
}
