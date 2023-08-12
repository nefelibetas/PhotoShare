package com.fish.photoshare.pojo;

import android.media.Image;

import java.util.ArrayList;

public class Images {
    private String imageCode;
    private ArrayList<String> imageUrlList;
    public Images() {}
    public Images(String code, ArrayList<String> list) {
        imageCode = code;
        imageUrlList = list;
    }
    public String getImageCode() {
        return imageCode;
    }
    public void setImageCode(String imageCode) {
        this.imageCode = imageCode;
    }
    public ArrayList<String> getImageUrlList() {
        return imageUrlList;
    }
    public void setImageUrlList(ArrayList<String> imageUrlList) {
        this.imageUrlList = imageUrlList;
    }
    @Override
    public String toString() {
        return "Images{" +
                "imageCode='" + imageCode + '\'' +
                ", imageUrlList=" + imageUrlList +
                '}';
    }
}
