package com.fish.photoshare.common;

public class CrossComponentHandler {
    public static OnCrossComponentClickListener crossComponentClickListener;
    public CrossComponentHandler(){}
    public CrossComponentHandler(OnCrossComponentClickListener listener) {
        crossComponentClickListener = listener;
    }
    public static OnCrossComponentClickListener getListener() {
        return crossComponentClickListener;
    }
    public void cancel() {
        crossComponentClickListener = null;
    }
}
