package com.fish.photoshare.common;

public interface RequestHandler {
    void onSuccess(String id);
    void onFailure();
}
